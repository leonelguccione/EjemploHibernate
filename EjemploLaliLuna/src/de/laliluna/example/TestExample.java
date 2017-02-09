package de.laliluna.example;

import de.laliluna.hibernate.InitSessionFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class TestExample
{
    private Logger log = Logger.getLogger(TestExample.class);


    public Honey createHoney()
    {
        Honey forestHoney = new Honey();
        forestHoney.setName("forest honey");
        forestHoney.setTaste("very sweet");

        //Session session = InitSessionFactory.getInstance().openSession();
        //Session session = InitSessionFactory.openSession();
        Session session = InitSessionFactory.getInstance().getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.save(forestHoney);
        tx.commit();
        //session.close();
        return forestHoney;
    }

    //The method update creates a new object using our last method, changes the name and updates the object using session.update.
    public void update()
    {
        Honey honey = createHoney();
        Session session = InitSessionFactory.getInstance().getCurrentSession();
        Transaction tx = session.beginTransaction();
        honey.setName("Modern style");
        session.update(honey);
        tx.commit();
        //session.close();
    }

    //The method delete creates an object and deletes it by calling session.delete.
    public void delete()
    {
        Honey honey = createHoney();
        Session session = InitSessionFactory.getInstance().getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.delete(honey);
        tx.commit();
        //session.close();
    }

    // The tables are emptied with the method clean.
    // The method session.createQuery creates a new query and runs it by calling executeUpdate.
    public void clean()
    {
        Session session = InitSessionFactory.getInstance().getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.createQuery("delete from Bee").executeUpdate();
        session.createQuery("delete from Honey").executeUpdate();
        tx.commit();
        //session.close();
    }

    //The method createRelation shows, how to create objects and set an association between these objects.
    //This will write a foreign key relation to the database.
    public void createRelation()
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
    public List query()
    {
        Session session = InitSessionFactory.getInstance().getCurrentSession();
        Transaction tx = session.beginTransaction();
        List honeys = session.createQuery("select h from Honey as h").list();
        for (Iterator iter = honeys.iterator(); iter.hasNext();)
        {
            Honey element = (Honey) iter.next();
            log.debug(element);
        }
        tx.commit();
        //        session.close();
        return honeys;
    }

    public static void main(String[] args)
    {
        TestExample tx = new TestExample();
        Scanner sc = new Scanner(System.in);
        String a[] = new String[7];
        a[0] = "0 - SALIR";
        a[1] = "1 - limpiar";
        a[2] = "2 - crearHoney";
        a[3] = "3 - createRelation";
        a[4] = "4 - delete";
        a[5] = "5 - update";
        a[6] = "6 - query";
        int op;
        try
        {
            //gran pedorrada
            System.out.println("elija opción \n");
            int i = 0;
            for (i = 0; i < 7; i++)
                System.out.println(a[i] + "\n");
            //op = 3;
            op = sc.nextInt();
            System.out.println("opcion electa: " + a[op]);
            if (op == 1)
            {
                tx.clean();
                System.out.println("clean listo");
            }
            else if (op == 2)
            {
                tx.createHoney();
                System.out.println("createHoney listo");
            }

            else if (op == 3)
            {
                tx.createRelation();
                System.out.println("createRelation listo");
            }
            else if (op == 4)
            {
                tx.delete();
                System.out.println("delete listo");
            }
            else if (op == 5)
            {
                tx.update();
                System.out.println("update listo");
            }
            else if (op == 6)
            {
                tx.query();
                System.out.println("query listo");
            }

        }
        catch (RuntimeException e)
        {
            try
            {
                Session session = InitSessionFactory.getInstance().getCurrentSession();
                if (session.getTransaction().isActive())
                    session.getTransaction().rollback();
            }
            catch (Exception el)
            {
                tx.log.error("Fehler beim Rollback der Transaktion");
            }
            throw e;
        }
    }
}

