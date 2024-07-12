package committee.nova.mods.momlove;


import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(Constants.MOD_ID)
@Mod.EventBusSubscriber()
public class MomLoveForge {

    public MomLoveForge() {
        CommonClass.init(FMLPaths.CONFIGDIR.get());
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent e) {
        CommonClass.onServerStarted(e.getServer());
    }
    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent e) {
        CommonClass.onServerStopping(e.getServer());
    }
    @SubscribeEvent
    public static void onServerChat(ServerChatEvent e) {
        CommonClass.onServerChat(e.getPlayer(), e.getRawText());
    }
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent e) {
        CommonClass.registerCmds(e.getDispatcher());
    }

}