package cy.jdkdigital.utilitarian.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.filefix.access.CompressedNbt;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.nio.file.Path;

/**
 * Ports the spirit of AllTheLeaks' SavedDataMixin to MC 26.1. CompressedNbt.writeFile is the entry
 * point that takes a Dynamic, casts it to CompoundTag, and writes to disk. Vanilla swallows the
 * IOException from the write step with a context-free log; it doesn't catch failures from the cast
 * step (potential encode/serialization issues) but those propagate up with no file context either.
 *
 * Two-stage wrap:
 *   1. WrapOperation on NbtIo.writeCompressed converts any IOException into an unchecked exception
 *      so it escapes vanilla's swallowing catch.
 *   2. WrapMethod on writeFile catches BOTH that rethrown I/O failure and any failure from the
 *      data.cast call, logs the path, and rethrows. Single log line per failure regardless of source.
 */
@Mixin(CompressedNbt.class)
public class MixinCompressedNbt
{
    @Shadow
    @Final
    private static Logger LOGGER;

    @Shadow
    @Final
    private Path path;

    @WrapOperation(
            method = "writeFile",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/nbt/NbtIo;writeCompressed(Lnet/minecraft/nbt/CompoundTag;Ljava/nio/file/Path;)V"
            )
    )
    private void utilitarian$rethrowIO(CompoundTag tag, Path path, Operation<Void> original) {
        try {
            original.call(tag, path);
        } catch (Throwable e) {
            throw e instanceof RuntimeException re ? re : new RuntimeException(e);
        }
    }

    @WrapMethod(method = "writeFile")
    private <T> void utilitarian$logFailure(Dynamic<T> data, Operation<Void> original) {
        try {
            original.call(data);
        } catch (Throwable e) {
            LOGGER.error("Failed to save {}", this.path, e);
            throw e instanceof RuntimeException re ? re : new RuntimeException(e);
        }
    }
}
