package se.mulander.cosmos.common.model;

/**
 * Created by marcu on 2017-04-02.
 */
public class ConsulDeregister
{
	String node;
	String serviceId;

	public ConsulDeregister(String node, String serviceId)
	{
		this.node = node;
		this.serviceId = serviceId;
	}
}
