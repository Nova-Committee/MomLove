package nova.committee.momlove.core.cmds;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import nova.committee.momlove.Momlove;
import nova.committee.momlove.init.handler.ConfigHandler;

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
                context.getSource().sendSuccess(new TranslatableComponent(contained ? "momlove.unlove.success" : "momlove.unlove.not_contained", player.getName().getString()), true);
            } catch (Exception e) {
                e.printStackTrace();
                context.getSource().sendFailure(new TranslatableComponent("momlove.unlove.failure", player.getName().getString()));
            }
        }
        ConfigHandler.onChange();
        return 0;
    }
}
