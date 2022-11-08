package committee.nova.momlove.mixin;

import com.mojang.authlib.GameProfile;
import committee.nova.momlove.MomLove;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayer extends PlayerEntity {
    public MixinServerPlayer(World w, BlockPos p, float f, GameProfile g) {
        super(w, p, f, g);
    }

    @Redirect(method = "restoreFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$RuleKey;)Z"))
    private boolean redirect$restoreFrom(GameRules instance, GameRules.RuleKey<GameRules.BooleanValue> v) {
        return instance.getBoolean(v) || MomLove.getConfig().getUuidData().contains(getUUID());
    }
}
