package se.munhunger.folderscraper;


import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;
import se.munhunger.folderscraper.utils.business.Scraper;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

/**
 * Created by marcu on 2017-02-14.
 */
public class Main
{

	private static final int PORT_NUMBER = 8080;

	public static void main(String[] args) throws IOException
	{
		System.out.println("Starting scraper HTTPServer...\n");
		HttpServer httpServer = createHttpServer();
		httpServer.start();
		System.out.println("Started scraper server successfully!");
		startWatcher();
	}

	private static void startWatcher()
	{
		new Thread((Runnable) () ->
		{
			while(true)
			{
				try
				{
					new Scraper().getFolderStatus();
					Thread.sleep(Settings.values.updateDelay * 1000);
				}
				catch(Exception e)
				{
					System.err.println("Error while running watcher");
					e.printStackTrace();
				}
			}
		}).start();
	}

	private static HttpServer createHttpServer() throws IOException
	{
		ResourceConfig resourceConfig = new PackagesResourceConfig("se.munhunger.folderscraper");
		URI uri = UriBuilder.fromUri("http://localhost/").port(PORT_NUMBER).build();
		return HttpServerFactory.create(uri, resourceConfig);
	}
}
