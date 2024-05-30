package cy.jdkdigital.utilitarian.module;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.common.block.DritBlock;
import cy.jdkdigital.utilitarian.common.block.GrrassBlock;
import cy.jdkdigital.utilitarian.common.block.SnadBlock;
import cy.jdkdigital.utilitarian.common.block.SoulSnadBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.RegistryObject;

public class SnadModule
{
    public static final ResourceKey<DamageType> DRIT_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Utilitarian.MODID, "drit"));
    public static final ResourceKey<DamageType> GRRASS_DAMAGE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Utilitarian.MODID, "grrass"));

    public static RegistryObject<Block> SNAD_BLOCK;
    public static RegistryObject<Block> RED_SNAD_BLOCK;
    public static RegistryObject<Block> SOUL_SNAD_BLOCK;
    public static RegistryObject<Block> DRIT_BLOCK;
    public static RegistryObject<Block> GRRASS_BLOCK;

    public static RegistryObject<Item> SNAD_BLOCK_ITEM;
    public static RegistryObject<Item> RED_SNAD_BLOCK_ITEM;
    public static RegistryObject<Item> SOUL_SNAD_BLOCK_ITEM;
    public static RegistryObject<Item> DRIT_BLOCK_ITEM;
    public static RegistryObject<Item> GRRASS_BLOCK_ITEM;

    public static void register() {
        SNAD_BLOCK = Utilitarian.BLOCKS.register("snad", () -> new SnadBlock(14406560, BlockBehaviour.Properties.copy(Blocks.SAND).randomTicks()));
        RED_SNAD_BLOCK = Utilitarian.BLOCKS.register("red_snad", () -> new SnadBlock(11098145, BlockBehaviour.Properties.copy(Blocks.SAND).randomTicks()));
        SOUL_SNAD_BLOCK = Utilitarian.BLOCKS.register("soul_snad", () -> new SoulSnadBlock(BlockBehaviour.Properties.copy(Blocks.SOUL_SAND).randomTicks()));
        DRIT_BLOCK = Utilitarian.BLOCKS.register("drit", () -> new DritBlock(BlockBehaviour.Properties.copy(Blocks.DIRT).randomTicks()));
        GRRASS_BLOCK = Utilitarian.BLOCKS.register("grrass", () -> new GrrassBlock(BlockBehaviour.Properties.copy(Blocks.GRASS_BLOCK).randomTicks()));

        SNAD_BLOCK_ITEM = Utilitarian.ITEMS.register("snad", () -> new BlockItem(SNAD_BLOCK.get(), new Item.Properties()));
        RED_SNAD_BLOCK_ITEM = Utilitarian.ITEMS.register("red_snad", () -> new BlockItem(RED_SNAD_BLOCK.get(), new Item.Properties()));
        SOUL_SNAD_BLOCK_ITEM = Utilitarian.ITEMS.register("soul_snad", () -> new BlockItem(SOUL_SNAD_BLOCK.get(), new Item.Properties()));
        DRIT_BLOCK_ITEM = Utilitarian.ITEMS.register("drit", () -> new BlockItem(DRIT_BLOCK.get(), new Item.Properties()));
        GRRASS_BLOCK_ITEM = Utilitarian.ITEMS.register("grrass", () -> new BlockItem(GRRASS_BLOCK.get(), new Item.Properties()));
    }
}
