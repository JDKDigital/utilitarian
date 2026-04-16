package cy.jdkdigital.utilitarian.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import cy.jdkdigital.utilitarian.Utilitarian;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BannerRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

public class NoSolicitingBannerRenderer extends BannerRenderer
{
    static final SpriteId BANNER_BASE_SPRITE = new SpriteId(Sheets.BANNER_SHEET, Identifier.fromNamespaceAndPath(Utilitarian.MODID, "entity/no_soliciting_banner"));
    private final ModelPart flag;
    private final SpriteGetter sprites;

    public NoSolicitingBannerRenderer(BlockEntityRendererProvider.Context pContext) {
        super(pContext);
        ModelPart modelpart = pContext.bakeLayer(ModelLayers.STANDING_BANNER_FLAG);
        this.flag = modelpart;
        this.sprites = pContext.sprites();
    }

    @Override
    public void submit(BannerRenderState pRenderState, PoseStack pPoseStack, SubmitNodeCollector collector, CameraRenderState cameraState) {
        super.submit(pRenderState, pPoseStack, collector, cameraState);

        pPoseStack.pushPose();
        pPoseStack.mulPose(pRenderState.transformation);
        pPoseStack.scale(0.6666667F, -0.6666667F, -0.6666667F);

        this.flag.xRot = (-0.0125F + 0.01F * Mth.cos(((float) Math.PI * 2F) * pRenderState.phase)) * (float) Math.PI;
        this.flag.y = -32.0F;

        var sprite = sprites.get(BANNER_BASE_SPRITE);
        collector.submitModelPart(this.flag, pPoseStack, RenderTypes.bannerPattern(Sheets.BANNER_SHEET), pRenderState.lightCoords, OverlayTexture.NO_OVERLAY, sprite);

        pPoseStack.popPose();
    }
}
