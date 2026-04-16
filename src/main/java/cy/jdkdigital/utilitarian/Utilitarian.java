package cy.jdkdigital.utilitarian;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import cy.jdkdigital.utilitarian.module.*;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Utilitarian.MODID)
public class Utilitarian
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "utilitarian";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, MODID);
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, MODID);
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MODID);
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, MODID);

    public static final TagKey<Item> BLACKLISTED_SEEDS = ItemTags.create(Identifier.fromNamespaceAndPath(MODID, "hoe_planting_blacklist"));
    public static final TagKey<Item> NITWIT_CONVERT = ItemTags.create(Identifier.fromNamespaceAndPath(MODID, "nitwit_convert"));
    public static final TagKey<Item> EQUIPMENT_DESPAWN_BLACKLIST = ItemTags.create(Identifier.fromNamespaceAndPath(MODID, "equipment_despawn_blacklist"));
    public static final TagKey<Block> FARMLAND_CAN_SURVIVE = BlockTags.create(Identifier.fromNamespaceAndPath(MODID, "farmland_cansurvive"));
    public static final TagKey<Block> MAGNET_VALID_BLOCKS = BlockTags.create(Identifier.fromNamespaceAndPath(MODID, "magnet_valid_blocks"));
    public static final TagKey<EntityType<?>> TRAMPLING_ENTITIES = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MODID, "trampling_entities"));
    public static final TagKey<EntityType<?>> ALWAYS_PERSIST_WITH_EQUIPMENT = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MODID, "always_persist_with_equipment"));

    public static final Supplier<AttachmentType<List<String>>> MUFFLER_BLOCK_LIST = ATTACHMENT_TYPES.register(
            "mufflers", () -> AttachmentType.<List<String>>builder(() -> new ArrayList<>()).serialize(Codec.STRING.listOf().fieldOf("value")).build()
    );

    public Utilitarian(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
        BLOCK_ENTITY.register(modEventBus);
        ITEMS.register(modEventBus);
        ENTITIES.register(modEventBus);
        POI_TYPES.register(modEventBus);
        DATA_COMPONENT_TYPES.register(modEventBus);
        ATTACHMENT_TYPES.register(modEventBus);

        ATTACHMENT_TYPES.addAlias(Identifier.fromNamespaceAndPath(MODID, "sound_mufflers"), Identifier.fromNamespaceAndPath(MODID, "mufflers"));

        NoSolicitingModule.register();
        UtilityBlockModule.register();
        UtilityItemModule.register();
        UtilityEntityModule.register();
        TPSMeterModule.register();
        SnadModule.register();

        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
    }
}
