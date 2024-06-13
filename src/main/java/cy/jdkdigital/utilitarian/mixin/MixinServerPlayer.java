package cy.jdkdigital.utilitarian.mixin;

import cy.jdkdigital.utilitarian.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Debug(export = true)
@Mixin(value = ServerPlayer.class)
public class MixinServerPlayer
{
    @Inject(
            at = @At(value = "INVOKE",
            target = "Ljava/util/List;isEmpty()Z"),
            method = {"startSleepInBed"},
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    public void utilitarian_startSleepInBed(BlockPos pAt, CallbackInfoReturnable<Boolean> ci, Optional<BlockPos> optAt, Player.BedSleepingProblem ret, Direction direction, double d0, double d1, Vec3 vec3, List<Monster> list) {
        if (Config.SERVER.BETTER_SLEEP_ENABLED.get()) {
            list.clear();
        }
    }

    @Inject(at = @At(value = "RETURN"), method = {"bedInRange"}, cancellable = true)
    public void utilitarian_bedInRange(BlockPos pos, Direction dir, CallbackInfoReturnable<Boolean> ci) {
        if (Config.SERVER.BETTER_SLEEP_ENABLED.get() && dir != null) {
            ci.setReturnValue(true);
        }
    }
}
