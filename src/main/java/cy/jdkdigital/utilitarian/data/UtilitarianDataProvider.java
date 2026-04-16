package cy.jdkdigital.utilitarian.data;

import cy.jdkdigital.utilitarian.Utilitarian;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = Utilitarian.MODID)
public class UtilitarianDataProvider
{
    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();

        event.addProvider(new LanguageProvider(output, "en_us"));

        event.addProvider(new ModelProvider(output));

        event.addProvider(new LootDataProvider(output, List.of(new LootTableProvider.SubProviderEntry(LootDataProvider.LootProvider::new, LootContextParamSets.BLOCK)), provider));
        event.addProvider(new RecipeProvider(output, provider));
//        event.addProvider(new FeatureProvider(output));
        event.addProvider(new DataMapProvider(output, provider));

        event.addProvider(new BlockTagProvider(output, provider));
        event.addProvider(new ItemTagProvider(output, provider));
        event.addProvider(new EntityTypeTagProvider(output, provider));
        event.addProvider(new StructureTagProvider(output, provider));
    }
}
