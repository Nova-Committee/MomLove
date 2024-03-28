package committee.nova.mods.momlove.core.cmds;

import com.mojang.brigadier.context.CommandContext;
import committee.nova.mods.momlove.init.handler.ConfigHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import committee.nova.mods.momlove.Momlove;

/**
 * Project: MomLove-fabric
 * Author: cnlimiter
 * Date: 2022/11/3 2:20
 * Description:
 */
public class AddKeysCmd {
    public static int execute(CommandContext<CommandSourceStack> context, String keyWord) {
        try {
            final var notIn = Momlove.addKey(keyWord);
            context.getSource().sendSuccess(() -> Component.translatable(notIn ? "momlove.keys.add.success" : "momlove.keys.add.duplicate"), true);
        } catch (Exception e) {
            e.printStackTrace();
            context.getSource().sendFailure(Component.translatable("momlove.keys.add.failure"));
        }
        ConfigHandler.onChange();
        return 0;
    }
}
