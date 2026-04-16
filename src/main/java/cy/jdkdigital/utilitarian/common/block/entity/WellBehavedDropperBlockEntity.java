package cy.jdkdigital.utilitarian.common.block.entity;

import cy.jdkdigital.utilitarian.module.UtilityBlockModule;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gamerules.GameRules;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import java.util.function.Supplier;

public class WellBehavedDropperBlockEntity extends BlockEntity
{
    boolean hasChanged = true;
    int counter = 20;
    public ItemStacksResourceHandler inventoryHandler = new ItemStacksResourceHandler(9) {
        @Override
        public boolean isValid(int slot, ItemResource resource) {
            return super.isValid(slot, resource) && getBlockState().getOptionalValue(BlockStateProperties.ENABLED).orElse(true);
        }

        @Override
        protected void onContentsChanged(int slot, ItemStack oldStack) {
            hasChanged = true;
        }
    };

    public WellBehavedDropperBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(UtilityBlockModule.WELL_BEHAVED_DROPPER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    private static void popResource(Level pLevel, Supplier<ItemEntity> pItemEntitySupplier) {
        if (pLevel instanceof ServerLevel serverLevel && serverLevel.getGameRules().get(GameRules.BLOCK_DROPS)) {
            ItemEntity itementity = pItemEntitySupplier.get();
            itementity.setDefaultPickUpDelay();
            pLevel.addFreshEntity(itementity);
        }
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, WellBehavedDropperBlockEntity blockEntity) {
        if (blockState.getOptionalValue(BlockStateProperties.ENABLED).orElse(true) && (blockEntity.hasChanged || --blockEntity.counter <= 0 && blockEntity.level != null)) {
            for (int i = 0; i < blockEntity.inventoryHandler.size(); i++) {
                var resource = blockEntity.inventoryHandler.getResource(i);
                if (!resource.isEmpty()) {
                    int amount = blockEntity.inventoryHandler.getAmountAsInt(i);
                    var stack = resource.toStack(amount);
                    var p = blockEntity.getBlockPos();
                    popResource(level, () -> new ItemEntity(blockEntity.level, p.getX() + .5d, p.getY()-0.51d, p.getZ() + .5d, stack, 0, -0.1, 0));
                    blockEntity.inventoryHandler.set(i, ItemResource.EMPTY, 0);
                }
            }
            blockEntity.counter = 21;
            blockEntity.hasChanged = false;
        }
    }
}
