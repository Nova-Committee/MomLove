package nova.committee.momlove.init.handler;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nova.committee.momlove.Momlove;
import nova.committee.momlove.init.config.ModConfig;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Project: MomLove-fabric
 * Author: cnlimiter
 * Date: 2022/11/1 12:08
 * Description:
 */
public class ConfigHandler {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static ModConfig load() {
        ModConfig config = new ModConfig();

        if (!Momlove.CONFIG_FOLDER.toFile().isDirectory()) {
            try {
                Files.createDirectories(Momlove.CONFIG_FOLDER);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Path configPath = Momlove.CONFIG_FOLDER.resolve(config.getConfigName() + ".json");
        if (configPath.toFile().isFile()) {
            try {
                config = GSON.fromJson(FileUtils.readFileToString(configPath.toFile(), StandardCharsets.UTF_8),
                        ModConfig.class);
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

    public static void save(ModConfig config) {
        if (!Momlove.CONFIG_FOLDER.toFile().isDirectory()) {
            try {
                Files.createDirectories(Momlove.CONFIG_FOLDER);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Path configPath = Momlove.CONFIG_FOLDER.resolve(config.getConfigName() + ".json");
        try {
            FileUtils.write(configPath.toFile(), GSON.toJson(config), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void onChange(){
        ConfigHandler.save(Momlove.config);
        Momlove.config = ConfigHandler.load();
    }
}
