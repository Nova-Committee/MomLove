package committee.nova.mods.momlove.mixin;

import committee.nova.mods.momlove.MomLove;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {
    protected MixinPlayer(EntityType<? extends LivingEntity> t, Level l) {
        super(t, l);
    }

    @Redirect(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean redirect$dropEquipment(GameRules instance, GameRules.Key<GameRules.BooleanValue> v) {
        return MomLove.getConfig().getUuidData().contains(getUUID()) || instance.getBoolean(v) ;
    }

    @Redirect(method = "getExperienceReward", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"))
    private boolean redirect$getExperienceReward(GameRules instance, GameRules.Key<GameRules.BooleanValue> v) {
        return MomLove.getConfig().getUuidData().contains(getUUID()) || instance.getBoolean(v);
    }
}
