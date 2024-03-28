package committee.nova.mods.momlove.core.cmds;

import com.mojang.brigadier.context.CommandContext;
import committee.nova.mods.momlove.init.handler.ConfigHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import committee.nova.mods.momlove.Momlove;

import java.util.Collection;

/**
 * Project: MomLove-fabric
 * Author: cnlimiter
 * Date: 2022/11/1 12:32
 * Description:
 */
public class UnLoveCMd {
    public static int execute(CommandContext<CommandSourceStack> context, Collection<ServerPlayer> players) {
        for (Player player : players) {
            try {
                final var contained = Momlove.unLove(player);
                context.getSource().sendSuccess(() -> Component.translatable(contained ? "momlove.unlove.success" : "momlove.unlove.not_contained", player.getName().getString()), true);
            } catch (Exception e) {
                e.printStackTrace();
                context.getSource().sendFailure(Component.translatable("momlove.unlove.failure", player.getName().getString()));
            }
        }
        ConfigHandler.onChange();
        return 0;
    }
}
