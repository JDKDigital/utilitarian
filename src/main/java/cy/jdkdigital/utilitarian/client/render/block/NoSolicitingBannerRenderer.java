package cy.jdkdigital.utilitarian.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import cy.jdkdigital.utilitarian.Utilitarian;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;

public class NoSolicitingBannerRenderer extends BannerRenderer
{
    static final Material BANNER_BASE = new Material(Sheets.BANNER_SHEET, ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "entity/no_soliciting_banner"));
    private final ModelPart flag;

    public NoSolicitingBannerRenderer(BlockEntityRendererProvider.Context pContext) {
        super(pContext);
        ModelPart modelpart = pContext.bakeLayer(ModelLayers.BANNER);
        this.flag = modelpart.getChild("flag");
    }

    @Override
    public void render(BannerBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        super.render(pBlockEntity, pPartialTick, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);

        pPoseStack.pushPose();
        long i;
        if (pBlockEntity.getLevel() == null) {
            i = 0L;
            pPoseStack.translate(0.5F, 0.5F, 0.5F);
        } else {
            i = pBlockEntity.getLevel().getGameTime();
            BlockState blockstate = pBlockEntity.getBlockState();
            if (blockstate.getBlock() instanceof BannerBlock) {
                pPoseStack.translate(0.5F, 0.5F, 0.5F);
                float f1 = -RotationSegment.convertToDegrees(blockstate.getValue(BannerBlock.ROTATION));
                pPoseStack.mulPose(Axis.YP.rotationDegrees(f1));
            } else {
                pPoseStack.translate(0.5F, -0.16666667F, 0.5F);
                float f3 = -blockstate.getValue(WallBannerBlock.FACING).toYRot();
                pPoseStack.mulPose(Axis.YP.rotationDegrees(f3));
                pPoseStack.translate(0.0F, -0.3125F, -0.4375F);
            }
        }

        pPoseStack.pushPose();
        pPoseStack.scale(0.6666667F, -0.6666667F, -0.6666667F);
        BlockPos blockpos = pBlockEntity.getBlockPos();
        float f2 = ((float)Math.floorMod(blockpos.getX() * 7L + blockpos.getY() * 9L + blockpos.getZ() * 13L + i, 100L) + pPartialTick) / 100.0F;
        this.flag.xRot = (-0.0125F + 0.01F * Mth.cos(((float)Math.PI * 2F) * f2)) * (float)Math.PI;
        this.flag.y = -32.0F;
        this.flag.render(pPoseStack, BANNER_BASE.buffer(pBuffer, RenderType::entityNoOutline), pPackedLight, pPackedOverlay, 1);
        pPoseStack.popPose();
        pPoseStack.popPose();
    }
}
