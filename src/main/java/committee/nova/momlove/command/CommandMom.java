package committee.nova.momlove.command;

import com.google.common.collect.ImmutableList;
import committee.nova.momlove.MomLove;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class CommandMom extends CommandBase {
    @Override
    public String getCommandName() {
        return "mom";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "commands.momlove.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 3 || args.length < 2) throw new WrongUsageException("commands.momlove.usage");
        if (args.length == 2) {
            switch (args[0]) {
                case "love": {
                    final String name = args[1];
                    try {
                        final EntityPlayerMP player = getPlayer(sender, name);
                        final boolean b = MomLove.setLove(player, false);
                        sender.addChatMessage(new ChatComponentTranslation(b ? "msg.momlove.love.success" : "msg.momlove.love.duplicate", player.getDisplayName()));
                    } catch (Exception e) {
                        sender.addChatMessage(new ChatComponentTranslation("msg.momlove.love.failure"));
                    }
                    return;
                }
                case "unlove": {
                    final String name = args[1];
                    try {
                        final EntityPlayerMP player = getPlayer(sender, name);
                        final boolean b = MomLove.unLove(player);
                        sender.addChatMessage(new ChatComponentTranslation(b ? "msg.momlove.unlove.success" : "msg.momlove.unlove.notContained", player.getDisplayName()));
                    } catch (Exception e) {
                        sender.addChatMessage(new ChatComponentTranslation("msg.momlove.unlove.failure"));
                    }
                    return;
                }
                default:
                    throw new WrongUsageException("commands.momlove.usage");
            }
        } else {
            if (!args[0].equals("keys")) throw new WrongUsageException("commands.momlove.usage");
            switch (args[1]) {
                case "add": {
                    final String key = args[2];
                    final boolean b = MomLove.addKey(key);
                    sender.addChatMessage(new ChatComponentTranslation(b ? "msg.momlove.keys.add.success" : "msg.momlove.keys.add.duplicate"));
                    return;
                }
                case "del": {
                    final String key = args[2];
                    final boolean b = MomLove.delKey(key);
                    sender.addChatMessage(new ChatComponentTranslation(b ? "msg.momlove.keys.del.success" : "msg.momlove.keys.del.notContained"));
                    return;
                }
                default:
                    throw new WrongUsageException("commands.momlove.usage");
            }
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {
        switch (args.length) {
            case 1:
                return getListOfStringsMatchingLastWord(args, "love", "unlove", "keys");
            case 2: {
                switch (args[0]) {
                    case "love":
                    case "unlove":
                        return getListOfStringsMatchingLastWord(args, MinecraftServer.getServer().getAllUsernames());
                    case "keys":
                        return getListOfStringsMatchingLastWord(args, "add", "del");
                    default:
                        return ImmutableList.of();
                }
            }
            default:
                return ImmutableList.of();
        }
    }
}
