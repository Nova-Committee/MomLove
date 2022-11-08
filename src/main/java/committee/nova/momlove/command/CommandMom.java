package committee.nova.momlove.command;

import com.google.common.collect.ImmutableList;
import committee.nova.momlove.MomLove;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CommandMom extends CommandBase {
    @Override
    public String getName() {
        return "mom";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.momlove.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length > 3 || args.length < 2) throw new WrongUsageException("commands.momlove.usage");
        if (args.length == 2) {
            switch (args[0]) {
                case "love": {
                    final String name = args[1];
                    try {
                        final List<EntityPlayerMP> players = getPlayers(server, sender, name);
                        players.forEach(player -> {
                            final boolean b = MomLove.setLove(player, false);
                            sender.sendMessage(new TextComponentTranslation(b ? "msg.momlove.love.success" : "msg.momlove.love.duplicate", player.getName()));
                        });
                    } catch (Exception e) {
                        sender.sendMessage(new TextComponentTranslation("msg.momlove.love.failure"));
                    }
                    return;
                }
                case "unlove": {
                    final String name = args[1];
                    try {
                        final List<EntityPlayerMP> players = getPlayers(server, sender, name);
                        players.forEach(player -> {
                            final boolean b = MomLove.unLove(player);
                            sender.sendMessage(new TextComponentTranslation(b ? "msg.momlove.unlove.success" : "msg.momlove.unlove.notContained", player.getName()));
                        });
                    } catch (Exception e) {
                        sender.sendMessage(new TextComponentTranslation("msg.momlove.unlove.failure"));
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
                    sender.sendMessage(new TextComponentTranslation(b ? "msg.momlove.keys.add.success" : "msg.momlove.keys.add.duplicate"));
                    return;
                }
                case "del": {
                    final String key = args[2];
                    final boolean b = MomLove.delKey(key);
                    sender.sendMessage(new TextComponentTranslation(b ? "msg.momlove.keys.del.success" : "msg.momlove.keys.del.notContained"));
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
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        switch (args.length) {
            case 1:
                return getListOfStringsMatchingLastWord(args, ImmutableList.of("love", "unlove", "keys"));
            case 2: {
                switch (args[0]) {
                    case "love":
                    case "unlove":
                        return getListOfStringsMatchingLastWord(args, server.getPlayerList().getOnlinePlayerNames());
                    case "keys":
                        return getListOfStringsMatchingLastWord(args, ImmutableList.of("add", "del"));
                    default:
                        return ImmutableList.of();
                }
            }
            default:
                return ImmutableList.of();
        }
    }
}
