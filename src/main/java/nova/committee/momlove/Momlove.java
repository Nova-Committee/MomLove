package nova.committee.momlove;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import nova.committee.momlove.init.config.ModConfig;
import nova.committee.momlove.init.handler.CmdEventHandler;
import nova.committee.momlove.init.handler.ConfigHandler;
import nova.committee.momlove.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

/**
 * Project: MomLove-fabric
 * Author: cnlimiter
 * Date: 2022/11/1 12:01
 * Description:
 */
public class Momlove implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("MomLove");
    public static MinecraftServer SERVER = null;
    public static Path CONFIG_FOLDER;
    public static ModConfig config;

    @Override
    public void onInitialize() {
        CONFIG_FOLDER = FabricLoader.getInstance().getConfigDir().resolve("momlove");
        FileUtils.checkFolder(CONFIG_FOLDER);
        ServerLifecycleEvents.SERVER_STARTING.register(this::onServerPreStart);

        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);

        ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStopping);
        CmdEventHandler.init();
    }
    public MinecraftServer getServer() {
        return SERVER;
    }

    private void onServerPreStart(MinecraftServer server){
        SERVER = server;
    }
    private void onServerStarted(MinecraftServer server) {
        config = ConfigHandler.load();//读取配置
    }

    private void onServerStopping(MinecraftServer server) {
        ConfigHandler.save(config);

    }

    public static boolean setLove(Player player, boolean byKey) {
        final var b = Momlove.config.getUuidData().add(player.getUUID());
        ConfigHandler.onChange();
        if (b) LOGGER.info("Set love for player {}", player.getName().getString());
        return b;
    }

    public static boolean unLove(Player player) {
        final var b = Momlove.config.getUuidData().remove(player.getUUID());
        ConfigHandler.onChange();
        if (b) LOGGER.info("Unlove player {}", player.getName().getString());
        return b;
    }

    public static boolean addKey(String keyWord) {
        final var b = Momlove.config.getKeyWordsData().add(keyWord);
        ConfigHandler.onChange();
        if (b) LOGGER.info("Add keyword {}", keyWord);
        return b;
    }

    public static boolean delKey(String keyWord) {
        final var b = Momlove.config.getKeyWordsData().remove(keyWord);
        ConfigHandler.onChange();
        if (b) LOGGER.info("Remove keyword {}", keyWord);
        return b;
    }
}
