package committee.nova.mods.momlove;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import committee.nova.mods.momlove.command.AddKeyCmd;
import committee.nova.mods.momlove.command.DelKeyCmd;
import committee.nova.mods.momlove.command.SetLoveCMd;
import committee.nova.mods.momlove.command.UnLoveCMd;
import committee.nova.mods.momlove.config.Configuration;
import committee.nova.mods.momlove.handler.ConfigHandler;
import committee.nova.mods.momlove.utils.FileUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

import java.nio.file.Path;

import static committee.nova.mods.momlove.Constants.LOG;

public class CommonClass {
    public static Path configFolder;
    public static Configuration config;

    public static void init(Path configDir) {
        configFolder = configDir.resolve("momlove");
        FileUtils.checkFolder(configFolder);
    }

    public static void onServerStarted(MinecraftServer server) {
        config = ConfigHandler.load();
    }

    public static void onServerStopping(MinecraftServer server) {
        ConfigHandler.save(config);
    }

    public static void onServerChat(Player player, String msg){
        if (config.getKeyWordsData().stream().noneMatch(msg::startsWith)) return;
        setLove(player, true);
    }

    public static void registerCmds(CommandDispatcher<CommandSourceStack> dispatcher){
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
                                        .then(Commands.argument("keyword", StringArgumentType.greedyString())
                                                .executes((commandContext) -> AddKeyCmd.execute(commandContext, StringArgumentType.getString(commandContext, "keyword")
                                                ))))
                                .then(Commands.literal("del")
                                        .then(Commands.argument("keyword", StringArgumentType.greedyString())
                                                .executes((commandContext) -> DelKeyCmd.execute(commandContext, StringArgumentType.getString(commandContext, "keyword")
                                                ))))
                        )
        );
    }


    public static boolean setLove(Player player, boolean byKey) {
        final var b = config.getUuidData().add(player.getUUID());
        ConfigHandler.onChange();
        if (b) LOG.info("Set love for player {}", player.getName().getString());
        if (b)
            player.displayClientMessage(Component.translatable(byKey ? "momlove.keys.success" : "momlove.love.success", Component.translatable("momlove.appellation.you.lower").getString()), true);
        return b;
    }

    public static boolean unLove(Player player) {
        final var b = config.getUuidData().remove(player.getUUID());
        ConfigHandler.onChange();
        if (b) LOG.info("Unlove player {}", player.getName().getString());
        if (b)
            player.displayClientMessage(Component.translatable("momlove.unlove.success", Component.translatable("momlove.appellation.you.upper").getString()), true);
        return b;
    }

    public static boolean addKey(String keyWord) {
        final var b = config.getKeyWordsData().add(keyWord);
        ConfigHandler.onChange();
        if (b) LOG.info("Add keyword {}", keyWord);
        return b;
    }

    public static boolean delKey(String keyWord) {
        final var b = config.getKeyWordsData().remove(keyWord);
        ConfigHandler.onChange();
        if (b) LOG.info("Remove keyword {}", keyWord);
        return b;
    }
}