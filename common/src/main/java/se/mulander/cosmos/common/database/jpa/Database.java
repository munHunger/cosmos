package se.mulander.cosmos.common.database.jpa;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Marcus Münger on 2017-04-27.
 */
public class Database
{
    private static SessionFactory sessionFactory;

    public static void resetSessions()
    {
        sessionFactory = null;
    }

    private static void init()
    {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure() // configures settings from hibernate.cfg.xml
                                                                                     .build();
        MetadataSources mds = new MetadataSources(registry);
        Metadata md = mds.buildMetadata();
        sessionFactory = md.buildSessionFactory();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            StandardServiceRegistryBuilder.destroy(registry);
        }));
    }

    public static void runScriptFile(String scriptFile)
    {
        try
        {
            scriptFile = Database.class.getClassLoader().getResource(scriptFile).getPath();
            scriptFile = new File(scriptFile).getCanonicalPath();
            String scriptString = new String(Files.readAllBytes(Paths.get(scriptFile)), Charset.forName("UTF-8"));
            if (sessionFactory == null)
                init();
            try (Session session = sessionFactory.openSession())
            {
                session.beginTransaction();
                for (String query : scriptString.trim().split(";"))
                {
                    Query q = session.createNativeQuery(query);
                    q.executeUpdate();
                }
                session.getTransaction().commit();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void saveObjects(Map<Object, Function<Object, Object>> objects)
    {
        if (sessionFactory == null)
            init();
        try (Session session = sessionFactory.openSession())
        {
            session.beginTransaction();
            for (Object o : objects.keySet())
            {
                if (objects.get(o) != null)
                    session.save(objects.get(o).apply(o));
                else
                    session.save(o);
            }
            session.getTransaction().commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void saveObjects(List objects)
    {
        if (sessionFactory == null)
            init();
        try (Session session = sessionFactory.openSession())
        {
            session.beginTransaction();
            for (Object o : objects)
                session.save(o);
            session.getTransaction().commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void saveObject(Object obj)
    {
        if (sessionFactory == null)
            init();
        try (Session session = sessionFactory.openSession())
        {
            session.beginTransaction();
            session.save(obj);
            session.getTransaction().commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void updateObject(Object obj)
    {
        if (sessionFactory == null)
            init();
        try (Session session = sessionFactory.openSession())
        {
            session.beginTransaction();
            session.update(obj);
            session.getTransaction().commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static List getObjects(String hibernateQuery) throws Exception
    {
        if (sessionFactory == null)
            init();
        try (Session session = sessionFactory.openSession())
        {
            Query query = session.createQuery(hibernateQuery);
            return query.getResultList();
        }
    }

    public static List getObjects(String hibernateQuery, Map<String, Object> parameters) throws Exception
    {
        if (sessionFactory == null)
            init();
        try (Session session = sessionFactory.openSession())
        {
            Query query = session.createQuery(hibernateQuery);
            for (String key : parameters.keySet())
            {
                Object param = parameters.get(key);
                if (param instanceof Collection)
                    query.setParameterList(key, (Collection) param);
                else
                    query.setParameter(key, param);
            }
            return query.getResultList();
        } catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    public static void deleteObject(Object o) throws Exception
    {
        if (sessionFactory == null)
            init();
        try (Session session = sessionFactory.openSession())
        {
            session.beginTransaction();
            session.delete(o);
            session.getTransaction().commit();
        }
    }

    public static void deleteObjects(List objects) throws Exception
    {
        if (sessionFactory == null)
            init();
        try (Session session = sessionFactory.openSession())
        {
            session.beginTransaction();
            for (Object o : objects)
                session.delete(o);
            session.getTransaction().commit();
        }
    }
}