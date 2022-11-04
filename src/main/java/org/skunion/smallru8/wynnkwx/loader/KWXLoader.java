package org.skunion.smallru8.wynnkwx.loader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Properties;

import org.json.JSONObject;
import org.skunion.smallru8.wynnkwx.WynnKWX;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class KWXLoader {

	private String hostname = "http://localhost";
	private URLClassLoader pluginsClassLoader;
	private ArrayList<KWX_Interface> modulars;
	
	public KWXLoader(String server) {
		hostname=server;
		modulars = new ArrayList<KWX_Interface>();
	}
	
	public boolean loadModular(String discord_oauth2_code) {
		modulars.clear();
		JSONObject tokenAndFileLs = verifyDiscord(discord_oauth2_code);
		if(tokenAndFileLs==null)
			return false;
		String access_token = tokenAndFileLs.getString("access_token");
		ArrayList<URL> links = new ArrayList<URL>();
		for(int i=0;i<tokenAndFileLs.getJSONArray("mod_func_ls").length();i++) {
			try {
				links.add(new URL(hostname+"/mod/dl/"+tokenAndFileLs.getJSONArray("mod_func_ls").getString(i)+"/"+access_token));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		pluginsClassLoader = new URLClassLoader(links.toArray(new URL[0]));
		
		try {
			Enumeration<URL> kwx_yml = pluginsClassLoader.findResources("kwx.yml");
			while(kwx_yml.hasMoreElements()) {
				InputStream is = kwx_yml.nextElement().openStream();
				/**
				 * path=com.github.smallru8.......
				 */
				Properties pro = new Properties();
				pro.load(is);
				String loadPath = pro.getProperty("path");
				is.close();
				try {
					///第一個切入點
					Class<?> tmpClass = pluginsClassLoader.loadClass(loadPath);
					KWX_Interface tmpInterface = (KWX_Interface) tmpClass.getDeclaredConstructor().newInstance();///創建plugins class
					modulars.add(tmpInterface);
					tmpInterface.onEnable();
					WynnKWX.LOG.info("[KWXLoader] Load: " + tmpInterface.getModularName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void unloadAllModular() {
		try {
			modulars.forEach(m -> {
				m.onDisable();
			});
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 驗證、取模塊列表
	 * @param discord_oauth2_code
	 * @return
	 */
	private JSONObject verifyDiscord(String discord_oauth2_code) {
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				  .url(hostname+"/mod/ls/"+discord_oauth2_code)
				  .get()
				  .addHeader("Accept", "application/json")
				  .build();
		try {
			Response response = client.newCall(request).execute();
			
			while(response==null) {
				Thread.sleep(100);
			}
			return new JSONObject(response.body().string());
	
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
