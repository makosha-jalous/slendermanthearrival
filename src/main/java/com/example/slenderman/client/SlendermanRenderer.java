package com.example.slenderman.client;

import com.example.slenderman.SlendermanEntity;
import com.example.slenderman.SlendermanMod;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class SlendermanRenderer extends MobRenderer<SlendermanEntity, SlendermanModel> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(SlendermanMod.ID, "textures/entity/slenderman.png");

    /** The model is built 8x finer than vanilla (high-res texture), so it is scaled down by 8 here. */
    private static final float MODEL_SCALE = 0.125F;

    public SlendermanRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new SlendermanModel(ctx.bakeLayer(SlendermanModel.LAYER)), 0.6F);
    }

    @Override
    protected void scale(SlendermanEntity entity, PoseStack ps, float partialTick) {
        ps.scale(MODEL_SCALE, MODEL_SCALE, MODEL_SCALE);
    }

    @Override
    public ResourceLocation getTextureLocation(SlendermanEntity entity) {
        return TEXTURE;
    }
}
