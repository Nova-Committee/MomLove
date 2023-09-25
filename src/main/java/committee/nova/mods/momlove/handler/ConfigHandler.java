package committee.nova.mods.momlove.handler;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import committee.nova.mods.momlove.MomLove;
import committee.nova.mods.momlove.config.Configuration;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Project: MomLove-Forge
 * Author: cnlimiter
 * Date: 2022/11/1 12:08
 * Description:
 */
public class ConfigHandler {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static Configuration load() {
        Configuration config = new Configuration();

        if (!MomLove.getConfigFolder().toFile().isDirectory()) {
            try {
                Files.createDirectories(MomLove.getConfigFolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Path configPath = MomLove.getConfigFolder().resolve(config.getConfigName() + ".json");
        if (configPath.toFile().isFile()) {
            try {
                config = GSON.fromJson(FileUtils.readFileToString(configPath.toFile(), StandardCharsets.UTF_8),
                        Configuration.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                FileUtils.write(configPath.toFile(), GSON.toJson(config), StandardCharsets.UTF_8);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return config;
    }

    public static void save(Configuration config) {
        if (!MomLove.getConfigFolder().toFile().isDirectory()) {
            try {
                Files.createDirectories(MomLove.getConfigFolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Path configPath = MomLove.getConfigFolder().resolve(config.getConfigName() + ".json");
        try {
            FileUtils.write(configPath.toFile(), GSON.toJson(config), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void onChange() {
        ConfigHandler.save(MomLove.getConfig());
        MomLove.setConfig(ConfigHandler.load());
    }
}
