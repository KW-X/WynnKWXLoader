package org.skunion.smallru8.wynnkwx.utils;

import java.awt.Desktop;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.skunion.smallru8.wynnkwx.ConfigKWX;
import org.skunion.smallru8.wynnkwx.WynnKWX;

import com.sun.net.httpserver.HttpServer;

public class DiscordOAuth {

	private CountDownLatch serverLatch = null;
	private boolean isCancelled;
	
	//0
	public DiscordOAuth() {
		isCancelled = false;
	}
	
	public void cancelDiscordOAuth() {
		isCancelled = true;
		if (serverLatch != null) {
            serverLatch.countDown();
        }
	}
	
	//1
	public void startDiscordOAuth(Callback callback) {
		new Thread(new Runnable() {
            public void run() {
            	String dcCode = authorizeDiscord();
            	WynnKWX.LOG.info("[DC_OAuth]Code: "+dcCode);
                if(callback!=null)
                	callback.callback(dcCode);
            }
        }).start();
	}
	
	/**
	 * 
	 * @return Discord OAuth code
	 */
	private String authorizeDiscord() {
		serverLatch = new CountDownLatch(1);
		AtomicReference<String> dcCode = new AtomicReference<>(null);
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress(25580), 0);
			server.createContext("/",httpExchange -> {
				String code = httpExchange.getRequestURI().getQuery();
				if (code != null) {
					dcCode.set(code.split("code=")[1]);
				}
				String response = "<h1>You can now close your browser.<h1>";
				httpExchange.sendResponseHeaders(200, response.length());
				OutputStream stream = httpExchange.getResponseBody();
	            stream.write(response.getBytes());
	            stream.close();
	            this.serverLatch.countDown();
	            server.stop(2);
			});
			server.setExecutor(null);
	        server.start();
			
	        try {//瀏覽器 OAUTH2
				Desktop.getDesktop().browse(new URI(ConfigKWX.DISCORD_OAUTH_LINK));
			} catch (IOException | URISyntaxException e) {
				e.printStackTrace();
			}
	        this.serverLatch.await();
	        server.stop(2);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return null;
		}
		
		if(isCancelled)
			return null;
		
		return dcCode.get();
	}
	
}
