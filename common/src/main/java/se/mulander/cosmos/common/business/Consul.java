package se.mulander.cosmos.common.business;

import se.mulander.cosmos.common.model.ConsulDeregister;
import se.mulander.cosmos.common.model.ConsulRegister;
import se.mulander.cosmos.common.model.ConsulService;

import java.net.InetAddress;

/**
 * Interface against consul agent
 * Created by marcu on 2017-04-02.
 */
public class Consul
{
	private static final int CONSUL_AGENT_PORT = 8500;
	private static final String WINDOWS_CONSUL_START_PATH = "../consul/windows/runClient.bat";
	public static void startAgent() throws Exception
	{
		if(HttpRequest.portIsAvailable(CONSUL_AGENT_PORT))
		{
			if(System.getProperty("os.name").toUpperCase().startsWith("WINDOWS"))
			{
				System.out.println("Starting agent at " + WINDOWS_CONSUL_START_PATH);
				ProcessBuilder builder = new ProcessBuilder(new String[]{"CMD.EXE", WINDOWS_CONSUL_START_PATH});
				builder.start();
			}
		}
	}

	public static void deregisterService(String serviceId) throws Exception
	{
		HttpRequest.getRequest("http://localhost:" + CONSUL_AGENT_PORT + "/v1/agent/service/deregister/" + serviceId, null);
	}

	public static void registerService(String id, String name, int port, String... tags) throws Exception
	{
		HttpRequest.putRequest("http://localhost:" + CONSUL_AGENT_PORT + "/v1/agent/service/register", new ConsulService(id, name, port, tags), null);
	}

	private static String getHostName() throws Exception
	{
		return InetAddress.getLocalHost().getHostName();
	}
}
