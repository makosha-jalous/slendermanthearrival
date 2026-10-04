package com.example.slenderman;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Comparator;
import java.util.List;

/**
 * Test commands (operators only):
 *   /slenderteleport far|mid|close|vanish   - makes the nearest Slenderman teleport relative to you
 *   /slenderteleport auto on|off            - switches the placeholder random teleports
 */
@Mod.EventBusSubscriber(modid = SlendermanMod.ID)
public class TeleportCommand {

    @SubscribeEvent
    public static void register(RegisterCommandsEvent e) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("slenderteleport")
                .requires(s -> s.hasPermission(2));

        root.then(Commands.argument("kind", StringArgumentType.word())
                .suggests((c, b) -> SharedSuggestionProvider.suggest(new String[]{"far", "mid", "close", "vanish"}, b))
                .executes(ctx -> run(ctx.getSource(), StringArgumentType.getString(ctx, "kind"))));

        root.then(Commands.literal("auto")
                .then(Commands.literal("on").executes(ctx -> {
                    SlendermanEntity.autoTeleport = true;
                    ctx.getSource().sendSuccess(Component.literal("Slenderman auto teleport: on"), false);
                    return 1;
                }))
                .then(Commands.literal("off").executes(ctx -> {
                    SlendermanEntity.autoTeleport = false;
                    ctx.getSource().sendSuccess(Component.literal("Slenderman auto teleport: off"), false);
                    return 1;
                })));

        e.getDispatcher().register(root);
    }

    private static int run(CommandSourceStack src, String kindName) {
        SlendermanEntity.TeleportKind kind;
        try {
            kind = SlendermanEntity.TeleportKind.valueOf(kindName.toUpperCase());
        } catch (IllegalArgumentException ex) {
            src.sendFailure(Component.literal("Use: far, mid, close or vanish"));
            return 0;
        }
        try {
            ServerPlayer p = src.getPlayerOrException();
            List<SlendermanEntity> list = p.level.getEntitiesOfClass(SlendermanEntity.class,
                    p.getBoundingBox().inflate(160));
            if (list.isEmpty()) {
                src.sendFailure(Component.literal("No Slenderman within 160 blocks"));
                return 0;
            }
            SlendermanEntity s = list.stream().min(Comparator.comparingDouble(en -> en.distanceToSqr(p))).get();
            boolean ok = s.teleportNear(p, kind);
            if (ok) src.sendSuccess(Component.literal("Slenderman teleported: " + kindName), false);
            else src.sendFailure(Component.literal("No safe place found, try again"));
            return ok ? 1 : 0;
        } catch (Exception ex) {
            src.sendFailure(Component.literal("Only a player can use this"));
            return 0;
        }
    }
}
