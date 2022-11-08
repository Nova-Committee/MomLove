package committee.nova.momlove.command;

import com.mojang.brigadier.context.CommandContext;
import committee.nova.momlove.MomLove;
import committee.nova.momlove.handler.ConfigHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

/**
 * Project: MomLove-Forge
 * Author: cnlimiter
 * Date: 2022/11/1 12:32
 * Description:
 */
public class SetLoveCMd {
    public static int execute(CommandContext<CommandSource> context, Collection<ServerPlayerEntity> players) {
        for (PlayerEntity player : players) {
            try {
                final boolean notIn = MomLove.setLove(player, false);
                context.getSource().sendSuccess(new TranslationTextComponent(notIn ? "momlove.love.success" : "momlove.love.duplicate", player.getName().getString()), true);
            } catch (Exception e) {
                e.printStackTrace();
                context.getSource().sendFailure(new TranslationTextComponent("momlove.love.failure", player.getName().getString()));
            }
        }
        ConfigHandler.onChange();
        return 0;
    }
}
