package cy.jdkdigital.utilitarian.data;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.NoSolicitingModule;
import cy.jdkdigital.utilitarian.module.SnadModule;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class EntityTypeTagProvider extends EntityTypeTagsProvider
{
    public EntityTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        super(output, provider, Utilitarian.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(NoSolicitingModule.RAIDER_BLACKLIST).add(EntityType.PILLAGER);
        tag(NoSolicitingModule.TRADER_BLACKLIST).add(EntityType.WANDERING_TRADER, EntityType.TRADER_LLAMA)
                .addOptional(ResourceLocation.parse("rats:plague_doctor"))
                .addOptional(ResourceLocation.parse("supplementaries:red_merchant"));
        tag(SnadModule.CURSED_GRRASS_BLACKLIST);
        tag(Utilitarian.TRAMPLING_ENTITIES);
        tag(Utilitarian.ALWAYS_PERSIST_WITH_EQUIPMENT);
    }

    @Override
    public String getName() {
        return "Utilitarian Entity Type Tags Provider";
    }
}
