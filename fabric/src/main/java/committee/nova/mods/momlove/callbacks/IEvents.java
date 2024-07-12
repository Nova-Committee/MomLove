package committee.nova.mods.momlove.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

/**
 * @Project: MomLove
 * @Author: cnlimiter
 * @CreateTime: 2024/7/13 上午12:28
 * @Description:
 */
public interface IEvents {
    public static final Event<ServerChat> SERVER_CHAT = EventFactory.createArrayBacked(ServerChat.class, callbacks -> (player, message) -> {
        for (ServerChat callback : callbacks) {
            callback.onChat(player, message);
        }
    });

    @FunctionalInterface
    public interface ServerChat {
        void onChat(ServerPlayer player, String message);
    }
}
