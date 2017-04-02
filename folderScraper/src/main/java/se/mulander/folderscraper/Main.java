package se.mulander.folderscraper;


import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;

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
	}

	private static HttpServer createHttpServer() throws IOException
	{
		ResourceConfig resourceConfig = new PackagesResourceConfig("se.munhunger.folderscraper");
		URI uri = UriBuilder.fromUri("http://localhost/").port(PORT_NUMBER).build();
		return HttpServerFactory.create(uri, resourceConfig);
	}
}
