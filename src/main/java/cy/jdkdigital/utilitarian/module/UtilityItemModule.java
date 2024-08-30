package cy.jdkdigital.utilitarian.module;

import com.mojang.serialization.Codec;
import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.common.item.TrowelItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class UtilityItemModule
{
    public static final Supplier<? extends DataComponentType<? super Boolean>> TROWEL_STATE = Utilitarian.DATA_COMPONENT_TYPES.register("extended", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    public static final TagKey<Item> TROWEL_BLACKLIST = ItemTags.create(ResourceLocation.fromNamespaceAndPath(Utilitarian.MODID, "trowel_blacklist"));

    public static DeferredHolder<Item, Item> TROWEL;

    public static void register() {
        TROWEL = Utilitarian.ITEMS.register("trowel", () -> new TrowelItem(new Item.Properties().stacksTo(1)));
    }
}
