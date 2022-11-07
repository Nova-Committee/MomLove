package committee.nova.momlove.command;

import com.mojang.brigadier.context.CommandContext;
import committee.nova.momlove.MomLove;
import committee.nova.momlove.handler.ConfigHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

/**
 * Project: MomLove-Forge
 * Author: cnlimiter
 * Date: 2022/11/3 2:20
 * Description:
 */
public class DelKeyCmd {
    public static int execute(CommandContext<CommandSourceStack> context, String keyWord) {
        try {
            final var contained = MomLove.delKey(keyWord);
            context.getSource().sendSuccess(Component.translatable(contained ? "momlove.keys.del.success" : "momlove.keys.del.not_contained"), true);
        } catch (Exception e) {
            e.printStackTrace();
            context.getSource().sendSuccess(Component.translatable("momlove.keys.failure"), true);
        }
        ConfigHandler.onChange();
        return 0;
    }
}
