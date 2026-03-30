package cy.jdkdigital.utilitarian.event;

import cy.jdkdigital.utilitarian.Config;
import cy.jdkdigital.utilitarian.Utilitarian;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.AccessibilityOnboardingScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = Utilitarian.MODID, value = Dist.CLIENT)
public class ClientEvents
{
    private static boolean hasDinged = false;
    @SubscribeEvent
    public static void onLoad(ScreenEvent.Init.Post event) {
        if (!hasDinged && (event.getScreen() instanceof AccessibilityOnboardingScreen || event.getScreen() instanceof TitleScreen)) {
            if (!ModList.get().isLoaded("ding") && Config.DING_DONG_ENABLED.get()) {
                ResourceLocation soundName = ResourceLocation.parse(Config.DING_DONG_SOUND.get());

                SoundEvent sound =  BuiltInRegistries.SOUND_EVENT.get(soundName);

                Minecraft.getInstance().getSoundManager().play(new SimpleSoundInstance(sound == null ? soundName : sound.getLocation(), SoundSource.MASTER, 1.0f, 1.0f, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D, true));
                hasDinged = true;
            }
        }
    }
}
