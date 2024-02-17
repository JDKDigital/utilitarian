package cy.jdkdigital.utilitarian.module;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.common.block.AngelBlock;
import cy.jdkdigital.utilitarian.common.item.AngelBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.RegistryObject;

public class AngelBlockModule
{
    public static RegistryObject<Block> ANGEL_BLOCK;
    public static RegistryObject<Item> ANGEL_BLOCK_ITEM;

    public static void register() {
        ANGEL_BLOCK = Utilitarian.BLOCKS.register("angel_block", () -> new AngelBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).noCollission().instabreak().pushReaction(PushReaction.DESTROY)));
        ANGEL_BLOCK_ITEM = Utilitarian.ITEMS.register("angel_block", () -> new AngelBlockItem(ANGEL_BLOCK.get(), new Item.Properties()));
    }
}
