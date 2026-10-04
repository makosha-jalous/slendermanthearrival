package com.example.slenderman.client;

import com.example.slenderman.SlendermanEntity;
import com.example.slenderman.SlendermanMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * GENERATED together with textures/entity/slenderman.png (UV coordinates match the texture).
 * Built 8x finer than vanilla (1 unit = 1/128 block); the renderer scales it by 1/8, so the
 * figure is 4 blocks tall and the 1024x1024 texture has 128 pixels per block.
 */
public class SlendermanModel extends EntityModel<SlendermanEntity> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(new ResourceLocation(SlendermanMod.ID, "slenderman"), "main");

    private static final float DEG = (float) Math.PI / 180F;

    private final ModelPart root, torso, leftThigh, rightThigh, leftShin, rightShin, neck, head;
    private final ModelPart leftArm, leftForearm, leftHand, rightArm, rightForearm, rightHand;
    private final ModelPart[][] tentacles = new ModelPart[6][8];

    public SlendermanModel(ModelPart root) {
        this.root = root;
        this.leftThigh = root.getChild("left_thigh");
        this.rightThigh = root.getChild("right_thigh");
        this.leftShin = leftThigh.getChild("left_shin");
        this.rightShin = rightThigh.getChild("right_shin");
        this.torso = root.getChild("torso");
        this.neck = torso.getChild("neck");
        this.head = neck.getChild("head");
        this.leftArm = torso.getChild("left_arm");
        this.leftForearm = leftArm.getChild("left_forearm");
        this.leftHand = leftForearm.getChild("left_hand");
        this.rightArm = torso.getChild("right_arm");
        this.rightForearm = rightArm.getChild("right_forearm");
        this.rightHand = rightForearm.getChild("right_hand");
        for (int t = 0; t < 6; t++) {
            ModelPart p = torso.getChild("ten" + t);
            tentacles[t][0] = p;
            for (int j = 1; j < 8; j++) {
                p = p.getChild("s" + j);
                tentacles[t][j] = p;
            }
        }
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition left_thigh = root.addOrReplaceChild("left_thigh",
                CubeListBuilder.create()
                .texOffs(825, 131).addBox(-17F, 0F, -17F, 34F, 58F, 34F)
                .texOffs(301, 131).addBox(-16F, 54F, -16F, 32F, 62F, 32F),
                PartPose.offset(19F, -208F, 0F));
        PartDefinition left_shin = left_thigh.addOrReplaceChild("left_shin",
                CubeListBuilder.create()
                .texOffs(306, 522).addBox(-14F, -6F, -15F, 28F, 18F, 30F)
                .texOffs(589, 228).addBox(-14F, 10F, -14F, 28F, 52F, 28F)
                .texOffs(0, 321).addBox(-13F, 60F, -13F, 26F, 48F, 26F)
                .texOffs(146, 572).addBox(-14F, 100F, -14F, 28F, 10F, 28F)
                .texOffs(724, 396).addBox(-14F, 108F, -31F, 28F, 12F, 46F)
                .texOffs(618, 572).addBox(-12F, 110F, -35F, 24F, 10F, 5F)
                .texOffs(873, 463).addBox(-14F, 117F, -31F, 28F, 3F, 46F),
                PartPose.offset(0F, 112F, 0F));
        PartDefinition right_thigh = root.addOrReplaceChild("right_thigh",
                CubeListBuilder.create()
                .texOffs(0, 228).addBox(-17F, 0F, -17F, 34F, 58F, 34F)
                .texOffs(430, 131).addBox(-16F, 54F, -16F, 32F, 62F, 32F),
                PartPose.offset(-19F, -208F, 0F));
        PartDefinition right_shin = right_thigh.addOrReplaceChild("right_shin",
                CubeListBuilder.create()
                .texOffs(423, 522).addBox(-14F, -6F, -15F, 28F, 18F, 30F)
                .texOffs(702, 228).addBox(-14F, 10F, -14F, 28F, 52F, 28F)
                .texOffs(105, 321).addBox(-13F, 60F, -13F, 26F, 48F, 26F)
                .texOffs(259, 572).addBox(-14F, 100F, -14F, 28F, 10F, 28F)
                .texOffs(873, 396).addBox(-14F, 108F, -31F, 28F, 12F, 46F)
                .texOffs(677, 572).addBox(-12F, 110F, -35F, 24F, 10F, 5F)
                .texOffs(0, 522).addBox(-14F, 117F, -31F, 28F, 3F, 46F),
                PartPose.offset(0F, 112F, 0F));
        PartDefinition torso = root.addOrReplaceChild("torso",
                CubeListBuilder.create()
                .texOffs(0, 0).addBox(-57F, -60F, -27F, 114F, 76F, 54F)
                .texOffs(0, 131).addBox(-50F, -104F, -25F, 100F, 46F, 50F)
                .texOffs(337, 0).addBox(-56F, -176F, -26F, 112F, 74F, 52F)
                .texOffs(210, 321).addBox(-62F, -186F, -26F, 124F, 22F, 52F)
                .texOffs(0, 572).addBox(-14F, -192F, -15F, 28F, 10F, 30F)
                .texOffs(666, 0).addBox(-16F, -178F, -27F, 32F, 110F, 2F)
                .texOffs(579, 572).addBox(-7F, -176F, -30F, 14F, 14F, 5F)
                .texOffs(117, 572).addBox(-5F, -162F, -29F, 10F, 36F, 4F)
                .texOffs(637, 463).addBox(-7F, -126F, -29F, 14F, 48F, 4F)
                .texOffs(814, 572).addBox(-6F, -78F, -29F, 12F, 8F, 4F)
                .texOffs(973, 572).addBox(-3F, -84F, -28F, 6F, 6F, 3F)
                .texOffs(992, 572).addBox(-3F, -46F, -30F, 6F, 6F, 3F)
                .texOffs(847, 572).addBox(18F, -52F, -30F, 28F, 8F, 3F)
                .texOffs(910, 572).addBox(-46F, -52F, -30F, 28F, 8F, 3F)
                .texOffs(0, 613).addBox(24F, -160F, -28F, 22F, 7F, 2F),
                PartPose.offset(0F, -208F, 0F));
        PartDefinition lapel_l = torso.addOrReplaceChild("lapel_l",
                CubeListBuilder.create()
                .texOffs(674, 463).addBox(0F, 0F, -4F, 24F, 46F, 4F)
                .texOffs(378, 463).addBox(0F, 44F, -4F, 17F, 52F, 4F),
                PartPose.offsetAndRotation(8F, -186F, -26F, 0F, 0F, 0.16F));
        PartDefinition lapel_r = torso.addOrReplaceChild("lapel_r",
                CubeListBuilder.create()
                .texOffs(731, 463).addBox(-24F, 0F, -4F, 24F, 46F, 4F)
                .texOffs(421, 463).addBox(-17F, 44F, -4F, 17F, 52F, 4F),
                PartPose.offsetAndRotation(-8F, -186F, -26F, 0F, 0F, -0.16F));
        PartDefinition collar_l = torso.addOrReplaceChild("collar_l",
                CubeListBuilder.create()
                .texOffs(736, 572).addBox(0F, 0F, -5F, 14F, 9F, 5F),
                PartPose.offsetAndRotation(9F, -190F, -16F, 0F, 0F, 0.5F));
        PartDefinition collar_r = torso.addOrReplaceChild("collar_r",
                CubeListBuilder.create()
                .texOffs(775, 572).addBox(-14F, 0F, -5F, 14F, 9F, 5F),
                PartPose.offsetAndRotation(-9F, -190F, -16F, 0F, 0F, -0.5F));
        PartDefinition neck = torso.addOrReplaceChild("neck",
                CubeListBuilder.create()
                .texOffs(788, 463).addBox(-10F, -22F, -11F, 20F, 28F, 22F),
                PartPose.offset(0F, -176F, 0F));
        PartDefinition head = neck.addOrReplaceChild("head",
                CubeListBuilder.create()
                .texOffs(372, 572).addBox(-11F, -80F, -13F, 22F, 7F, 26F)
                .texOffs(149, 522).addBox(-18F, -73F, -21F, 36F, 7F, 42F)
                .texOffs(0, 463).addBox(-22F, -66F, -25F, 44F, 8F, 50F)
                .texOffs(306, 396).addBox(-24F, -58F, -28F, 48F, 7F, 56F)
                .texOffs(89, 396).addBox(-25F, -51F, -29F, 50F, 7F, 58F)
                .texOffs(563, 321).addBox(-25F, -44F, -29F, 50F, 8F, 58F)
                .texOffs(515, 396).addBox(-24F, -36F, -28F, 48F, 7F, 56F)
                .texOffs(189, 463).addBox(-22F, -29F, -25F, 44F, 7F, 50F)
                .texOffs(464, 463).addBox(-20F, -22F, -23F, 40F, 8F, 46F)
                .texOffs(654, 522).addBox(-16F, -14F, -18F, 32F, 7F, 36F)
                .texOffs(490, 572).addBox(-10F, -7F, -12F, 20F, 7F, 24F),
                PartPose.offset(0F, -22F, 0F));
        PartDefinition left_arm = torso.addOrReplaceChild("left_arm",
                CubeListBuilder.create()
                .texOffs(559, 131).addBox(-16F, -14F, -17F, 32F, 60F, 34F)
                .texOffs(355, 228).addBox(-14F, 44F, -15F, 28F, 52F, 30F)
                .texOffs(780, 321).addBox(-13F, 94F, -14F, 26F, 38F, 28F),
                PartPose.offset(70F, -168F, 0F));
        PartDefinition left_forearm = left_arm.addOrReplaceChild("left_forearm",
                CubeListBuilder.create()
                .texOffs(137, 228).addBox(-13F, -4F, -14F, 26F, 56F, 28F)
                .texOffs(815, 228).addBox(-11F, 50F, -12F, 22F, 52F, 24F)
                .texOffs(791, 522).addBox(-13F, 96F, -14F, 26F, 14F, 28F),
                PartPose.offset(0F, 130F, 0F));
        PartDefinition left_hand = left_forearm.addOrReplaceChild("left_hand",
                CubeListBuilder.create()
                .texOffs(540, 522).addBox(-4F, 0F, -10F, 8F, 24F, 20F)
                .texOffs(469, 572).addBox(-2F, 22F, -10F, 4F, 17F, 4F)
                .texOffs(469, 572).addBox(-2F, 38F, -10F, 4F, 14F, 4F)
                .texOffs(469, 572).addBox(-2F, 22F, -5F, 4F, 17F, 4F)
                .texOffs(469, 572).addBox(-2F, 38F, -5F, 4F, 14F, 4F)
                .texOffs(469, 572).addBox(-2F, 22F, 0F, 4F, 17F, 4F)
                .texOffs(469, 572).addBox(-2F, 38F, 0F, 4F, 14F, 4F)
                .texOffs(469, 572).addBox(-2F, 22F, 5F, 4F, 17F, 4F)
                .texOffs(469, 572).addBox(-2F, 38F, 5F, 4F, 14F, 4F)
                .texOffs(469, 572).addBox(-2F, 4F, -15F, 4F, 13F, 5F)
                .texOffs(469, 572).addBox(-2F, 16F, -15F, 4F, 10F, 4F),
                PartPose.offset(0F, 110F, 0F));
        PartDefinition right_arm = torso.addOrReplaceChild("right_arm",
                CubeListBuilder.create()
                .texOffs(692, 131).addBox(-16F, -14F, -17F, 32F, 60F, 34F)
                .texOffs(472, 228).addBox(-14F, 44F, -15F, 28F, 52F, 30F)
                .texOffs(889, 321).addBox(-13F, 94F, -14F, 26F, 38F, 28F),
                PartPose.offset(-70F, -168F, 0F));
        PartDefinition right_forearm = right_arm.addOrReplaceChild("right_forearm",
                CubeListBuilder.create()
                .texOffs(246, 228).addBox(-13F, -4F, -14F, 26F, 56F, 28F)
                .texOffs(908, 228).addBox(-11F, 50F, -12F, 22F, 52F, 24F)
                .texOffs(900, 522).addBox(-13F, 96F, -14F, 26F, 14F, 28F),
                PartPose.offset(0F, 130F, 0F));
        PartDefinition right_hand = right_forearm.addOrReplaceChild("right_hand",
                CubeListBuilder.create()
                .texOffs(597, 522).addBox(-4F, 0F, -10F, 8F, 24F, 20F)
                .texOffs(469, 572).addBox(-2F, 22F, -10F, 4F, 17F, 4F)
                .texOffs(469, 572).addBox(-2F, 38F, -10F, 4F, 14F, 4F)
                .texOffs(469, 572).addBox(-2F, 22F, -5F, 4F, 17F, 4F)
                .texOffs(469, 572).addBox(-2F, 38F, -5F, 4F, 14F, 4F)
                .texOffs(469, 572).addBox(-2F, 22F, 0F, 4F, 17F, 4F)
                .texOffs(469, 572).addBox(-2F, 38F, 0F, 4F, 14F, 4F)
                .texOffs(469, 572).addBox(-2F, 22F, 5F, 4F, 17F, 4F)
                .texOffs(469, 572).addBox(-2F, 38F, 5F, 4F, 14F, 4F)
                .texOffs(469, 572).addBox(-2F, 4F, -15F, 4F, 13F, 5F)
                .texOffs(469, 572).addBox(-2F, 16F, -15F, 4F, 10F, 4F),
                PartPose.offset(0F, 110F, 0F));
        PartDefinition ten0 = torso.addOrReplaceChild("ten0",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-10F, 0F, -10F, 20F, 44F, 20F),
                PartPose.offset(16F, -150F, 28F));
        PartDefinition ten0_s1 = ten0.addOrReplaceChild("s1",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-9F, 0F, -9F, 18F, 44F, 18F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten0_s2 = ten0_s1.addOrReplaceChild("s2",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-9F, 0F, -9F, 17F, 44F, 17F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten0_s3 = ten0_s2.addOrReplaceChild("s3",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-8F, 0F, -8F, 15F, 44F, 15F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten0_s4 = ten0_s3.addOrReplaceChild("s4",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-7F, 0F, -7F, 13F, 44F, 13F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten0_s5 = ten0_s4.addOrReplaceChild("s5",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-6F, 0F, -6F, 12F, 44F, 12F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten0_s6 = ten0_s5.addOrReplaceChild("s6",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-5F, 0F, -5F, 10F, 44F, 10F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten0_s7 = ten0_s6.addOrReplaceChild("s7",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-4F, 0F, -4F, 8F, 44F, 8F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten1 = torso.addOrReplaceChild("ten1",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-10F, 0F, -10F, 20F, 44F, 20F),
                PartPose.offset(-16F, -150F, 28F));
        PartDefinition ten1_s1 = ten1.addOrReplaceChild("s1",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-9F, 0F, -9F, 18F, 44F, 18F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten1_s2 = ten1_s1.addOrReplaceChild("s2",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-9F, 0F, -9F, 17F, 44F, 17F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten1_s3 = ten1_s2.addOrReplaceChild("s3",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-8F, 0F, -8F, 15F, 44F, 15F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten1_s4 = ten1_s3.addOrReplaceChild("s4",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-7F, 0F, -7F, 13F, 44F, 13F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten1_s5 = ten1_s4.addOrReplaceChild("s5",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-6F, 0F, -6F, 12F, 44F, 12F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten1_s6 = ten1_s5.addOrReplaceChild("s6",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-5F, 0F, -5F, 10F, 44F, 10F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten1_s7 = ten1_s6.addOrReplaceChild("s7",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-4F, 0F, -4F, 8F, 44F, 8F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten2 = torso.addOrReplaceChild("ten2",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-10F, 0F, -10F, 20F, 44F, 20F),
                PartPose.offset(26F, -116F, 28F));
        PartDefinition ten2_s1 = ten2.addOrReplaceChild("s1",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-9F, 0F, -9F, 18F, 44F, 18F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten2_s2 = ten2_s1.addOrReplaceChild("s2",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-9F, 0F, -9F, 17F, 44F, 17F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten2_s3 = ten2_s2.addOrReplaceChild("s3",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-8F, 0F, -8F, 15F, 44F, 15F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten2_s4 = ten2_s3.addOrReplaceChild("s4",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-7F, 0F, -7F, 13F, 44F, 13F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten2_s5 = ten2_s4.addOrReplaceChild("s5",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-6F, 0F, -6F, 12F, 44F, 12F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten2_s6 = ten2_s5.addOrReplaceChild("s6",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-5F, 0F, -5F, 10F, 44F, 10F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten2_s7 = ten2_s6.addOrReplaceChild("s7",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-4F, 0F, -4F, 8F, 44F, 8F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten3 = torso.addOrReplaceChild("ten3",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-10F, 0F, -10F, 20F, 44F, 20F),
                PartPose.offset(-26F, -116F, 28F));
        PartDefinition ten3_s1 = ten3.addOrReplaceChild("s1",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-9F, 0F, -9F, 18F, 44F, 18F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten3_s2 = ten3_s1.addOrReplaceChild("s2",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-9F, 0F, -9F, 17F, 44F, 17F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten3_s3 = ten3_s2.addOrReplaceChild("s3",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-8F, 0F, -8F, 15F, 44F, 15F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten3_s4 = ten3_s3.addOrReplaceChild("s4",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-7F, 0F, -7F, 13F, 44F, 13F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten3_s5 = ten3_s4.addOrReplaceChild("s5",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-6F, 0F, -6F, 12F, 44F, 12F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten3_s6 = ten3_s5.addOrReplaceChild("s6",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-5F, 0F, -5F, 10F, 44F, 10F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten3_s7 = ten3_s6.addOrReplaceChild("s7",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-4F, 0F, -4F, 8F, 44F, 8F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten4 = torso.addOrReplaceChild("ten4",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-10F, 0F, -10F, 20F, 44F, 20F),
                PartPose.offset(36F, -82F, 28F));
        PartDefinition ten4_s1 = ten4.addOrReplaceChild("s1",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-9F, 0F, -9F, 18F, 44F, 18F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten4_s2 = ten4_s1.addOrReplaceChild("s2",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-9F, 0F, -9F, 17F, 44F, 17F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten4_s3 = ten4_s2.addOrReplaceChild("s3",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-8F, 0F, -8F, 15F, 44F, 15F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten4_s4 = ten4_s3.addOrReplaceChild("s4",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-7F, 0F, -7F, 13F, 44F, 13F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten4_s5 = ten4_s4.addOrReplaceChild("s5",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-6F, 0F, -6F, 12F, 44F, 12F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten4_s6 = ten4_s5.addOrReplaceChild("s6",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-5F, 0F, -5F, 10F, 44F, 10F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten4_s7 = ten4_s6.addOrReplaceChild("s7",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-4F, 0F, -4F, 8F, 44F, 8F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten5 = torso.addOrReplaceChild("ten5",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-10F, 0F, -10F, 20F, 44F, 20F),
                PartPose.offset(-36F, -82F, 28F));
        PartDefinition ten5_s1 = ten5.addOrReplaceChild("s1",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-9F, 0F, -9F, 18F, 44F, 18F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten5_s2 = ten5_s1.addOrReplaceChild("s2",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-9F, 0F, -9F, 17F, 44F, 17F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten5_s3 = ten5_s2.addOrReplaceChild("s3",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-8F, 0F, -8F, 15F, 44F, 15F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten5_s4 = ten5_s3.addOrReplaceChild("s4",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-7F, 0F, -7F, 13F, 44F, 13F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten5_s5 = ten5_s4.addOrReplaceChild("s5",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-6F, 0F, -6F, 12F, 44F, 12F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten5_s6 = ten5_s5.addOrReplaceChild("s6",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-5F, 0F, -5F, 10F, 44F, 10F),
                PartPose.offset(0F, 44F, 0F));
        PartDefinition ten5_s7 = ten5_s6.addOrReplaceChild("s7",
                CubeListBuilder.create()
                .texOffs(0, 396).addBox(-4F, 0F, -4F, 8F, 44F, 8F),
                PartPose.offset(0F, 44F, 0F));
        return LayerDefinition.create(mesh, 1024, 1024);
    }

    @Override
    public void setupAnim(SlendermanEntity e, float limbSwing, float limbSwingAmount,
                          float age, float netHeadYaw, float headPitch) {
        float pt = age - e.tickCount;
        int action = e.getAction();
        float lunge = action == 1 ? e.getProgress(pt) : 0F;
        float peek = action == 2 ? e.getProgress(pt) : 0F;
        float tent = e.getTentacles(pt);

        head.yRot = Mth.clamp(netHeadYaw, -75F, 75F) * DEG;
        head.xRot = Mth.clamp(headPitch, -40F, 40F) * DEG + peek * 0.22F + lunge * 0.25F;

        // very light walk: slow hip swing, knees bend a little on the back swing
        float sw = Mth.cos(limbSwing * 0.55F) * 0.38F * limbSwingAmount;
        leftThigh.xRot = sw;
        rightThigh.xRot = -sw;
        leftShin.xRot = Math.max(0F, sw) * 0.8F;
        rightShin.xRot = Math.max(0F, -sw) * 0.8F;
        torso.zRot = Mth.cos(limbSwing * 0.55F) * 0.02F * limbSwingAmount;
        torso.xRot = 0.02F + Mth.sin(age * 0.05F) * 0.008F + peek * 0.32F + lunge * 0.55F;

        // arms hang loosely, long and heavy
        float idle = Mth.sin(age * 0.04F) * 0.02F;
        leftArm.xRot = -sw * 0.30F + idle - lunge * 0.35F;
        rightArm.xRot = sw * 0.30F - idle;
        leftArm.zRot = -0.05F;
        rightArm.zRot = 0.05F;
        leftForearm.xRot = -0.07F - lunge * 0.10F;
        rightForearm.xRot = -0.07F;
        leftHand.xRot = 0.06F;
        rightHand.xRot = 0.06F;

        // sudden reach with the right arm
        if (lunge > 0F) {
            rightArm.xRot = -1.50F * lunge + (1F - lunge) * rightArm.xRot;
            rightArm.zRot = 0.05F * (1F - lunge);
            rightForearm.xRot = -0.25F * lunge;
            rightHand.xRot = -0.35F * lunge;
        }

        // tentacles: unfurl from a tight curl, then writhe slowly
        for (int t = 0; t < 6; t++) {
            int side = (t % 2 == 0) ? 1 : -1;
            int layer = t / 2;
            ModelPart r0 = tentacles[t][0];
            r0.visible = tent > 0.02F;
            r0.xRot = 1.95F - layer * 0.18F + Mth.sin(age * 0.07F + t * 1.9F) * 0.10F;
            r0.yRot = side * (0.30F + layer * 0.30F) + Mth.cos(age * 0.05F + t) * 0.08F;
            r0.zRot = 0F;
            float curl = (1F - tent) * 0.95F;
            int visible = (int) Math.ceil(tent * 8F);
            for (int j = 1; j < 8; j++) {
                ModelPart s = tentacles[t][j];
                s.visible = j < visible;
                float wave = Mth.sin(age * 0.09F + t * 1.7F + j * 0.55F);
                s.xRot = 0.12F + curl + 0.30F * wave;
                s.yRot = 0.22F * Mth.cos(age * 0.07F + t + j * 0.5F);
                s.zRot = 0F;
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack ps, VertexConsumer vc, int light, int overlay,
                               float r, float g, float b, float a) {
        root.render(ps, vc, light, overlay, r, g, b, a);
    }
}
