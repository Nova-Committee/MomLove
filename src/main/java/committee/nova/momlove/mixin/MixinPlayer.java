package committee.nova.momlove.mixin;

import committee.nova.momlove.MomLove;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayer extends LivingEntity {
    protected MixinPlayer(EntityType<? extends LivingEntity> t, World l) {
        super(t, l);
    }

    @Redirect(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$RuleKey;)Z"))
    private boolean redirect$dropEquipment(GameRules instance, GameRules.RuleKey<GameRules.BooleanValue> v) {
        return instance.getBoolean(v) || MomLove.getConfig().getUuidData().contains(getUUID());
    }

    @Redirect(method = "getExperienceReward", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/GameRules;getBoolean(Lnet/minecraft/world/GameRules$RuleKey;)Z"))
    private boolean redirect$getExperienceReward(GameRules instance, GameRules.RuleKey<GameRules.BooleanValue> v) {
        return instance.getBoolean(v) || MomLove.getConfig().getUuidData().contains(getUUID());
    }
}
