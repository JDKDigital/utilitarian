package cy.jdkdigital.utilitarian.data;

import cy.jdkdigital.utilitarian.module.UtilityItemModule;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class DataMapProvider extends net.neoforged.neoforge.common.data.DataMapProvider
{
    /**
     * Create a new provider.
     *
     * @param packOutput     the output location
     * @param lookupProvider a {@linkplain CompletableFuture} supplying the registries
     */
    protected DataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(net.minecraft.core.HolderLookup.Provider provider) {
        final var fuels = builder(NeoForgeDataMaps.FURNACE_FUELS);

        fuels.add(UtilityItemModule.TINY_COAL.getKey(), new FurnaceFuel(200), false);
        fuels.add(UtilityItemModule.TINY_CHARCOAL.getKey(), new FurnaceFuel(200), false);
    }
}
