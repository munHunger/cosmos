package se.mulander.cosmos.common.model;

import se.mulander.cosmos.common.business.Consul;

/**
 * Created by marcu on 2017-04-02.
 */
public class ConsulService
{
	public String id;
	public String name;
	public String[] tags;
	public int port;
	public ConsulService(String id, String name, int port, String[] tags)
	{
		this.id = id;
		this.name = name;
		this.port = port;
		this.tags = tags;
	}
}
