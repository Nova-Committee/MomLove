package committee.nova.momlove.mixin;

import committee.nova.momlove.MomLove;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase {
    public MixinEntityPlayer(World worldIn) {
        super(worldIn);
    }

    @Redirect(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getGameRuleBooleanValue(Ljava/lang/String;)Z"))
    private boolean redirect$onDeath(GameRules instance, String name) {
        return instance.getGameRuleBooleanValue(name) || MomLove.getConfig().getUuidData().contains(getUniqueID());
    }

    @Redirect(method = "getExperiencePoints", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getGameRuleBooleanValue(Ljava/lang/String;)Z"))
    private boolean redirect$getExperiencePoints(GameRules instance, String name) {
        return instance.getGameRuleBooleanValue(name) || MomLove.getConfig().getUuidData().contains(getUniqueID());
    }

    @Redirect(method = "clonePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getGameRuleBooleanValue(Ljava/lang/String;)Z"))
    private boolean redirect$clonePlayer(GameRules instance, String name) {
        return instance.getGameRuleBooleanValue(name) || MomLove.getConfig().getUuidData().contains(getUniqueID());
    }
}
