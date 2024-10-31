package cy.jdkdigital.utilitarian.common.block.entity;

import cy.jdkdigital.utilitarian.module.UtilityBlockModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.function.Supplier;

public class WellBehavedDropperBlockEntity extends BlockEntity
{
    boolean hasChanged = true;
    int counter = 20;
    public ItemStackHandler inventoryHandler = new ItemStackHandler(9) {
        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return super.isItemValid(slot, stack) && getBlockState().getOptionalValue(BlockStateProperties.ENABLED).orElse(true);
        }

        @Override
        protected void onContentsChanged(int slot) {
            hasChanged = true;
            super.onContentsChanged(slot);
        }
    };

    public WellBehavedDropperBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(UtilityBlockModule.WELL_BEHAVED_DROPPER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    private static void popResource(Level pLevel, Supplier<ItemEntity> pItemEntitySupplier) {
        if (!pLevel.isClientSide && pLevel.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && !pLevel.restoringBlockSnapshots) {
            ItemEntity itementity = pItemEntitySupplier.get();
            itementity.setDefaultPickUpDelay();
            pLevel.addFreshEntity(itementity);
        }
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, WellBehavedDropperBlockEntity blockEntity) {
        if (blockState.getOptionalValue(BlockStateProperties.ENABLED).orElse(true) && (blockEntity.hasChanged || --blockEntity.counter <= 0 && blockEntity.level != null)) {
            for (int i = 0; i < blockEntity.inventoryHandler.getSlots(); i++) {
                var stack = blockEntity.inventoryHandler.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    var p = blockEntity.getBlockPos();
                    popResource(level, () -> new ItemEntity(blockEntity.level, p.getX() + .5d, p.getY()-0.51d, p.getZ() + .5d, stack, 0, -0.1, 0));
                    blockEntity.inventoryHandler.setStackInSlot(i, ItemStack.EMPTY);
                }
            }
            blockEntity.counter = 21;
            blockEntity.hasChanged = false;
        }
    }
}
