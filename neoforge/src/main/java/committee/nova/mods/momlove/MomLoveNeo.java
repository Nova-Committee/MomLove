package committee.nova.mods.momlove;


import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.ServerChatEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

@Mod(Constants.MOD_ID)
@EventBusSubscriber
public class MomLoveNeo {

    public MomLoveNeo(IEventBus eventBus) {
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