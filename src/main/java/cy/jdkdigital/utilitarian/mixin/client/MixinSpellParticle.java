package cy.jdkdigital.utilitarian.mixin.client;

import cy.jdkdigital.utilitarian.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpellParticle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SpellParticle.class)
public abstract class MixinSpellParticle extends SingleQuadParticle
{
    protected MixinSpellParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
    }

    @Inject(at = {@At("RETURN")}, method = {"tick"})
    public void alterTransparency(CallbackInfo ci) {
        if (this.alpha > 0 && utilitarian$isFirstPersonPlayer()) {
            this.alpha = Config.POTION_EFFECT_TRANSPARENCY.get().floatValue();
        }
    }

    @Unique
    private boolean utilitarian$isFirstPersonPlayer() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer localplayer = minecraft.player;
        return localplayer != null
                && localplayer.getEyePosition().distanceToSqr(this.x, this.y, this.z) <= 1.3
                && minecraft.options.getCameraType().isFirstPerson();
    }
}
