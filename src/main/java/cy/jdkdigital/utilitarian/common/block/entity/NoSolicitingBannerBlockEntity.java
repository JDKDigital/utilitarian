package cy.jdkdigital.utilitarian.common.block.entity;

import cy.jdkdigital.utilitarian.module.NoSolicitingModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class NoSolicitingBannerBlockEntity extends BannerBlockEntity
{
    public NoSolicitingBannerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(pPos, pBlockState);
    }

    @Override
    public BlockEntityType<?> getType() {
        return NoSolicitingModule.NO_SOLICITING_BANNER_BLOCK_ENTITY.get();
    }
}
