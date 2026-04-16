package cy.jdkdigital.utilitarian.mixin;

import com.google.common.collect.ImmutableMap;
import cy.jdkdigital.utilitarian.Config;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

//@Debug(export = true)
@Mixin(value = ServerAdvancementManager.class)
public class MixinServerAdvancementManager
{
    @Shadow
    private Map<Identifier, AdvancementHolder> advancements = Map.of();

    @Inject(
            at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/AdvancementTree;<init>()V"),
            method = {"apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V"}
    )
    public void utilitarian_removeAdvancements(Map<Identifier, Advancement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler, CallbackInfo ci) {
        if (Config.DISABLE_RECIPE_ADVANCEMENTS.get()) {
            Map<Identifier, AdvancementHolder> newAdvancements = new HashMap<>();
            this.advancements.forEach((identifier, advancementHolder) -> {
                if (!identifier.getPath().startsWith("recipes/") || advancementHolder.value().rewards().recipes().isEmpty()) {
                    newAdvancements.put(identifier, advancementHolder);
                }
            });
            this.advancements = ImmutableMap.copyOf(newAdvancements);
        }
    }
}
