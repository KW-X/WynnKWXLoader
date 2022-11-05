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

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

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
		trustAllTLS();
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
			if(pluginsClassLoader!=null)
				pluginsClassLoader.close();
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
		OkHttpClient client = getUnsafeOkHttpClient();
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

	private OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }
 
                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }
 
                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };
 
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
 
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
 
            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

	private void trustAllTLS() {
		TrustManager[] trustAllCerts = new TrustManager[]{
			    new X509TrustManager() {
			        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			            return null;
			        }
			        public void checkClientTrusted(
			            java.security.cert.X509Certificate[] certs, String authType) {
			        }
			        public void checkServerTrusted(
			            java.security.cert.X509Certificate[] certs, String authType) {
			        }
			    }
			};

			// Install the all-trusting trust manager
			try {
			    SSLContext sc = SSLContext.getInstance("SSL");
			    sc.init(null, trustAllCerts, new java.security.SecureRandom());
			    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch (Exception e) {
			}
	}
	
}
