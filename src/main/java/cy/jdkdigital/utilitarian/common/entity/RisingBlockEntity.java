package cy.jdkdigital.utilitarian.common.entity;

import cy.jdkdigital.utilitarian.Utilitarian;
import cy.jdkdigital.utilitarian.module.UtilityEntityModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class RisingBlockEntity extends FallingBlockEntity
{
    public RisingBlockEntity(EntityType<? extends FallingBlockEntity> entityType, Level level) {
        super(entityType, level);
    }

    private RisingBlockEntity(Level level, double x, double y, double z, BlockState state) {
        this(UtilityEntityModule.RISING_BLOCK.get(), level);
        this.blockState = state;
        this.blocksBuilding = true;
        this.setPos(x, y, z);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = x;
        this.yo = y;
        this.zo = z;
        this.setStartPos(this.blockPosition());
    }

    public static RisingBlockEntity rise(Level level, BlockPos pos, BlockState blockState) {
        RisingBlockEntity risingBlockEntity = new RisingBlockEntity(
                level,
                (double)pos.getX() + 0.5,
                pos.getY(),
                (double)pos.getZ() + 0.5,
                blockState.hasProperty(BlockStateProperties.WATERLOGGED) ? blockState.setValue(BlockStateProperties.WATERLOGGED, Boolean.FALSE) : blockState
        );
        level.setBlock(pos, blockState.getFluidState().createLegacyBlock(), 3);
        level.addFreshEntity(risingBlockEntity);
        return risingBlockEntity;
    }

    @Override
    protected double getDefaultGravity() {
        return -0.04;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().getBlockState(blockPosition().above()).isAir()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1f, 0f, 1f));
            this.setPos(this.position().x, blockPosition().getY(), this.position().z);
        }
    }
}
