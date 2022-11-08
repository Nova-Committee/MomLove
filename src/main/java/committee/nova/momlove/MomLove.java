package committee.nova.momlove;

import com.mojang.brigadier.arguments.StringArgumentType;
import committee.nova.momlove.command.AddKeyCmd;
import committee.nova.momlove.command.DelKeyCmd;
import committee.nova.momlove.command.SetLoveCMd;
import committee.nova.momlove.command.UnLoveCMd;
import committee.nova.momlove.config.Configuration;
import committee.nova.momlove.handler.ConfigHandler;
import committee.nova.momlove.utils.FileUtils;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MomLove.MODID)
public class MomLove {
    public static final String MODID = "momlove";
    public static final Logger LOGGER = LogManager.getLogger();
    private static Path configFolder;
    private static Configuration config;

    public MomLove() {
        configFolder = FMLPaths.CONFIGDIR.get().resolve("momlove");
        FileUtils.checkFolder(configFolder);
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (remote, isServer) -> true));
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
    public void onServerStarted(FMLServerStartedEvent e) {
        config = ConfigHandler.load();
    }

    public static boolean setLove(PlayerEntity player, boolean byKey) {
        final boolean b = MomLove.config.getUuidData().add(player.getUUID());
        ConfigHandler.onChange();
        if (b) LOGGER.info("Set love for player {}", player.getName().getString());
        if (b)
            player.displayClientMessage(new TranslationTextComponent(byKey ? "momlove.keys.success" : "momlove.love.success", new TranslationTextComponent("momlove.appellation.you.lower").getString()), true);
        return b;
    }

    public static boolean unLove(PlayerEntity player) {
        final boolean b = MomLove.getConfig().getUuidData().remove(player.getUUID());
        ConfigHandler.onChange();
        if (b) LOGGER.info("Unlove player {}", player.getName().getString());
        if (b)
            player.displayClientMessage(new TranslationTextComponent("momlove.unlove.success", new TranslationTextComponent("momlove.appellation.you.upper").getString()), true);
        return b;
    }

    public static boolean addKey(String keyWord) {
        final boolean b = MomLove.getConfig().getKeyWordsData().add(keyWord);
        ConfigHandler.onChange();
        if (b) LOGGER.info("Add keyword {}", keyWord);
        return b;
    }

    public static boolean delKey(String keyWord) {
        final boolean b = MomLove.getConfig().getKeyWordsData().remove(keyWord);
        ConfigHandler.onChange();
        if (b) LOGGER.info("Remove keyword {}", keyWord);
        return b;
    }

    @SubscribeEvent
    public void onServerChat(ServerChatEvent e) {
        final String msg = e.getMessage();
        if (MomLove.getConfig().getKeyWordsData().stream().noneMatch(msg::startsWith)) return;
        final PlayerEntity player = e.getPlayer();
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
