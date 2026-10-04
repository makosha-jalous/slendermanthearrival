package com.example.slenderman;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Slenderman behaviour (no link to other mods, no pages yet).
 *
 *  - While a player looks at him he is frozen ("cannot walk").
 *  - While nobody looks he walks straight at the player. The longer he is left unobserved the closer he
 *    dares to come ("closeness"): from a safe distance up to standing right next to the player.
 *  - He looks at the spot where the player WAS when last unobserved; his body does not follow the player.
 *  - Too close: sometimes a sudden reach and lean toward the player.
 *  - Behind the player: sometimes a slight bow, as if peering into the face.
 *  - Long staring makes 6 tentacles unfurl from his back.
 *  - Teleports: FAR (horizon, in view), MID (medium distance in front), CLOSE (right in front of the player)
 *    and VANISH (disappears out of sight while being looked at). The trigger is a placeholder timer
 *    (autoTeleport); real triggers can be added later by calling teleportNear(...).
 */
public class SlendermanEntity extends PathfinderMob {

    public enum TeleportKind { FAR, MID, CLOSE, VANISH }

    private static final EntityDataAccessor<Float> DATA_TENT =
            SynchedEntityData.defineId(SlendermanEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> DATA_ACTION =
            SynchedEntityData.defineId(SlendermanEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DATA_PROGRESS =
            SynchedEntityData.defineId(SlendermanEntity.class, EntityDataSerializers.FLOAT);

    // ================= tweakables =================
    /** Placeholder trigger for teleports (a timer). Switch with /slenderteleport auto on|off. */
    public static boolean autoTeleport = true;

    private static final double SEEN_COS = 0.55;        // how wide the view cone counts as "looking at him"
    private static final float CLOSE_TIME = 20 * 25;    // unobserved ticks until he walks right up to you
    private static final float FAR_DIST = 14F;          // distance he keeps at the start (closeness 0)
    private static final float NEAR_DIST = 1.3F;        // distance at full closeness
    private static final double LUNGE_RANGE = 4.6;
    private static final float LUNGE_CHANCE = 0.012F;
    private static final float PEEK_CHANCE = 0.004F;
    private static final int STARE_TO_TENTACLES = 110;

    public static final int ACTION_NONE = 0, ACTION_LUNGE = 1, ACTION_PEEK = 2;

    // client-side interpolation
    private float tentPrev, tentNow, progPrev, progNow;

    // server-side state
    private int stare, action, actionTimer, actionDuration, lungeCooldown = 60, peekCooldown = 200;
    private int pendingLunge, tpCooldown = 400, observedTicks;
    private float tent, closeness;
    private boolean tentGoal;
    private Vec3 remembered;

    public SlendermanEntity(EntityType<? extends SlendermanEntity> type, Level level) {
        super(type, level);
        this.maxUpStep = 1.5F;
        this.xpReward = 0;
        this.setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100.0)
                .add(Attributes.MOVEMENT_SPEED, 0.26)
                .add(Attributes.FOLLOW_RANGE, 64.0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_TENT, 0F);
        entityData.define(DATA_ACTION, 0);
        entityData.define(DATA_PROGRESS, 0F);
    }

    // ---- client helpers used by the model
    public int getAction() { return entityData.get(DATA_ACTION); }
    public float getProgress(float pt) { return Mth.lerp(pt, progPrev, progNow); }
    public float getTentacles(float pt) { return Mth.lerp(pt, tentPrev, tentNow); }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide) {
            tentPrev = tentNow;
            tentNow = entityData.get(DATA_TENT);
            progPrev = progNow;
            progNow = entityData.get(DATA_PROGRESS);
        }
    }

    // ---- silent, tough, always there
    @Override protected float getStandingEyeHeight(Pose pose, EntityDimensions dims) { return 3.7F; }
    @Override public boolean isPushable() { return false; }
    @Override public boolean isInvulnerableTo(DamageSource source) { return !source.isBypassInvul(); }
    @Override protected void playStepSound(BlockPos pos, BlockState state) { }
    @Override public boolean removeWhenFarAway(double distance) { return false; }
    @Override public boolean canBeLeashed(Player player) { return false; }

    @Override
    public AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().inflate(2.5, 0.5, 2.5);
    }

    // ---------------------------------------------------------------- server brain

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        Player p = level.getNearestPlayer(this, 64.0);
        if (p == null) {
            getNavigation().stop();
            action = ACTION_NONE;
            stare = 0;
            updateTentacles(false);
            entityData.set(DATA_ACTION, 0);
            entityData.set(DATA_PROGRESS, 0F);
            return;
        }

        boolean observed = isSeenBy(p);
        double dist = distanceTo(p);

        if (observed && dist < 45) stare = Math.min(stare + 1, 600);
        else stare = Math.max(0, stare - 3);
        if (stare > STARE_TO_TENTACLES) tentGoal = true;
        else if (stare < 30) tentGoal = false;
        updateTentacles(tentGoal);

        observedTicks = observed ? observedTicks + 1 : 0;
        // the longer he is left alone, the closer he gets; looking at him only slowly undoes it
        if (observed) closeness = Math.max(0F, closeness - 1F / (CLOSE_TIME * 4F));
        else closeness = Math.min(1F, closeness + 1F / CLOSE_TIME);

        if (remembered == null || (!observed && action == ACTION_NONE)) {
            remembered = p.getEyePosition();
        }

        if (lungeCooldown > 0) lungeCooldown--;
        if (peekCooldown > 0) peekCooldown--;

        // ---- running action (reach / peer)
        if (action != ACTION_NONE) {
            actionTimer++;
            float x = actionTimer / (float) actionDuration;
            float prog;
            if (action == ACTION_LUNGE) {
                prog = Mth.sin((float) Math.PI * x);
            } else {
                float t = Math.min(1F, Math.min(x / 0.25F, (1F - x) / 0.25F));
                prog = t * t * (3F - 2F * t);
            }
            entityData.set(DATA_PROGRESS, prog);
            getNavigation().stop();
            setDeltaMovement(0, getDeltaMovement().y, 0);
            faceToward(p.position(), action == ACTION_LUNGE ? 28F : 12F);
            Vec3 eye = p.getEyePosition();
            getLookControl().setLookAt(eye.x, eye.y, eye.z, 30F, 40F);
            if (actionTimer >= actionDuration) {
                action = ACTION_NONE;
                entityData.set(DATA_ACTION, 0);
                entityData.set(DATA_PROGRESS, 0F);
            }
            return;
        }

        // ---- placeholder teleport trigger
        if (autoTeleport && --tpCooldown <= 0) {
            autoTeleport(p, observed, dist);
            return;
        }

        // ---- reach right after a CLOSE teleport
        if (pendingLunge > 0 && --pendingLunge == 0 && dist < 5.5) {
            startAction(ACTION_LUNGE, 26);
            lungeCooldown = 140;
            return;
        }

        // ---- maybe start an action
        if (dist < LUNGE_RANGE && lungeCooldown == 0 && random.nextFloat() < LUNGE_CHANCE * (0.5F + closeness)) {
            startAction(ACTION_LUNGE, 26);
            lungeCooldown = 140 + random.nextInt(120);
            return;
        }
        if (!observed && isBehind(p) && dist > 3.0 && dist < 9.0
                && peekCooldown == 0 && random.nextFloat() < PEEK_CHANCE) {
            startAction(ACTION_PEEK, 70);
            peekCooldown = 400 + random.nextInt(300);
            return;
        }

        // ---- head: look where the player was
        getLookControl().setLookAt(remembered.x, remembered.y, remembered.z, 18F, 40F);

        // ---- movement: frozen while watched, otherwise straight at the player
        if (observed) {
            getNavigation().stop();
            setDeltaMovement(0, getDeltaMovement().y, 0);
        } else {
            approach(p, dist);
        }
    }

    private void startAction(int type, int duration) {
        action = type;
        actionTimer = 0;
        actionDuration = duration;
        entityData.set(DATA_ACTION, type);
        entityData.set(DATA_PROGRESS, 0F);
    }

    private void updateTentacles(boolean out) {
        tent += Mth.clamp((out ? 1F : 0F) - tent, -0.05F, 0.02F);
        entityData.set(DATA_TENT, tent);
    }

    /** Walk straight at the player; the allowed distance shrinks as closeness grows. */
    private void approach(Player p, double dist) {
        float want = Mth.lerp((float) Math.pow(closeness, 1.2), FAR_DIST, NEAR_DIST);
        if (dist <= want + 0.3) {
            getNavigation().stop();
            return;
        }
        double speed = 1.0 + 0.6 * closeness;
        if (tickCount % 8 == 0 || getNavigation().isDone()) {
            boolean ok = getNavigation().moveTo(p.getX(), p.getY(), p.getZ(), speed);
            if (!ok) { // no path (trees, uneven ground): just walk at them
                getMoveControl().setWantedPosition(p.getX(), p.getY(), p.getZ(), speed);
            }
        }
    }

    // ---------------------------------------------------------------- teleports

    private void autoTeleport(Player p, boolean observed, double dist) {
        TeleportKind kind;
        float r = random.nextFloat();
        if (observed) {
            if (observedTicks > 60 && r < 0.35F) kind = TeleportKind.VANISH;
            else if (observedTicks > 60 && r < 0.55F) kind = TeleportKind.CLOSE;
            else if (r < 0.80F) kind = TeleportKind.MID;
            else kind = TeleportKind.FAR;
        } else if (dist > 28) {
            kind = r < 0.7F ? TeleportKind.MID : TeleportKind.FAR; // bring him back into the game
        } else {
            kind = r < 0.5F ? TeleportKind.FAR : TeleportKind.MID;
        }
        boolean ok = teleportNear(p, kind);
        tpCooldown = ok ? 400 + random.nextInt(800) : 60; // 20-60 s between auto teleports
    }

    /**
     * Teleports him relative to the player.
     * FAR: 28-45 blocks away, inside the view cone (a figure on the horizon).
     * MID: 9-16 blocks away, in front of the player.
     * CLOSE: 2-2.6 blocks away, right in front of the player; he reaches out shortly after.
     * VANISH: 25-50 blocks away, out of the player's view (he simply disappears).
     * @return false if no safe standing place was found
     */
    public boolean teleportNear(Player p, TeleportKind kind) {
        Vec3 look = p.getLookAngle();
        double base = Math.atan2(look.z, look.x);
        for (int i = 0; i < 16; i++) {
            double dist, ang;
            switch (kind) {
                case FAR -> { dist = 28 + random.nextDouble() * 17; ang = (random.nextDouble() - 0.5) * 70; }
                case MID -> { dist = 9 + random.nextDouble() * 7; ang = (random.nextDouble() - 0.5) * 60; }
                case CLOSE -> { dist = 1.9 + random.nextDouble() * 0.7; ang = (random.nextDouble() - 0.5) * 14; }
                default -> { dist = 25 + random.nextDouble() * 25;
                    ang = (random.nextBoolean() ? 1 : -1) * (110 + random.nextDouble() * 70); }
            }
            double a = base + Math.toRadians(ang);
            double x = p.getX() + Math.cos(a) * dist;
            double z = p.getZ() + Math.sin(a) * dist;
            double y = findGround(x, z, p.getY());
            if (Double.isNaN(y)) continue;
            float yaw = (float) (Mth.atan2(p.getZ() - z, p.getX() - x) * 57.29577951308232) - 90F;
            moveTo(x, y, z, yaw, 0F);
            this.yBodyRot = yaw;
            this.yHeadRot = yaw;
            getNavigation().stop();
            setDeltaMovement(Vec3.ZERO);
            fallDistance = 0F;
            remembered = p.getEyePosition();
            action = ACTION_NONE;
            entityData.set(DATA_ACTION, 0);
            entityData.set(DATA_PROGRESS, 0F);
            if (kind == TeleportKind.CLOSE) {
                lungeCooldown = 0;
                pendingLunge = 12 + random.nextInt(28);
            }
            return true;
        }
        return false;
    }

    /** Standing place near the player's height (so he does not end up on tree tops). NaN if none. */
    private double findGround(double x, double z, double py) {
        int bx = Mth.floor(x), bz = Mth.floor(z), base = Mth.floor(py);
        for (int k = 0; k <= 16; k++) {
            int dy = (k % 2 == 0) ? k / 2 : -(k + 1) / 2;   // 0, -1, 1, -2, 2 ...
            BlockPos pos = new BlockPos(bx, base + dy, bz);
            if (!level.hasChunkAt(pos)) return Double.NaN;
            BlockPos below = pos.below();
            if (level.getBlockState(below).getCollisionShape(level, below).isEmpty()) continue;
            AABB box = new AABB(x - 0.45, pos.getY(), z - 0.45, x + 0.45, pos.getY() + 4.0, z + 0.45);
            if (level.noCollision(this, box) && !level.containsAnyLiquid(box)) return pos.getY();
        }
        return Double.NaN;
    }

    // ---------------------------------------------------------------- helpers

    private boolean isBehind(Player p) {
        Vec3 look = p.getLookAngle();
        Vec3 flat = new Vec3(look.x, 0, look.z);
        Vec3 toMe = new Vec3(getX() - p.getX(), 0, getZ() - p.getZ());
        if (flat.lengthSqr() < 1e-4 || toMe.lengthSqr() < 1e-4) return false;
        return flat.normalize().dot(toMe.normalize()) < -0.25;
    }

    /** True if the player has him in view (view cone + clear line of sight to head, chest, legs or feet). */
    private boolean isSeenBy(Player p) {
        Vec3 eye = p.getEyePosition();
        Vec3 look = p.getViewVector(1.0F).normalize();
        for (double h : new double[]{3.6, 2.6, 1.4, 0.4}) {
            Vec3 pt = new Vec3(getX(), getY() + h, getZ());
            Vec3 d = pt.subtract(eye);
            double len = d.length();
            if (len < 0.5) return true;
            if (look.dot(d.scale(1.0 / len)) < SEEN_COS) continue;
            BlockHitResult r = level.clip(new ClipContext(eye, pt,
                    ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, p));
            if (r.getType() == HitResult.Type.MISS || r.getLocation().distanceToSqr(pt) < 0.25) return true;
        }
        return false;
    }

    private void faceToward(Vec3 target, float maxStep) {
        double dx = target.x - getX(), dz = target.z - getZ();
        float want = (float) (Mth.atan2(dz, dx) * 57.29577951308232) - 90F;
        float yaw = Mth.approachDegrees(getYRot(), want, maxStep);
        setYRot(yaw);
        this.yBodyRot = yaw;
    }
}
