package committee.nova.mods.momlove;

import committee.nova.mods.momlove.callbacks.IEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;

public class MomLoveFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        CommonClass.init(FabricLoader.getInstance().getConfigDir());
        CommandRegistrationCallback.EVENT.register((dispatcher, commandBuildContext, selection) -> {
            CommonClass.registerCmds(dispatcher);
        });

        ServerLifecycleEvents.SERVER_STARTED.register(CommonClass::onServerStarted);

        ServerLifecycleEvents.SERVER_STOPPING.register(CommonClass::onServerStopping);

        IEvents.SERVER_CHAT.register(CommonClass::onServerChat);
    }


}
