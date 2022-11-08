package nova.committee.momlove.init.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import nova.committee.momlove.Momlove;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Project: MomLove-fabric
 * Author: cnlimiter
 * Date: 2022/11/1 12:08
 * Description:
 */

@Mixin(value = ServerPlayer.class, priority = 1001)
public abstract class ServerPlayerMixin extends Player {
    public ServerPlayerMixin(Level level, BlockPos blockPos, float f, GameProfile gameProfile, ProfilePublicKey key) {
        super(level, blockPos, f, gameProfile, key);
    }

    @Redirect(method = "restoreFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean redirect$restoreFrom(GameRules instance, GameRules.Key<GameRules.BooleanValue> v) {
        return instance.getBoolean(v) || Momlove.config.getUuidData().contains(getUUID());
    }

}
