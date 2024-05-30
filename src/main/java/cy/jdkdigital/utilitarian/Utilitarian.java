package cy.jdkdigital.utilitarian;

import com.mojang.logging.LogUtils;
import cy.jdkdigital.utilitarian.event.EventHandler;
import cy.jdkdigital.utilitarian.module.UtilityBlockModule;
import cy.jdkdigital.utilitarian.module.NoSolicitingModule;
import cy.jdkdigital.utilitarian.module.SnadModule;
import cy.jdkdigital.utilitarian.module.TPSMeterModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Utilitarian.MODID)
public class Utilitarian
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "utilitarian";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, MODID);

    public static final TagKey<Item> BLACKLISTED_SEEDS = ItemTags.create(new ResourceLocation(MODID, "hoe_planting_blacklist"));

    public Utilitarian() {
        MinecraftForge.EVENT_BUS.register(EventHandler.class);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        BLOCK_ENTITY.register(modEventBus);
        ITEMS.register(modEventBus);
        POI_TYPES.register(modEventBus);

        NoSolicitingModule.register();
        UtilityBlockModule.register();
        TPSMeterModule.register();
        SnadModule.register();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
    }
}
