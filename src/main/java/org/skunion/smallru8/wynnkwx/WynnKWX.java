package org.skunion.smallru8.wynnkwx;

import java.awt.EventQueue;

import org.apache.logging.log4j.Logger;
import org.skunion.smallru8.wynnkwx.core.screens.ScreenEventSub;
import org.skunion.smallru8.wynnkwx.ctrlpanel.MainPanel;
import org.skunion.smallru8.wynnkwx.loader.KWXLoader;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
@Mod(modid = WynnKWX.MODID, name = WynnKWX.NAME, version = WynnKWX.VERSION)
public class WynnKWX {

	public static final String MODID = "wynnkwx";
    public static final String NAME = "WynnKWX Main";
    public static final String VERSION = "1.12.2-0.0.1.0";
    public static KWXLoader KWXMODULAR;
    public static ConfigKWX KWXCONFIG;
    public static MainPanel KWXMainPanel;
    
    public static Logger LOG;
    
    public WynnKWX() {
    	MinecraftForge.EVENT_BUS.register(new ScreenEventSub());
    	MinecraftForge.EVENT_BUS.register(new Listener());
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
    	LOG = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
    	KWXCONFIG = new ConfigKWX();
    	KWXMODULAR = new KWXLoader(ConfigKWX.KWX_API_SERVER);
    }
   
    @EventHandler
    public void finishLoading(FMLLoadCompleteEvent event) {
    	EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KWXMainPanel = new MainPanel();
					KWXMainPanel.frmKwxpanel.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }
    
}
