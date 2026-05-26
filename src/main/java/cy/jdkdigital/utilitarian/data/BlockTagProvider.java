package cy.jdkdigital.utilitarian.data;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.SnadModule;
import cy.jdkdigital.utilitarian.module.TPSMeterModule;
import cy.jdkdigital.utilitarian.module.UtilityBlockModule;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends BlockTagsProvider
{
    public BlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        super(output, provider, Utilitarian.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Utilitarian.MAGNET_VALID_BLOCKS).addTag(BlockTags.ANVIL).addTag(Tags.Blocks.STORAGE_BLOCKS_IRON).addOptionalTag(ResourceLocation.parse("c:storage_blocks/steel"));
        tag(UtilityBlockModule.MUFFLERS).add(UtilityBlockModule.SOUND_MUFFLER.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(UtilityBlockModule.FLUID_HOPPER_BLOCK.get())
                .add(UtilityBlockModule.REDSTONE_CLOCK_BLOCK.get())
                .add(UtilityBlockModule.WELL_BEHAVED_DROPPER.get())
                .add(UtilityBlockModule.MAGNET.get())
                .add(TPSMeterModule.TPS_METER.get());
        tag(BlockTags.MINEABLE_WITH_AXE)
                .addTag(Tags.Blocks.SKULLS);
        tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(SnadModule.CURSED_GRRASS_BLOCK.get())
                .add(SnadModule.DRIT_BLOCK.get())
                .add(SnadModule.GRRASS_BLOCK.get())
                .add(SnadModule.SNAD_BLOCK.get())
                .add(SnadModule.RED_SNAD_BLOCK.get())
                .add(SnadModule.SOUL_SNAD_BLOCK.get());
    }

    @Override
    public String getName() {
        return "Utilitarian Block Tags Provider";
    }
}
