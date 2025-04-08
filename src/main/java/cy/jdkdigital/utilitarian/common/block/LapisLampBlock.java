package cy.jdkdigital.utilitarian.common.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedstoneLampBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;

public class LapisLampBlock extends RedstoneLampBlock
{
    public LapisLampBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (state.getValue(BlockStateProperties.LIT)) {
            level.getLightEngine().checkBlock(pos);
        }
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        if (!(level instanceof ServerLevel) && state.getValue(BlockStateProperties.LIT)) {
            return 15;
        }
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable(getDescriptionId() + ".tooltip").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.LIGHT_PURPLE));
        tooltipComponents.add(Component.translatable(getDescriptionId() + ".tooltip2").withStyle(ChatFormatting.GREEN));
    }
}
