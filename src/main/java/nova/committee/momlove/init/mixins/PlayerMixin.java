package nova.committee.momlove.init.mixins;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import nova.committee.momlove.Momlove;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Project: MomLove-fabric
 * Author: cnlimiter
 * Date: 2022/11/8 1:19
 * Description:
 */
@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {
    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Redirect(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean redirect$dropEquipment(GameRules instance, GameRules.Key<GameRules.BooleanValue> v) {
        return instance.getBoolean(v) || Momlove.config.getUuidData().contains(getUUID());
    }

    @Redirect(method = "getExperienceReward", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean redirect$getExperienceReward(GameRules instance, GameRules.Key<GameRules.BooleanValue> v) {
        return instance.getBoolean(v) || Momlove.config.getUuidData().contains(getUUID());
    }
}
