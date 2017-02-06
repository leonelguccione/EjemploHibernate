package de.laliluna.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class InitSessionFactory
{
    /** The single instance of hibernate SessionFactory */
    private static org.hibernate.SessionFactory sessionFactory;

    private InitSessionFactory()
    {
    }

    static
    {
        final Configuration cfg = new Configuration();
        cfg.configure("/hibernate.cfg.xml");
        sessionFactory = cfg.buildSessionFactory();
    }

    public static SessionFactory getInstance()
    {
        return sessionFactory;
    }

}
