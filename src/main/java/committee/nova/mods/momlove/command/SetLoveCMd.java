package committee.nova.mods.momlove.command;

import com.mojang.brigadier.context.CommandContext;
import committee.nova.mods.momlove.MomLove;
import committee.nova.mods.momlove.handler.ConfigHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;

/**
 * Project: MomLove-Forge
 * Author: cnlimiter
 * Date: 2022/11/1 12:32
 * Description:
 */
public class SetLoveCMd {
    public static int execute(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players) {
        for (Player player : players) {
            try {
                final var notIn = MomLove.setLove(player, false);
                context.getSource().sendSuccess(()-> Component.translatable(notIn ? "momlove.love.success" : "momlove.love.duplicate", player.getName().getString()), true);
            } catch (Exception e) {
                context.getSource().sendFailure(Component.translatable("momlove.love.failure", player.getName().getString()));
                e.printStackTrace();
            }
        }
        ConfigHandler.onChange();
        return 0;
    }
}
