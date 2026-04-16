package cy.jdkdigital.utilitarian.module;

import com.mojang.serialization.Codec;
import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.common.item.SlimeBucketItem;
import cy.jdkdigital.utilitarian.common.item.TrowelItem;
import cy.jdkdigital.utilitarian.common.item.UnnameTagItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Supplier;

public class UtilityItemModule
{
    public static final Supplier<? extends DataComponentType<? super Boolean>> TROWEL_STATE = Utilitarian.DATA_COMPONENT_TYPES.register("extended", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    public static final Supplier<? extends DataComponentType<? super Boolean>> SLIME_STATE = Utilitarian.DATA_COMPONENT_TYPES.register("slimed", () -> DataComponentType.<Boolean>builder().persistent(Codec.BOOL).networkSynchronized(ByteBufCodecs.BOOL).build());
    public static final TagKey<Item> TROWEL_BLACKLIST = ItemTags.create(Identifier.fromNamespaceAndPath(Utilitarian.MODID, "trowel_blacklist"));

    public static DeferredHolder<Item, Item> TROWEL;
    public static DeferredHolder<Item, Item> TINY_COAL;
    public static DeferredHolder<Item, Item> TINY_CHARCOAL;
    public static DeferredHolder<Item, Item> UNNAME_TAG;
    public static DeferredHolder<Item, Item> SLIME_BUCKET;
//    public static DeferredHolder<Item, Item> YANKING_ROPE;
    // TODO angel ring
    //    No weird cost (xp power etc, it’s not a jet pack and the price should be in the creation)
    //    Speed should be based on the players speed same way XU2 did it, can you run real fast? Well you can fly real fast too!
    //    I would hope for something visual, like wings or (idea as I’m typing) a gold halo above the players head could be real fucking cool and easier to do than wings

    public static void register() {
        TROWEL = Utilitarian.ITEMS.registerItem("trowel", TrowelItem::new, () -> new Item.Properties().stacksTo(1));
        TINY_COAL = Utilitarian.ITEMS.registerItem("tiny_coal", Item::new);
        TINY_CHARCOAL = Utilitarian.ITEMS.registerItem("tiny_charcoal", Item::new);
        UNNAME_TAG = Utilitarian.ITEMS.registerItem("unname_tag", UnnameTagItem::new);
        SLIME_BUCKET = Utilitarian.ITEMS.registerItem("slime_bucket", SlimeBucketItem::new, () -> new Item.Properties().stacksTo(1).component(UtilityItemModule.SLIME_STATE.get(), false));
//        YANKING_ROPE = Utilitarian.ITEMS.register("yanking_rope", () -> new YankingRopeItem(new Item.Properties().component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)));
    }
}
