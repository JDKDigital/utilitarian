package cy.jdkdigital.utilitarian.common.block;

import com.mojang.serialization.MapCodec;
import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.common.block.entity.RedstoneClockBlockEntity;
import cy.jdkdigital.utilitarian.module.UtilityBlockModule;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RedstoneClockBlock extends BaseEntityBlock
{
    public static final MapCodec<RedstoneClockBlock> CODEC = simpleCodec(RedstoneClockBlock::new);

    public RedstoneClockBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.ENABLED, true).setValue(BlockStateProperties.POWERED, false));
    }

    @Override
    public MapCodec<RedstoneClockBlock> codec() {
        return CODEC;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new RedstoneClockBlockEntity(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : createTickerHelper(pBlockEntityType, UtilityBlockModule.REDSTONE_CLOCK_BLOCK_ENTITY.get(), RedstoneClockBlockEntity::tick);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.ENABLED, BlockStateProperties.POWERED);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (!pOldState.is(pState.getBlock())) {
            this.checkPoweredState(pLevel, pPos, pState, Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        if (!pLevel.isClientSide && pLevel.getBlockEntity(pPos) instanceof RedstoneClockBlockEntity redstoneClockBlockEntity) {
            int newValue = redstoneClockBlockEntity.rate + (pPlayer.isShiftKeyDown() ? 10 : 1);
            if (newValue < Config.REDSTONE_CLOCK_MIN_FREQUENCY.get() || newValue > 100) {
                newValue = Config.REDSTONE_CLOCK_MIN_FREQUENCY.get();
            }
            redstoneClockBlockEntity.rate = newValue;
            pPlayer.displayClientMessage(Component.translatable("block.utilitarian.redstone_clock.message", newValue), true);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        if (!pLevel.isClientSide && pLevel.getBlockEntity(pPos) instanceof RedstoneClockBlockEntity redstoneClockBlockEntity) {
            pPlayer.displayClientMessage(Component.translatable("block.utilitarian.redstone_clock.message", redstoneClockBlockEntity.rate), true);
        }
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        var fromState = pLevel.getBlockState(pFromPos);
        if (fromState.is(Blocks.REDSTONE_WIRE) || fromState.hasProperty(BlockStateProperties.POWER)) return;
        this.checkPoweredState(pLevel, pPos, pState, Block.UPDATE_CLIENTS);
    }

    private void checkPoweredState(Level pLevel, BlockPos pPos, BlockState pState, int pFlags) {
        boolean noSignal = !pLevel.hasNeighborSignal(pPos);
        if (noSignal != pState.getValue(BlockStateProperties.ENABLED)) {
            pLevel.setBlock(pPos, pState.setValue(BlockStateProperties.ENABLED, noSignal), pFlags);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, Item.TooltipContext pContext, List<Component> pTootipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTootipComponents, pTooltipFlag);
        pTootipComponents.add(Component.translatable("block.utilitarian.redstone_clock.description").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public boolean isSignalSource(BlockState pState) {
        return true;
    }

    @Override
    public int getSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
        return pState.getValue(BlockStateProperties.ENABLED) && pState.getValue(BlockStateProperties.POWERED) ? 15 : 0;
    }
}
