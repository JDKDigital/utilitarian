package cy.jdkdigital.utilitarian.mixin.client;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.HudOverflowModule;
import net.minecraft.util.ARGB;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(PlayerTabOverlay.class)
public abstract class MixinPlayerTabOverlay
{
    @Shadow
    @Final
    private Minecraft minecraft;

    private static final Identifier UTILITARIAN$HEART_CONTAINER = Identifier.withDefaultNamespace("hud/heart/container");
    private static final Identifier UTILITARIAN$HEART_FULL = Identifier.fromNamespaceAndPath(Utilitarian.MODID, "hud/heart/full");
    private static final Identifier UTILITARIAN$HEART_HALF = Identifier.fromNamespaceAndPath(Utilitarian.MODID, "hud/heart/half");
    private static final int UTILITARIAN$WHITE = 0xFFFFFFFF;

    @Inject(method = "extractTablistHearts", at = @At("HEAD"), cancellable = true)
    private void utilitarian$recolorTablistHearts(int yo, int left, int right, UUID profileId, GuiGraphicsExtractor graphics, int score, CallbackInfo ci) {
        if (!Config.TAB_LIST_HEARTS_ENABLED.get()) {
            return;
        }
        if (score <= 0) {
            ci.cancel();
            return;
        }

        int heartsToRender = 10;
        int widthPerHeart = Mth.floor(Math.min((float) (right - left - 4) / heartsToRender, 9.0F));

        if (widthPerHeart <= 3) {
            float pct = Mth.clamp(score / 20.0F, 0.0F, 1.0F);
            int color = (int) ((1.0F - pct) * 255.0F) << 16 | (int) (pct * 255.0F) << 8;
            float hearts = score / 2.0F;
            Component hpText = Component.translatable("multiplayer.player.list.hp", hearts);
            Component text = right - this.minecraft.font.width(hpText) >= left
                    ? hpText
                    : Component.literal(Float.toString(hearts));
            graphics.text(this.minecraft.font, text, (right + left - this.minecraft.font.width(text)) / 2, yo, ARGB.opaque(color));
            ci.cancel();
            return;
        }

        int bottomTier = HudOverflowModule.bottomTier(score);
        int pageHalves = HudOverflowModule.currentPageHalves(score);
        int fillColor = HudOverflowModule.heartColor(bottomTier);
        int baseColor = bottomTier > 0 ? HudOverflowModule.heartColor(bottomTier - 1) : UTILITARIAN$WHITE;

        // Containers
        for (int h = 0; h < heartsToRender; h++) {
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, UTILITARIAN$HEART_CONTAINER, left + h * widthPerHeart, yo, 9, 9, baseColor);
        }
        // Base fill once we've reached tier ≥ 1
        if (bottomTier > 0) {
            for (int h = 0; h < heartsToRender; h++) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, UTILITARIAN$HEART_FULL, left + h * widthPerHeart, yo, 9, 9, baseColor);
            }
        }
        // Top fill for the current page
        for (int h = 0; h < heartsToRender; h++) {
            int xo = left + h * widthPerHeart;
            int firstHalf = h * 2 + 1;
            int secondHalf = h * 2 + 2;
            if (secondHalf <= pageHalves) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, UTILITARIAN$HEART_FULL, xo, yo, 9, 9, fillColor);
            } else if (firstHalf == pageHalves) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, UTILITARIAN$HEART_HALF, xo, yo, 9, 9, fillColor);
            }
        }

        ci.cancel();
    }
}
