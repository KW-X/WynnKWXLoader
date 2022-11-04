package org.skunion.smallru8.wynnkwx;

import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = WynnKWX.MODID)
public class Listener {
	 
    /**
     * Save config
     * @param event
     */
    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    	if (event.getModID().equals(WynnKWX.MODID)) {
    		ConfigManager.sync(WynnKWX.MODID, Type.INSTANCE);
    	}
    }
    
}
