package se.mulander.cosmos.common.database.jpa;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Map;

/**
 * Created by marcu on 2017-06-25.
 */
public class Database
{
	private static SessionFactory sessionFactory;

	private static void init()
	{
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure() // configures settings from hibernate.cfg.xml
				.build();
		sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		Runtime.getRuntime().addShutdownHook(new Thread(() ->
		{
			System.out.println("Destroying database StandardServiceRegistryBuilder");
			StandardServiceRegistryBuilder.destroy(registry);
		}));
	}

	public static void saveObject(Object obj)
	{
		if(sessionFactory == null)
			init();
		try(Session session = sessionFactory.openSession())
		{
			session.beginTransaction();
			session.save(obj);
			session.getTransaction().commit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public static List getObjects(String hibernateQuery) throws Exception
	{
		if(sessionFactory == null)
			init();
		try(Session session = sessionFactory.openSession())
		{
			Query query = session.createQuery(hibernateQuery);
			return query.getResultList();
		}
	}

	public static List getObjects(String hibernateQuery, Map<String, Object> parameters) throws Exception
	{
		if(sessionFactory == null)
			init();
		try(Session session = sessionFactory.openSession())
		{
			Query query = session.createQuery(hibernateQuery);
			for(String key : parameters.keySet())
				query.setParameter(key, parameters.get(key));
			return query.getResultList();
		}
	}

	public static void deleteObjects(Object o) throws Exception
	{
		if(sessionFactory == null)
			init();
		try(Session session = sessionFactory.openSession())
		{
			session.beginTransaction();
			session.delete(o);
			session.getTransaction().commit();
		}
	}
}