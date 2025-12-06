package cy.jdkdigital.utilitarian.data;

import cy.jdkdigital.utilitarian.Utilitarian;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
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
    }

    @Override
    public String getName() {
        return "Utilitarian Block Tags Provider";
    }
}
