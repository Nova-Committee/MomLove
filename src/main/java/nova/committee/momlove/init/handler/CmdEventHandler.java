package nova.committee.momlove.init.handler;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import nova.committee.momlove.core.cmds.AddKeysCmd;
import nova.committee.momlove.core.cmds.DelKeysCmd;
import nova.committee.momlove.core.cmds.SetLoveCMd;
import nova.committee.momlove.core.cmds.UnLoveCMd;


/**
 * Project: MomLove-fabric
 * Author: cnlimiter
 * Date: 2022/11/1 12:08
 * Description:
 */
public class CmdEventHandler {
    public static void init() {
        CommandRegistrationCallback.EVENT.register((dispatcher, commandBuildContext, selection) -> {
            dispatcher.register(
                    Commands.literal("mom")
                            .requires(source -> source.hasPermission(2))
                            .then(Commands.literal("love")
                                    .then(Commands.argument("targets", EntityArgument.players())
                                            .executes((commandContext) -> SetLoveCMd.execute(commandContext, EntityArgument.getPlayers(commandContext, "targets")
                                             )))
                            )
                            .then(Commands.literal("unlove")
                                    .then(Commands.argument("targets", EntityArgument.players())
                                            .executes((commandContext) -> UnLoveCMd.execute(commandContext, EntityArgument.getPlayers(commandContext, "targets")
                                            )))
                            )
                            .then(Commands.literal("keys")
                                    .then(Commands.literal("add")
                                            .then(Commands.argument("keywords", StringArgumentType.greedyString())
                                                    .executes((commandContext) -> AddKeysCmd.execute(commandContext, StringArgumentType.getString(commandContext, "keywords")
                                                    ))))
                                    .then(Commands.literal("del")
                                            .then(Commands.argument("keywords", StringArgumentType.greedyString())
                                                    .executes((commandContext) -> DelKeysCmd.execute(commandContext, StringArgumentType.getString(commandContext, "keywords")
                                                    ))))
                            )

            );


        });
    }
}
