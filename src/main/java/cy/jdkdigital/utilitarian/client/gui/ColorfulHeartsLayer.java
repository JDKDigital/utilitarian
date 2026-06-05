package cy.jdkdigital.utilitarian.client.gui;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.HudOverflowModule;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class ColorfulHeartsLayer implements GuiLayer
{
    private static final int WHITE = 0xFFFFFFFF;

    private static final Identifier OUR_FULL = id("hud/heart/full");
    private static final Identifier OUR_HALF = id("hud/heart/half");
    private static final Identifier OUR_FULL_BLINKING = id("hud/heart/full_blinking");
    private static final Identifier OUR_HALF_BLINKING = id("hud/heart/half_blinking");
    private static final Identifier OUR_HARDCORE_FULL = id("hud/heart/hardcore_full");
    private static final Identifier OUR_HARDCORE_HALF = id("hud/heart/hardcore_half");
    private static final Identifier OUR_HARDCORE_FULL_BLINKING = id("hud/heart/hardcore_full_blinking");
    private static final Identifier OUR_HARDCORE_HALF_BLINKING = id("hud/heart/hardcore_half_blinking");
    private static final Identifier OUR_ABSORBING_FULL = id("hud/heart/absorbing_full");
    private static final Identifier OUR_ABSORBING_HALF = id("hud/heart/absorbing_half");
    private static final Identifier OUR_ABSORBING_FULL_BLINKING = id("hud/heart/absorbing_full_blinking");
    private static final Identifier OUR_ABSORBING_HALF_BLINKING = id("hud/heart/absorbing_half_blinking");
    private static final Identifier OUR_ABSORBING_HARDCORE_FULL = id("hud/heart/absorbing_hardcore_full");
    private static final Identifier OUR_ABSORBING_HARDCORE_HALF = id("hud/heart/absorbing_hardcore_half");
    private static final Identifier OUR_ABSORBING_HARDCORE_FULL_BLINKING = id("hud/heart/absorbing_hardcore_full_blinking");
    private static final Identifier OUR_ABSORBING_HARDCORE_HALF_BLINKING = id("hud/heart/absorbing_hardcore_half_blinking");

    private static final Identifier TIER_MARK = id("hud/heart/tier_mark");
    private static final int TIER_MARK_COLOR = 0xFF000000; // black

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(Utilitarian.MODID, path);
    }

    // Tracks damage-blink state. Vanilla Gui has these as private fields; since we replaced the layer
    // Gui no longer updates them for us, so we mirror them on the instance.
    private int lastHealth;
    private int displayHealth;
    private long lastHealthTime;
    private int healthBlinkTime;

    @Override
    public void render(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) return;
        if (mc.gameMode == null || !mc.gameMode.canHurtPlayer()) return;
        if (!(mc.getCameraEntity() instanceof Player player)) return;

        Gui gui = mc.gui;
        boolean isHardcore = player.level().getLevelData().isHardcore();
        Gui.HeartType type = heartTypeFor(player);
        boolean normalHearts = type == Gui.HeartType.NORMAL;
        boolean tintAbsorbing = type != Gui.HeartType.WITHERED;

        int currentHealth = Mth.ceil(player.getHealth());
        int absorption = Mth.ceil(player.getAbsorptionAmount());
        float maxHealthAttr = (float) player.getAttributeValue(Attributes.MAX_HEALTH);
        int maxHealthHalves = Math.max(Mth.ceil(maxHealthAttr), currentHealth);
        int containerCount = Math.min(10, Mth.ceil(maxHealthHalves / 2.0f));

        // Damage-blink state machine
        int tickCount = gui.getGuiTicks();
        boolean blink = healthBlinkTime > tickCount && (healthBlinkTime - tickCount) / 3L % 2L == 1L;
        long timeMillis = Util.getMillis();
        if (currentHealth < lastHealth && player.invulnerableTime > 0) {
            lastHealthTime = timeMillis;
            healthBlinkTime = tickCount + 20;
        } else if (currentHealth > lastHealth && player.invulnerableTime > 0) {
            lastHealthTime = timeMillis;
            healthBlinkTime = tickCount + 10;
        }
        if (timeMillis - lastHealthTime > 1000L) {
            displayHealth = currentHealth;
            lastHealthTime = timeMillis;
        }
        lastHealth = currentHealth;
        int oldHealth = displayHealth;

        int regenOffsetCell = -1;
        if (player.hasEffect(MobEffects.REGENERATION)) {
            regenOffsetCell = tickCount % Mth.ceil(maxHealthAttr + 5.0F);
        }

        int xLeft = graphics.guiWidth() / 2 - 91;
        int yLineBase = graphics.guiHeight() - gui.leftHeight;

        gui.leftHeight += 10;

        // Containers (empty heart outlines — keep using vanilla sprite, no tint needed)
        Identifier containerSprite = Gui.HeartType.CONTAINER.getSprite(isHardcore, false, false);
        for (int i = 0; i < containerCount; i++) {
            int yo = (i == regenOffsetCell) ? yLineBase - 2 : yLineBase;
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, containerSprite, xLeft + i * 8, yo, 9, 9, WHITE);
        }

        // Health fill
        drawTieredFill(graphics, xLeft, yLineBase, regenOffsetCell,
                currentHealth, oldHealth, blink,
                normalHearts, false, isHardcore, type);

        // Absorption overlays leftmost cells with the absorbing sprite. No damage-blink for absorption.
        if (absorption > 0) {
            drawTieredFill(graphics, xLeft, yLineBase, regenOffsetCell,
                    absorption, 0, false,
                    tintAbsorbing, true, isHardcore, type);
        }

        // Tier indicator: one mark per tier on the leftmost hearts. Clamped to 10 cells.
        int healthTier = HudOverflowModule.bottomTier(currentHealth);
        int markCount = Math.min(10, healthTier);
        for (int i = 0; i < markCount; i++) {
            int xo = xLeft + i * 8;
            int yo = (i == regenOffsetCell) ? yLineBase - 2 : yLineBase;
            graphics.blitSprite(RenderPipelines.GUI_TEXTURED, TIER_MARK, xo, yo, 9, 9, TIER_MARK_COLOR);
        }
    }

    /** Replicates Gui.HeartType.forPlayer (which is private) plus the NeoForge mod hook. */
    private static Gui.HeartType heartTypeFor(Player player) {
        Gui.HeartType type;
        if (player.hasEffect(MobEffects.POISON)) {
            type = Gui.HeartType.POISIONED;
        } else if (player.hasEffect(MobEffects.WITHER)) {
            type = Gui.HeartType.WITHERED;
        } else if (player.isFullyFrozen()) {
            type = Gui.HeartType.FROZEN;
        } else {
            type = Gui.HeartType.NORMAL;
        }
        return ClientHooks.firePlayerHeartTypeEvent(player, type);
    }

    /**
     * @param tintable  if true, use our grayscale sprites and apply the palette tint. If false, use the vanilla
     *                  poisoned/withered/frozen sprite at full saturation so its distinctive color reads through.
     * @param absorbing if true, this is the absorbing layer (uses absorbing palette + absorbing sprites)
     */
    private static void drawTieredFill(GuiGraphicsExtractor graphics, int xLeft, int yBase, int regenOffsetCell,
                                       int halves, int oldHalves, boolean blink,
                                       boolean tintable, boolean absorbing, boolean hardcore, Gui.HeartType vanillaType) {
        if (halves <= 0) return;
        int bottomTier = HudOverflowModule.bottomTier(halves);
        int pageHalves = HudOverflowModule.currentPageHalves(halves);

        Identifier full, half, fullBlinking, halfBlinking;
        int fillColor;
        if (tintable) {
            full = ourSprite(absorbing, hardcore, false, false);
            half = ourSprite(absorbing, hardcore, true, false);
            fullBlinking = ourSprite(absorbing, hardcore, false, true);
            halfBlinking = ourSprite(absorbing, hardcore, true, true);
            fillColor = absorbing ? HudOverflowModule.absorbingColor(bottomTier) : HudOverflowModule.heartColor(bottomTier);
        } else {
            // Vanilla sprite for poisoned/withered/frozen — preserve original color, no tier tint
            Gui.HeartType spriteType = absorbing && vanillaType == Gui.HeartType.WITHERED ? vanillaType
                    : (absorbing ? Gui.HeartType.ABSORBING : vanillaType);
            full = spriteType.getSprite(hardcore, false, false);
            half = spriteType.getSprite(hardcore, true, false);
            fullBlinking = spriteType.getSprite(hardcore, false, true);
            halfBlinking = spriteType.getSprite(hardcore, true, true);
            fillColor = WHITE;
        }

        // Base layer: full row in the previous tier's color, only when we've crossed into a higher tier
        if (tintable && bottomTier > 0) {
            int baseColor = absorbing ? HudOverflowModule.absorbingColor(bottomTier - 1) : HudOverflowModule.heartColor(bottomTier - 1);
            for (int i = 0; i < 10; i++) {
                int yo = (i == regenOffsetCell) ? yBase - 2 : yBase;
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, full, xLeft + i * 8, yo, 9, 9, baseColor);
            }
        }

        // Blink overlay (damage flash) — only when old/current same tier; otherwise the recolor itself is the cue
        boolean sameTier = oldHalves > 0 && bottomTier == HudOverflowModule.bottomTier(oldHalves);
        if (blink && sameTier) {
            int oldPageHalves = HudOverflowModule.currentPageHalves(oldHalves);
            for (int i = 0; i < 10; i++) {
                int xo = xLeft + i * 8;
                int yo = (i == regenOffsetCell) ? yBase - 2 : yBase;
                int firstHalf = i * 2 + 1;
                int secondHalf = i * 2 + 2;
                if (secondHalf <= oldPageHalves) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, fullBlinking, xo, yo, 9, 9, fillColor);
                } else if (firstHalf == oldPageHalves) {
                    graphics.blitSprite(RenderPipelines.GUI_TEXTURED, halfBlinking, xo, yo, 9, 9, fillColor);
                }
            }
        }

        // Top layer: tier-colored fill for the current page (covers blink within the current range)
        for (int i = 0; i < 10; i++) {
            int xo = xLeft + i * 8;
            int yo = (i == regenOffsetCell) ? yBase - 2 : yBase;
            int firstHalf = i * 2 + 1;
            int secondHalf = i * 2 + 2;
            if (secondHalf <= pageHalves) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, full, xo, yo, 9, 9, fillColor);
            } else if (firstHalf == pageHalves) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, half, xo, yo, 9, 9, fillColor);
            }
        }
    }

    private static Identifier ourSprite(boolean absorbing, boolean hardcore, boolean half, boolean blinking) {
        if (absorbing) {
            if (hardcore) {
                return half ? (blinking ? OUR_ABSORBING_HARDCORE_HALF_BLINKING : OUR_ABSORBING_HARDCORE_HALF)
                        : (blinking ? OUR_ABSORBING_HARDCORE_FULL_BLINKING : OUR_ABSORBING_HARDCORE_FULL);
            }
            return half ? (blinking ? OUR_ABSORBING_HALF_BLINKING : OUR_ABSORBING_HALF)
                    : (blinking ? OUR_ABSORBING_FULL_BLINKING : OUR_ABSORBING_FULL);
        }
        if (hardcore) {
            return half ? (blinking ? OUR_HARDCORE_HALF_BLINKING : OUR_HARDCORE_HALF)
                    : (blinking ? OUR_HARDCORE_FULL_BLINKING : OUR_HARDCORE_FULL);
        }
        return half ? (blinking ? OUR_HALF_BLINKING : OUR_HALF)
                : (blinking ? OUR_FULL_BLINKING : OUR_FULL);
    }
}
