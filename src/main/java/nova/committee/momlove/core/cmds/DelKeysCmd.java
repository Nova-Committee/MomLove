package nova.committee.momlove.core.cmds;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import nova.committee.momlove.Momlove;
import nova.committee.momlove.init.handler.ConfigHandler;

/**
 * Project: MomLove-fabric
 * Author: cnlimiter
 * Date: 2022/11/3 2:20
 * Description:
 */
public class DelKeysCmd {
    public static int execute(CommandContext<CommandSourceStack> context, String keyWord) {
        try {
            final var contained = Momlove.delKey(keyWord);
            context.getSource().sendSuccess(Component.translatable(contained ? "momlove.keys.del.success" : "momlove.keys.del.not_contained"), true);
        } catch (Exception e) {
            e.printStackTrace();
            context.getSource().sendSuccess(Component.translatable("momlove.keys.del.failure"), true);
        }
        ConfigHandler.onChange();
        return 0;
    }

}
