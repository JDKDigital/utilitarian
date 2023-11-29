package cy.jdkdigital.utilitarian.common.block;

import cy.jdkdigital.utilitarian.common.block.entity.NoSolicitingBannerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class NoSolicitingBanner extends BannerBlock
{
    public NoSolicitingBanner(DyeColor pColor, Properties pProperties) {
        super(pColor, pProperties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new NoSolicitingBannerBlockEntity(pPos, pState);
    }
}
