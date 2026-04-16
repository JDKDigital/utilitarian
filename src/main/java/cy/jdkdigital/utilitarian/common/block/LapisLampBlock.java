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
    boolean isInverted = false;

    public LapisLampBlock(Properties properties, boolean isInverted) {
        super(properties);
        this.isInverted = isInverted;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (isLit(state)) {
            level.getLightEngine().checkBlock(pos);
        }
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        if (!(level instanceof ServerLevel)) {
            return isLit(state) ? 15 : 0;
        }
        return 0;
    }

    // TODO MC 26.1: appendHoverText removed from Block

    private boolean isLit(BlockState state) {
        return (state.getValue(BlockStateProperties.LIT) && !isInverted) || (!state.getValue(BlockStateProperties.LIT) && isInverted);
    }
}
