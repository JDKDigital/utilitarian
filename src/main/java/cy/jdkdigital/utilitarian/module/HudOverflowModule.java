package cy.jdkdigital.utilitarian.module;

public final class HudOverflowModule
{
    private static final int[] HEART_PALETTE = {
            0xFFFF3030, // tier 0 — red (vanilla appearance)
            0xFFFF7020, // tier 1 — orange
            0xFFFFB020, // tier 2 — amber
            0xFFFFE020, // tier 3 — yellow
            0xFFB0E020, // tier 4 — chartreuse
            0xFF40D040, // tier 5 — green
            0xFF20D0A0, // tier 6 — teal
            0xFF20C0E0, // tier 7 — cyan
            0xFF40A0FF, // tier 8 — blue
            0xFF7060FF, // tier 9 — indigo
            0xFFB060FF, // tier 10 — violet
            0xFFE060B0, // tier 11 — pink
    };

    private static final int[] ABSORBING_PALETTE = {
            0xFFFFC020, // tier 0 — yellow (vanilla absorbing appearance)
            0xFFFFE060, // tier 1 — light yellow
            0xFFE0FF60, // tier 2 — lime
            0xFF60E0A0, // tier 3 — mint
            0xFF40D0E0, // tier 4 — cyan
            0xFF40A0FF, // tier 5 — blue
            0xFF8060FF, // tier 6 — purple
            0xFFD040FF, // tier 7 — magenta
            0xFFFF40C0, // tier 8 — hot pink
            0xFFFF6080, // tier 9 — coral
            0xFFFF8040, // tier 10 — orange
            0xFFFF4040, // tier 11 — red
    };

    private static final int[] ARMOR_PALETTE = {
            0xFFE0E0E0, // tier 0 — silver (vanilla appearance)
            0xFFFF8040, // tier 1 — orange
            0xFFFFB020, // tier 2 — amber
            0xFFFFE020, // tier 3 — yellow
            0xFFB0E020, // tier 4 — chartreuse
            0xFF40D040, // tier 5 — green
            0xFF20D0A0, // tier 6 — teal
            0xFF40C0FF, // tier 7 — blue
            0xFF7060FF, // tier 8 — indigo
            0xFFB060FF, // tier 9 — violet
            0xFFE060B0, // tier 10 — pink
            0xFFFF6080, // tier 11 — coral
    };

    private HudOverflowModule() {}

    public static int heartColor(int tier) {
        return HEART_PALETTE[Math.floorMod(tier, HEART_PALETTE.length)];
    }

    public static int absorbingColor(int tier) {
        return ABSORBING_PALETTE[Math.floorMod(tier, ABSORBING_PALETTE.length)];
    }

    public static int armorColor(int tier) {
        return ARMOR_PALETTE[Math.floorMod(tier, ARMOR_PALETTE.length)];
    }

    /** Returns the "page" tier for a value expressed in half-units. Page 0 covers halves 1-20. */
    public static int bottomTier(int halves) {
        return Math.max(0, (halves - 1) / 20);
    }

    /** Halves filling the current page, in range [0, 20]. */
    public static int currentPageHalves(int halves) {
        if (halves <= 0) return 0;
        return halves - bottomTier(halves) * 20;
    }
}
