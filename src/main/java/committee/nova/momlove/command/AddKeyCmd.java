package committee.nova.momlove.command;

import com.mojang.brigadier.context.CommandContext;
import committee.nova.momlove.MomLove;
import committee.nova.momlove.handler.ConfigHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Project: MomLove-Forge
 * Author: cnlimiter
 * Date: 2022/11/3 2:20
 * Description:
 */
public class AddKeyCmd {
    public static int execute(CommandContext<CommandSource> context, String keyWord) {
        try {
            final boolean notIn = MomLove.addKey(keyWord);
            context.getSource().sendSuccess(new TranslationTextComponent(notIn ? "momlove.keys.add.success" : "momlove.keys.add.duplicate"), true);
        } catch (Exception e) {
            e.printStackTrace();
            context.getSource().sendSuccess(new TranslationTextComponent("momlove.keys.add.failure"), true);
        }
        ConfigHandler.onChange();
        return 0;
    }
}
