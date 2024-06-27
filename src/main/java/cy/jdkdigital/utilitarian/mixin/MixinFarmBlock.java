package cy.jdkdigital.utilitarian.mixin;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.Utilitarian;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

//@Debug(export = true)
@Mixin(value = FarmBlock.class)
public abstract class MixinFarmBlock
{
    @Inject(at = {@At("RETURN")}, method = {"canSurvive"}, cancellable = true)
    public void canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos, CallbackInfoReturnable<Boolean> cir) {
        if (Config.NO_TRAMPLE_ENABLED.get() && !cir.getReturnValue()) {
            BlockState blockstate = pLevel.getBlockState(pPos.above());
            cir.setReturnValue(blockstate.is(Utilitarian.FARMLAND_CANSURVIVE));
        }
    }
}
