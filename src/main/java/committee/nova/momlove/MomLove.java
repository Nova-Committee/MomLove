package committee.nova.momlove;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.logging.LogUtils;
import committee.nova.momlove.command.AddKeyCmd;
import committee.nova.momlove.command.DelKeyCmd;
import committee.nova.momlove.command.SetLoveCMd;
import committee.nova.momlove.command.UnLoveCMd;
import committee.nova.momlove.config.Configuration;
import committee.nova.momlove.handler.ConfigHandler;
import committee.nova.momlove.utils.FileUtils;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.NetworkConstants;
import org.slf4j.Logger;

import java.nio.file.Path;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MomLove.MODID)
public class MomLove {
    public static final String MODID = "momlove";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static Path configFolder;
    private static Configuration config;

    public MomLove() {
        configFolder = FMLPaths.CONFIGDIR.get().resolve("momlove");
        FileUtils.checkFolder(configFolder);
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (n, s) -> true));
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static Path getConfigFolder() {
        return configFolder;
    }

    public static Configuration getConfig() {
        return config;
    }

    public static void setConfig(Configuration config) {
        MomLove.config = config;
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent e) {
        config = ConfigHandler.load();
    }

    public static boolean setLove(Player player, boolean byKey) {
        final var b = MomLove.config.getUuidData().add(player.getUUID());
        ConfigHandler.onChange();
        if (b) LOGGER.info("Set love for player {}", player.getName().getString());
        if (b)
            player.displayClientMessage(new TranslatableComponent(byKey ? "momlove.keys.success" : "momlove.love.success", new TranslatableComponent("momlove.appellation.you.lower").getString()), true);
        return b;
    }

    public static boolean unLove(Player player) {
        final var b = MomLove.getConfig().getUuidData().remove(player.getUUID());
        ConfigHandler.onChange();
        if (b) LOGGER.info("Unlove player {}", player.getName().getString());
        if (b)
            player.displayClientMessage(new TranslatableComponent("momlove.unlove.success", new TranslatableComponent("momlove.appellation.you.upper").getString()), true);
        return b;
    }

    public static boolean addKey(String keyWord) {
        final var b = MomLove.getConfig().getKeyWordsData().add(keyWord);
        ConfigHandler.onChange();
        if (b) LOGGER.info("Add keyword {}", keyWord);
        return b;
    }

    public static boolean delKey(String keyWord) {
        final var b = MomLove.getConfig().getKeyWordsData().remove(keyWord);
        ConfigHandler.onChange();
        if (b) LOGGER.info("Remove keyword {}", keyWord);
        return b;
    }

    @SubscribeEvent
    public void onServerChat(ServerChatEvent e) {
        final var msg = e.getMessage();
        if (MomLove.getConfig().getKeyWordsData().stream().noneMatch(msg::startsWith)) return;
        final var player = e.getPlayer();
        setLove(player, true);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent e) {
        e.getDispatcher().register(
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
}
