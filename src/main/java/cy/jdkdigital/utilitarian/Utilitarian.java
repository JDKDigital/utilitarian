package cy.jdkdigital.utilitarian;

import com.mojang.logging.LogUtils;
import cy.jdkdigital.utilitarian.module.NoSolicitingModule;
import cy.jdkdigital.utilitarian.module.SnadModule;
import cy.jdkdigital.utilitarian.module.TPSMeterModule;
import cy.jdkdigital.utilitarian.module.UtilityBlockModule;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
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
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Utilitarian.MODID)
public class Utilitarian
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "utilitarian";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, MODID);
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, MODID);
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, MODID);

    public static final TagKey<Item> BLACKLISTED_SEEDS = ItemTags.create(ResourceLocation.fromNamespaceAndPath(MODID, "hoe_planting_blacklist"));
    public static final TagKey<Block> FARMLAND_CANSURVIVE = BlockTags.create(ResourceLocation.fromNamespaceAndPath(MODID, "farmland_cansurvive"));
    public static final TagKey<EntityType<?>> TRAMPLING_ENTITIES = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(MODID, "trampling_entities"));

    public Utilitarian(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
        BLOCK_ENTITY.register(modEventBus);
        ITEMS.register(modEventBus);
        POI_TYPES.register(modEventBus);
        DATA_COMPONENT_TYPES.register(modEventBus);

        NoSolicitingModule.register();
        UtilityBlockModule.register();
        TPSMeterModule.register();
        SnadModule.register();

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
    }
}
