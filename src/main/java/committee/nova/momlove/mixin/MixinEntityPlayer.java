package committee.nova.momlove.mixin;

import com.mojang.authlib.GameProfile;
import committee.nova.momlove.MomLove;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.minecraft.entity.player.EntityPlayer.getUUID;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase {
    @Shadow
    public abstract GameProfile getGameProfile();

    public MixinEntityPlayer(World worldIn) {
        super(worldIn);
    }

    @Redirect(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Ljava/lang/String;)Z"))
    private boolean redirect$onDeath(GameRules instance, String name) {
        return instance.getBoolean(name) || MomLove.getConfig().getUuidData().contains(getUUID(getGameProfile()));
    }

    @Redirect(method = "getExperiencePoints", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Ljava/lang/String;)Z"))
    private boolean redirect$getExperiencePoints(GameRules instance, String name) {
        return instance.getBoolean(name) || MomLove.getConfig().getUuidData().contains(getUUID(getGameProfile()));
    }
}
