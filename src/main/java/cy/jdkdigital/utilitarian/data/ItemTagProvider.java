package cy.jdkdigital.utilitarian.data;

import cy.jdkdigital.utilitarian.Utilitarian;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ItemTagsProvider;

import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends ItemTagsProvider
{
    public ItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> future) {
        super(output, future, Utilitarian.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Utilitarian.EQUIPMENT_DESPAWN_BLACKLIST)
                .add(Items.LEATHER_HELMET)
                .add(Items.LEATHER_CHESTPLATE)
                .add(Items.LEATHER_LEGGINGS)
                .add(Items.LEATHER_BOOTS)
                .add(Items.STONE_SWORD)
                .add(Items.FISHING_ROD)
                .add(Items.IRON_SWORD)
                .add(Items.IRON_SHOVEL)
                .add(Items.IRON_AXE)
                .add(Items.GOLDEN_HELMET)
                .add(Items.GOLDEN_CHESTPLATE)
                .add(Items.GOLDEN_LEGGINGS)
                .add(Items.GOLDEN_BOOTS)
                .add(Items.GOLDEN_AXE)
                .add(Items.GOLDEN_SWORD)
                .add(Items.CROSSBOW)
                .add(Items.SHIELD)
                .add(Items.BOW);
    }

    @Override
    public String getName() {
        return "Utilitarian Item Tags Provider";
    }
}
