package cy.jdkdigital.utilitarian.data;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.NoSolicitingModule;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class StructureTagProvider extends StructureTagsProvider
{
    public StructureTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        super(output, provider, Utilitarian.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(NoSolicitingModule.RAIDER_OUTPOSTS).add(BuiltinStructures.PILLAGER_OUTPOST);
    }

    @Override
    public String getName() {
        return "Utilitarian Structure Tags Provider";
    }
}
