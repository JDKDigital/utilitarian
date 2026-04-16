package cy.jdkdigital.utilitarian.module;

import com.mojang.serialization.Codec;
import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.common.block.NoRaiderBlock;
import cy.jdkdigital.utilitarian.common.block.NoSolicitingBanner;
import cy.jdkdigital.utilitarian.common.block.NoSolicitingWallBanner;
import cy.jdkdigital.utilitarian.common.block.SolicitingCarpet;
import cy.jdkdigital.utilitarian.common.block.entity.NoSolicitingBannerBlockEntity;
import cy.jdkdigital.utilitarian.common.item.RestrainingOrder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class NoSolicitingModule
{
    public static DeferredHolder<Block, Block> NO_SOLICITING_BANNER;
    public static DeferredHolder<Block, Block> NO_SOLICITING_WALL_BANNER;
    public static DeferredHolder<Block, Block> NO_RAIDER_BLOCK;
    public static Map<DyeColor, DeferredHolder<Block, Block>> SOLICITING_CARPET = new HashMap<>();
    public static Map<DyeColor, DeferredHolder<Block, Block>> TRAPPED_SOLICITING_CARPET = new HashMap<>();
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<NoSolicitingBannerBlockEntity>> NO_SOLICITING_BANNER_BLOCK_ENTITY;
    public static DeferredHolder<Item, Item> NO_SOLICITING_BANNER_ITEM;
    public static DeferredHolder<Item, BlockItem> NO_RAIDER_BLOCK_ITEM;
    public static Map<DyeColor, DeferredHolder<Item, BlockItem>> SOLICITING_CARPET_ITEM = new HashMap<>();
    public static Map<DyeColor, DeferredHolder<Item, BlockItem>> TRAPPED_SOLICITING_CARPET_ITEM = new HashMap<>();
    public static DeferredHolder<Item, Item> RESTRAINING_ORDER;
    public static DeferredHolder<PoiType, PoiType> NO_SOLICITING_POI;
    public static DeferredHolder<PoiType, PoiType> SOLICITING_POI;
    public static DeferredHolder<PoiType, PoiType> NO_RAIDER_POI;

    public static TagKey<EntityType<?>> TRADER_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Utilitarian.MODID, "traders"));
    public static TagKey<PoiType> NO_SOLICITING_POI_TAG = TagKey.create(Registries.POINT_OF_INTEREST_TYPE, Identifier.fromNamespaceAndPath(Utilitarian.MODID, "no_soliciting"));
    public static TagKey<PoiType> SOLICITING_POI_TAG = TagKey.create(Registries.POINT_OF_INTEREST_TYPE, Identifier.fromNamespaceAndPath(Utilitarian.MODID, "soliciting"));
    public static TagKey<Block> TRAPPED_SOLICITING_CARPETS = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Utilitarian.MODID, "trapped_soliciting_carpets"));
    public static TagKey<EntityType<?>> RAIDER_BLACKLIST = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(Utilitarian.MODID, "raiders"));
    public static TagKey<Structure> RAIDER_OUTPOSTS = TagKey.create(Registries.STRUCTURE, Identifier.fromNamespaceAndPath(Utilitarian.MODID, "outposts"));

    public static final Supplier<DataComponentType<Unit>> ACTIVE = Utilitarian.DATA_COMPONENT_TYPES.register("active", () -> DataComponentType.<Unit>builder().persistent(Unit.CODEC).networkSynchronized(Unit.STREAM_CODEC).build());

    public static void register() {
        NO_SOLICITING_BANNER = Utilitarian.BLOCKS.registerBlock("no_soliciting_banner", p -> new NoSolicitingBanner(DyeColor.WHITE, p), () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_BANNER));
        NO_SOLICITING_WALL_BANNER = Utilitarian.BLOCKS.registerBlock("no_soliciting_wall_banner", p -> new NoSolicitingWallBanner(DyeColor.WHITE, p), () -> BlockBehaviour.Properties.of().overrideLootTable(NO_SOLICITING_BANNER.get().getLootTable()).overrideDescription(NO_SOLICITING_BANNER.get().getDescriptionId()).mapColor(MapColor.WOOD)
                .forceSolidOn()
                .instrument(NoteBlockInstrument.BASS)
                .noCollision()
                .strength(1.0F)
                .sound(SoundType.WOOD)
                .ignitedByLava());
        NO_SOLICITING_BANNER_BLOCK_ENTITY = Utilitarian.BLOCK_ENTITY.register("no_soliciting_banner", () -> new BlockEntityType<>(NoSolicitingBannerBlockEntity::new, NO_SOLICITING_BANNER.get(), NO_SOLICITING_WALL_BANNER.get()));
        NO_SOLICITING_BANNER_ITEM = Utilitarian.ITEMS.registerItem("no_soliciting_banner", p -> new BannerItem(NO_SOLICITING_BANNER.get(), NO_SOLICITING_WALL_BANNER.get(), p));
        NO_RAIDER_BLOCK = Utilitarian.BLOCKS.registerBlock("no_raider_block", NoRaiderBlock::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL));
        NO_RAIDER_BLOCK_ITEM = Utilitarian.ITEMS.registerSimpleBlockItem(NO_RAIDER_BLOCK);

        for (DyeColor color : DyeColor.values()) {
            SOLICITING_CARPET.put(color, Utilitarian.BLOCKS.registerBlock(color.getSerializedName() + "_soliciting_carpet", SolicitingCarpet::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CARPET)));
            TRAPPED_SOLICITING_CARPET.put(color, Utilitarian.BLOCKS.registerBlock(color.getSerializedName() + "_trapped_soliciting_carpet", SolicitingCarpet::new, () -> BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_CARPET)));

            SOLICITING_CARPET_ITEM.put(color, Utilitarian.ITEMS.registerSimpleBlockItem(SOLICITING_CARPET.get(color)));
            TRAPPED_SOLICITING_CARPET_ITEM.put(color, Utilitarian.ITEMS.registerSimpleBlockItem(TRAPPED_SOLICITING_CARPET.get(color)));
        }

        RESTRAINING_ORDER = Utilitarian.ITEMS.registerItem("restraining_order", p -> new RestrainingOrder(p.stacksTo(1)));

        NO_SOLICITING_POI = Utilitarian.POI_TYPES.register("no_soliciting", () -> {
            Set<BlockState> blockStates = new HashSet<>();
            blockStates.addAll(NO_SOLICITING_BANNER.get().getStateDefinition().getPossibleStates());
            blockStates.addAll(NO_SOLICITING_WALL_BANNER.get().getStateDefinition().getPossibleStates());
            return new PoiType(blockStates, 1, 1);
        });
        SOLICITING_POI = Utilitarian.POI_TYPES.register("soliciting", () -> {
            Set<BlockState> blockStates = new HashSet<>();
            for (DyeColor color : DyeColor.values()) {
                blockStates.addAll(SOLICITING_CARPET.get(color).get().getStateDefinition().getPossibleStates());
                blockStates.addAll(TRAPPED_SOLICITING_CARPET.get(color).get().getStateDefinition().getPossibleStates());
            }
            return new PoiType(blockStates, 1, 1);
        });
        NO_RAIDER_POI = Utilitarian.POI_TYPES.register("no_raider", () -> new PoiType(new HashSet<>(NO_RAIDER_BLOCK.get().getStateDefinition().getPossibleStates()), 1, 1));
    }
}
