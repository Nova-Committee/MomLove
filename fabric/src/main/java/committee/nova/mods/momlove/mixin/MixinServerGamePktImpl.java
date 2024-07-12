package committee.nova.mods.momlove.mixin;

import committee.nova.mods.momlove.callbacks.IEvents;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @Project: MomLove
 * @Author: cnlimiter
 * @CreateTime: 2024/7/13 上午12:29
 * @Description:
 */
@Mixin(value = ServerGamePacketListenerImpl.class, priority = 1001)
public class MixinServerGamePktImpl {
    @Shadow
    public ServerPlayer player;
    @Inject(method = "broadcastChatMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastChatMessage(Lnet/minecraft/network/chat/PlayerChatMessage;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/network/chat/ChatType$Bound;)V", shift = At.Shift.BEFORE), cancellable = true)
    public void mcbot$handleChat(PlayerChatMessage filteredText, CallbackInfo ci) {
        String s1 = filteredText.decoratedContent().getString();
        if (!ci.isCancelled()) IEvents.SERVER_CHAT.invoker().onChat(this.player, s1);
    }
}
