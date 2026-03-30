package cy.jdkdigital.utilitarian.common.block;

import com.mojang.serialization.MapCodec;
import cy.jdkdigital.utilitarian.common.block.entity.WellBehavedDropperBlockEntity;
import cy.jdkdigital.utilitarian.module.UtilityBlockModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class WellBehavedDropperBlock extends BaseEntityBlock
{
    public static final MapCodec<WellBehavedDropperBlock> CODEC = simpleCodec(WellBehavedDropperBlock::new);

    public WellBehavedDropperBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.ENABLED, true));
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new WellBehavedDropperBlockEntity(pPos, pState);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.ENABLED);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, UtilityBlockModule.WELL_BEHAVED_DROPPER_BLOCK_ENTITY.get(), WellBehavedDropperBlockEntity::tick);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (!pOldState.is(pState.getBlock())) {
            this.checkPoweredState(pLevel, pPos, pState, Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        this.checkPoweredState(pLevel, pPos, pState, Block.UPDATE_CLIENTS);
    }

    private void checkPoweredState(Level pLevel, BlockPos pPos, BlockState pState, int pFlags) {
        boolean noSignal = !pLevel.hasNeighborSignal(pPos);
        if (noSignal != pState.getValue(BlockStateProperties.ENABLED)) {
            pLevel.setBlock(pPos, pState.setValue(BlockStateProperties.ENABLED, noSignal), pFlags);
        }
    }
}
