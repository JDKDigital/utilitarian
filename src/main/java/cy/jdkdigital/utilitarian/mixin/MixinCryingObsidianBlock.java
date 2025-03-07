package cy.jdkdigital.utilitarian.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.CryingObsidianBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = CryingObsidianBlock.class)
public abstract class MixinCryingObsidianBlock implements IBlockExtension
{
    @Override
    public boolean isPortalFrame(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }
}
