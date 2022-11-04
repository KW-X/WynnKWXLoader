package org.skunion.smallru8.wynnkwx;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;

@Config(modid = WynnKWX.MODID)
public class ConfigKWX {

	@Comment({
		  "Discord OAuth2 link."
		})
	public static String DISCORD_OAUTH_LINK = "https://discord.com/api/oauth2/authorize?client_id=865838197782347796&redirect_uri=http%3A%2F%2Flocalhost%3A25580&response_type=code&scope=identify%20email";
	
	@Comment({
		  "KWX's API server."
		})
	public static String KWX_API_SERVER = "https://api.sample.org";
	
}
