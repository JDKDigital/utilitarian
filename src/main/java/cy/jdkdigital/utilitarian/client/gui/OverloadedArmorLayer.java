package cy.jdkdigital.utilitarian.client.gui;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.HudOverflowModule;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class OverloadedArmorLayer implements GuiLayer
{
    private static final int WHITE = 0xFFFFFFFF;
    private static final Identifier ARMOR_EMPTY = Identifier.withDefaultNamespace("hud/armor_empty");
    private static final Identifier ARMOR_HALF = Identifier.fromNamespaceAndPath(Utilitarian.MODID, "hud/armor_half");
    private static final Identifier ARMOR_FULL = Identifier.fromNamespaceAndPath(Utilitarian.MODID, "hud/armor_full");

    @Override
    public void render(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options.hideGui) return;
        if (mc.gameMode == null || !mc.gameMode.canHurtPlayer()) return;
        if (!(mc.getCameraEntity() instanceof Player player)) return;

        int armor = player.getArmorValue();
        if (armor <= 0) return;

        Gui gui = mc.gui;
        int xLeft = graphics.guiWidth() / 2 - 91;
        int yLineArmor = graphics.guiHeight() - gui.leftHeight;

        gui.leftHeight += 10;

        int bottomTier = HudOverflowModule.bottomTier(armor);
        int pageHalves = HudOverflowModule.currentPageHalves(armor);

        // Empty cells — only visible at armor < 20 (where the row isn't full)
        for (int i = 0; i < 10; i++) {
            if (i * 2 + 1 >= pageHalves && bottomTier == 0) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_EMPTY, xLeft + i * 8, yLineArmor, 9, 9, WHITE);
            }
        }

        // Base layer: full row tinted with the previous tier's color once we cross into tier ≥ 1
        if (bottomTier > 0) {
            int baseColor = HudOverflowModule.armorColor(bottomTier - 1);
            for (int i = 0; i < 10; i++) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_FULL, xLeft + i * 8, yLineArmor, 9, 9, baseColor);
            }
        }

        // Top layer: the current page's fill in the current tier color
        int fillColor = HudOverflowModule.armorColor(bottomTier);
        for (int i = 0; i < 10; i++) {
            int xo = xLeft + i * 8;
            int firstHalf = i * 2 + 1;
            int secondHalf = i * 2 + 2;
            if (secondHalf <= pageHalves) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_FULL, xo, yLineArmor, 9, 9, fillColor);
            } else if (firstHalf == pageHalves) {
                graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_HALF, xo, yLineArmor, 9, 9, fillColor);
            }
        }
    }
}
