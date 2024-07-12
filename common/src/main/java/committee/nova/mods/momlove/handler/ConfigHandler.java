package committee.nova.mods.momlove.handler;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import committee.nova.mods.momlove.CommonClass;
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

        if (!CommonClass.configFolder.toFile().isDirectory()) {
            try {
                Files.createDirectories(CommonClass.configFolder);
            } catch (IOException ignored) {
            }
        }

        Path configPath = CommonClass.configFolder.resolve(config.getConfigName() + ".json");
        if (configPath.toFile().isFile()) {
            try {
                config = GSON.fromJson(FileUtils.readFileToString(configPath.toFile(), StandardCharsets.UTF_8),
                        Configuration.class);
            } catch (IOException ignored) {
            }
        } else {
            try {
                FileUtils.write(configPath.toFile(), GSON.toJson(config), StandardCharsets.UTF_8);
            } catch (IOException ignored) {
            }
        }

        return config;
    }

    public static void save(Configuration config) {
        if (!CommonClass.configFolder.toFile().isDirectory()) {
            try {
                Files.createDirectories(CommonClass.configFolder);
            } catch (IOException ignored) {
            }
        }
        Path configPath = CommonClass.configFolder.resolve(config.getConfigName() + ".json");
        try {
            FileUtils.write(configPath.toFile(), GSON.toJson(config), StandardCharsets.UTF_8);
        } catch (IOException ignored) {
        }
    }

    public static void onChange() {
        ConfigHandler.save(CommonClass.config);
        CommonClass.config = ConfigHandler.load();
    }
}
