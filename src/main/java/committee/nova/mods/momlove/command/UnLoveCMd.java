package committee.nova.mods.momlove.command;

import com.mojang.brigadier.context.CommandContext;
import committee.nova.mods.momlove.handler.ConfigHandler;
import committee.nova.mods.momlove.MomLove;
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
public class UnLoveCMd {
    public static int execute(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players) {
        for (Player player : players) {
            try {
                final var contained = MomLove.unLove(player);
                context.getSource().sendSuccess(() -> Component.translatable(contained ? "momlove.unlove.success" : "momlove.unlove.not_contained", player.getName().getString()), true);
            } catch (Exception e) {
                context.getSource().sendFailure(Component.translatable("momlove.unlove.failure", player.getName().getString()));
                e.printStackTrace();
            }
        }
        ConfigHandler.onChange();
        return 0;
    }
}
