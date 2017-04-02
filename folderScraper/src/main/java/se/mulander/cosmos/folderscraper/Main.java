package se.mulander.cosmos.folderscraper;


import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;
import se.mulander.cosmos.common.business.Consul;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

/**
 * Created by marcu on 2017-02-14.
 */
public class Main
{
	private static final int PORT_NUMBER = 8181;
	private static final String SERVICE_NAME = "folderscraper";

	public static void main(String[] args) throws Exception
	{
		System.out.println("Starting scraper HTTPServer...\n");
		HttpServer httpServer = createHttpServer();
		httpServer.start();
		System.out.println("Started scraper server successfully!");
		System.out.println("Setting up consul...");
		Consul.startAgent();
		Consul.registerService(Main.class.getName(), SERVICE_NAME, PORT_NUMBER);
		System.out.println("Consul correctly setup");
	}

	private static HttpServer createHttpServer() throws IOException
	{
		ResourceConfig resourceConfig = new PackagesResourceConfig(Main.class.getPackage().getName());
		URI uri = UriBuilder.fromUri("http://localhost/").port(PORT_NUMBER).build();
		return HttpServerFactory.create(uri, resourceConfig);
	}
}
