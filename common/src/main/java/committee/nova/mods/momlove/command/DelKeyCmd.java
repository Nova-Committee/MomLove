package committee.nova.mods.momlove.command;

import com.mojang.brigadier.context.CommandContext;
import committee.nova.mods.momlove.CommonClass;
import committee.nova.mods.momlove.handler.ConfigHandler;
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
            final var contained = CommonClass.delKey(keyWord);
            context.getSource().sendSuccess(()->Component.translatable(contained ? "momlove.keys.del.success" : "momlove.keys.del.not_contained"), true);
        } catch (Exception e) {
            context.getSource().sendFailure(Component.translatable("momlove.keys.del.failure"));
            e.printStackTrace();
        }
        ConfigHandler.onChange();
        return 0;
    }
}
