package se.mulander.cosmos.common.model;

/**
 * Created by marcu on 2017-04-02.
 */
public class ConsulRegister
{
	String node;
	String address = "127.0.0.1";
	ConsulService service;
	public ConsulRegister(String node, ConsulService service)
	{
		this.node = node;
		this.service = service;
	}
}
