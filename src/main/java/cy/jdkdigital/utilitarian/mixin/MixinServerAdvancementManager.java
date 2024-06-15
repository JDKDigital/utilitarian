package cy.jdkdigital.utilitarian.mixin;

import com.google.gson.JsonElement;
import cy.jdkdigital.utilitarian.Config;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(value = ServerAdvancementManager.class)
public class MixinServerAdvancementManager
{
//    @Inject(
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/advancements/AdvancementTree;<init>()V"),
//            method = {"apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V"},
//            locals = LocalCapture.CAPTURE_FAILSOFT
//    )
//    public void utilitarian_removeAdvancements(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler, CallbackInfo ci, Map<ResourceLocation, Advancement.Builder> builders) {
//        if (Config.DISABLE_RECIPE_ADVANCEMENTS.get()) {
//            var recipeTrigger = ResourceLocation.withDefaultNamespace("recipe_unlocked");
//            var it = builders.entrySet().iterator();
//            while (it.hasNext()) {
//                var builderEntry = it.next();
//                if (builderEntry.getKey().getPath().startsWith("recipes/")) {
//                    builderEntry.getValue().getCriteria().forEach((s, criterion) -> {
//                        if (criterion.getTrigger().getCriterion().equals(recipeTrigger)) {
//                            it.remove();
//                        }
//                    });
//                }
//            }
//        }
//    }
}
