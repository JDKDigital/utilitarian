package cy.jdkdigital.utilitarian.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.resources.RegistryOps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.SavedDataStorage;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.nio.file.Path;

/**
 * Sibling of {@link MixinCompressedNbt}. 26.1's SavedDataStorage.tryWrite uses NeoForge's
 * IOUtilities.writeNbtCompressed and also swallows the IOException with only a log line.
 * We wrap it, log the file, and rethrow unchecked so the failure surfaces.
 */
@Mixin(SavedDataStorage.class)
public class MixinSavedDataStorage
{
    @Shadow
    @Final
    private static Logger LOGGER;

    @WrapOperation(
            method = "tryWrite",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/common/IOUtilities;writeNbtCompressed(Lnet/minecraft/nbt/CompoundTag;Ljava/nio/file/Path;)V"
            )
    )
    private void utilitarian$logAndRethrowOnSaveFailure(CompoundTag tag, Path path, Operation<Void> original) {
        try {
            original.call(tag, path);
        } catch (Throwable e) {
            LOGGER.error("File {} not saved", path.getFileName(), e);
            throw e instanceof RuntimeException re ? re : new RuntimeException(e);
        }
    }

    /**
     * Wraps {@code encodeUnchecked} so codec failures (the {@code getOrThrow} call inside) name the
     * offending SavedData type instead of bubbling up a context-free serialization error.
     */
    @WrapMethod(method = "encodeUnchecked")
    private CompoundTag utilitarian$logEncodeFailure(SavedDataType<?> type, SavedData data, RegistryOps<?> ops, Operation<CompoundTag> original) {
        try {
            return original.call(type, data, ops);
        } catch (Throwable e) {
            LOGGER.error("Failed to encode SavedData {}", type.id(), e);
            throw e instanceof RuntimeException re ? re : new RuntimeException(e);
        }
    }
}
