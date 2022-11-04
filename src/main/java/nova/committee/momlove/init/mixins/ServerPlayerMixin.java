package nova.committee.momlove.init.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.Level;
import nova.committee.momlove.init.callbacks.PlayerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Project: MomLove-fabric
 * Author: cnlimiter
 * Date: 2022/11/1 12:08
 * Description:
 */

@Mixin(value = ServerPlayer.class, priority = 1001)
public abstract class ServerPlayerMixin extends Player {
    @Shadow public abstract void initInventoryMenu();

    public ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile, ProfilePublicKey key) {
        super(level, blockPos, f, gameProfile, key);
    }

    @Inject(method = "tick()V", at = @At(value = "HEAD"))
    public void ServerPlayer_tick(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        ServerLevel world = (ServerLevel) player.getCommandSenderWorld();

        PlayerEvents.PLAYER_TICK.invoker().onTick(world, player);
    }

    @Redirect(method = "die(Lnet/minecraft/world/damagesource/DamageSource;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;dropAllDeathLoot(Lnet/minecraft/world/damagesource/DamageSource;)V"))
    public void ServerPlayer_die(ServerPlayer instance, DamageSource source) {

        if(!PlayerEvents.PLAYER_DROP_DEATH.invoker().onDeath(source, instance))
            this.dropAllDeathLoot(source);

    }

    @Inject(method = "changeDimension(Lnet/minecraft/server/level/ServerLevel;)Lnet/minecraft/world/entity/Entity;", at = @At(value = "RETURN"))
    public void ServerPlayer_changeDimension(ServerLevel serverLevel, CallbackInfoReturnable<Boolean> ci) {
        ServerPlayer player = (ServerPlayer) (Object) this;

        PlayerEvents.PLAYER_CHANGE_DIMENSION.invoker().onChangeDimension(serverLevel, player);
    }

}
