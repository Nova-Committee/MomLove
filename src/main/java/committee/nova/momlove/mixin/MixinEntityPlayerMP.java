package committee.nova.momlove.mixin;

import com.mojang.authlib.GameProfile;
import committee.nova.momlove.MomLove;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityPlayerMP.class)
public abstract class MixinEntityPlayerMP extends EntityPlayer {
    public MixinEntityPlayerMP(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }

    @Redirect(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getGameRuleBooleanValue(Ljava/lang/String;)Z"))
    private boolean redirect$onDeath(GameRules instance, String name) {
        return instance.getGameRuleBooleanValue(name) || MomLove.getConfig().getUuidData().contains(getUniqueID());
    }
}
