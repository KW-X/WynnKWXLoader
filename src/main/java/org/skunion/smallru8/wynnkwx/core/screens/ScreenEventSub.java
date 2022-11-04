package org.skunion.smallru8.wynnkwx.core.screens;

import org.skunion.smallru8.wynnkwx.WynnKWX;
import org.skunion.smallru8.wynnkwx.utils.Callback;
import org.skunion.smallru8.wynnkwx.utils.DiscordOAuth;

import net.minecraft.client.gui.GuiButton;
//import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = WynnKWX.MODID)
public class ScreenEventSub {
	
	private static DiscordOAuth DCOAuth = null;
	
	@SubscribeEvent
	public static void onGuiOpen(GuiScreenEvent.InitGuiEvent.Post event) {
    	GuiScreen screen = event.getGui();
    	//Minecraft client = Minecraft.getMinecraft();
    	/*
    	if (screen instanceof GuiIngameMenu && event.getButtonList().size() !=0) {
    		for (int k = 0; k < event.getButtonList().size(); k++) {
    			GuiButton OldButton = event.getButtonList().get(k);
				if (OldButton.y >= screen.height / 4 + 96 + -16) {
					OldButton.y += 24;
				}
			}
    		GuiButton settingButton_KWX = new GuiButton(87601, screen.width / 2 - 100, screen.height / 4 + 96 + -16, 200, 20, "KWX");
    		event.getButtonList().add(settingButton_KWX);
    	}
    	*/
    	if(screen instanceof GuiMainMenu && event.getButtonList().size() !=0) {
    		//主選單
    		int x = event.getButtonList().get(0).x;
    		int y = event.getButtonList().get(0).y;
    		int h = event.getButtonList().get(0).height;
    		
    		GuiButton discordVerifyBtn = new GuiButton(87600, x-h-4, y, h, h, TextFormatting.RED+"DC");
    		event.getButtonList().add(discordVerifyBtn);
    	}
    }
	
	@SubscribeEvent
    public static void action(GuiScreenEvent.ActionPerformedEvent.Post event) {
		if (event.getGui() instanceof GuiMainMenu) {
			if(event.getButton().id==87600) {
				//執行 Discord 驗證
				if(DCOAuth != null)
					DCOAuth.cancelDiscordOAuth();
				DCOAuth = new DiscordOAuth();
				DCOAuth.startDiscordOAuth(new Callback() {
					@Override
					public void callback(String dcCode) {
						//這裡會拿到 Discord OAuth 驗證碼
						//載入自訂modular
						if(dcCode!=null) {
							WynnKWX.KWXMODULAR.unloadAllModular();
							event.getButton().displayString = TextFormatting.YELLOW+"DC";
							if(WynnKWX.KWXMODULAR.loadModular(dcCode))
								event.getButton().displayString = TextFormatting.GREEN+"DC";
							else
								event.getButton().displayString = TextFormatting.RED+"DC";
						}
					}
				});
			}
		}
	}
	
}
