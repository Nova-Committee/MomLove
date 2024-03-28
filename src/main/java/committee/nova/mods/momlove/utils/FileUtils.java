package committee.nova.mods.momlove.utils;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Project: MomLove-fabric
 * Author: cnlimiter
 * Date: 2022/11/1 12:11
 * Description:
 */
public class FileUtils {

    public FileUtils() {
    }

    public static void checkFolder(Path folder) {
        if (!folder.toFile().isDirectory()) {
            try {
                Files.createDirectories(folder);
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }

    }
}
