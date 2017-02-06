package de.laliluna.example;

import de.laliluna.hibernate.HibernateUtil;
import de.laliluna.hibernate.InitSessionFactory;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TestExample
{
    private static Logger log = Logger.getLogger(TestExample.class);


    private static Honey createHoney()
    {
        Honey forestHoney = new Honey();
        forestHoney.setName("forest honey");
        forestHoney.setTaste("very sweet");

        //Session session = InitSessionFactory.getInstance().openSession();
        //Session session = InitSessionFactory.openSession();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.save(forestHoney);
        tx.commit();
        session.close();
        return forestHoney;
    }

    //The method update creates a new object using our last method, changes the name and updates the object using session.update.
    private static void update()
    {
        Honey honey = createHoney();
        Session session = InitSessionFactory.getInstance().openSession();
        Transaction tx = session.beginTransaction();
        honey.setName("Modern style");
        session.update(honey);
        tx.commit();
        session.close();
    }

    //The method delete creates an object and deletes it by calling session.delete.
    private static void delete()
    {
        Honey honey = createHoney();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.delete(honey);
        tx.commit();
        session.close();
    }

    // The tables are emptied with the method clean.
    // The method session.createQuery creates a new query and runs it by calling executeUpdate.
    private static void clean()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("delete from Bee").executeUpdate();
        session.createQuery("delete from Honey").executeUpdate();
        tx.commit();
        session.close();
    }

    //The method createRelation shows, how to create objects and set an association between these objects.
    //This will write a foreign key relation to the database.
    private static void createRelation()
    {
        Session session = InitSessionFactory.getInstance().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Honey honig = new Honey();
        honig.setName("OdenwaldHonig");
        honig.setTaste("Herrlich, geschmeidig im Abgang");
        session.save(honig);
        Bee biene = new Bee("Sonja");
        session.save(biene);

        // Beziehung auf beiden Seiten erstellen
        biene.setHoney(honig);
        honig.getBees().add(biene);
        tx.commit();
    }

    //The method query shows how to query all honeys in the database.
    //The call to session.createQuery creates the query and list() runs the query and returns a list of Honey objects.
    private static void query()
    {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List honeys = session.createQuery("select h from Honey as h").list();
        for (Iterator iter = honeys.iterator(); iter.hasNext();)
        {
            Honey element = (Honey) iter.next();
            log.debug(element);
        }
        tx.commit();
        session.close();
    }

    public static void main(String[] args)
    {
        /* clean tables */
        try
        {
            //clean();

            /* simple create example */
            //createHoney();

            /* relation example */
            createRelation();

            /* delete example */
            //delete();

            /* update example */
            //update();

            /* query example */
            //query();
        }
        catch (RuntimeException e)
        {
            try
            {
                Session session = InitSessionFactory.getInstance().getCurrentSession();
                if (session.getTransaction().isActive())
                    session.getTransaction().rollback();
            }
            catch (HibernateException el)
            {
                log.error("Fehler beim Rollback der Transaktion");
            }
            throw e;
        }
    }
}

