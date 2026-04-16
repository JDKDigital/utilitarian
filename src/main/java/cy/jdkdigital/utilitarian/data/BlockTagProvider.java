package cy.jdkdigital.utilitarian.data;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.UtilityBlockModule;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import java.util.concurrent.CompletableFuture;

public class BlockTagProvider extends BlockTagsProvider
{
    public BlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, Utilitarian.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Utilitarian.MAGNET_VALID_BLOCKS).addTag(BlockTags.ANVIL).addTag(Tags.Blocks.STORAGE_BLOCKS_IRON).addOptionalTag(BlockTags.create(Identifier.parse("c:storage_blocks/steel")));
        tag(UtilityBlockModule.MUFFLERS).add(UtilityBlockModule.SOUND_MUFFLER.get());
    }

    @Override
    public String getName() {
        return "Utilitarian Block Tags Provider";
    }
}
