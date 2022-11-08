package committee.nova.momlove;

import committee.nova.momlove.command.CommandMom;
import committee.nova.momlove.config.Configuration;
import committee.nova.momlove.handler.ConfigHandler;
import committee.nova.momlove.utils.FileUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

@Mod(modid = MomLove.MODID, useMetadata = true)
public class MomLove {
    public static final Logger LOGGER = LogManager.getLogger();
    private static Path configFolder;
    private static Configuration config;
    public static final String MODID = "momlove";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        configFolder = e.getModConfigurationDirectory().toPath().resolve("momlove");
        FileUtils.checkFolder(configFolder);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void onServerStarted(FMLServerStartedEvent e) {
        config = ConfigHandler.load();
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent e) {
        e.registerServerCommand(new CommandMom());
    }

    @SubscribeEvent
    public void onServerChat(ServerChatEvent e) {
        final String msg = e.getMessage();
        if (MomLove.getConfig().getKeyWordsData().stream().noneMatch(msg::startsWith)) return;
        final EntityPlayer player = e.getPlayer();
        setLove(player, true);
    }

    public static boolean setLove(EntityPlayer player, boolean byKey) {
        final boolean b = MomLove.config.getUuidData().add(EntityPlayer.getUUID(player.getGameProfile()));
        ConfigHandler.onChange();
        if (b) LOGGER.info("Set love for player {}", player.getName());
        if (b)
            player.sendStatusMessage(new TextComponentTranslation(byKey ? "msg.momlove.keys.success" : "msg.momlove.love.success", new TextComponentTranslation("momlove.appellation.you.lower").getFormattedText()), true);
        return b;
    }

    public static boolean unLove(EntityPlayer player) {
        final boolean b = MomLove.getConfig().getUuidData().remove(EntityPlayer.getUUID(player.getGameProfile()));
        ConfigHandler.onChange();
        if (b) LOGGER.info("Unlove player {}", player.getName());
        if (b)
            player.sendStatusMessage(new TextComponentTranslation("msg.momlove.unlove.success", new TextComponentTranslation("momlove.appellation.you.upper").getFormattedText()), true);
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

    public static Path getConfigFolder() {
        return configFolder;
    }

    public static Configuration getConfig() {
        return config;
    }

    public static void setConfig(Configuration config) {
        MomLove.config = config;
    }
}
