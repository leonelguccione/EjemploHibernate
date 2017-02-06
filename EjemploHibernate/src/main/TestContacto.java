package main;

import java.util.Iterator;
import java.util.List;

import modelo.Contacto;

import org.hibernate.Session;
import org.hibernate.Transaction;

import util.HibernateUtil;

public class TestContacto
{
    public TestContacto()
    {
    }

    public static void main(String[] args)
    {
        // Second unit of work
        Session newSession = HibernateUtil.getSessionFactory().openSession();
        Transaction newTransaction = newSession.beginTransaction();

        System.out.println("\n\n\n");
        // CONSULTA
        List contactos = null;
        // contactos = newSession.createQuery("from Contacto c ").list();
        // contactos = newSession.createQuery("from Contacto c where c.paterno = 'Contreras'  ").list();
        contactos = newSession.createQuery("from Contacto c where c.edad > 26 order by c.edad desc ").list();
        System.out.println(" >>>>>>>>>>> Contactos (" + contactos.size() + ")<<<<<<<<<<< ");
        for (Iterator iter = contactos.iterator(); iter.hasNext();)
        {
            Contacto loadedContacto = (Contacto) iter.next();
            System.out.println("--->" + loadedContacto.getId() + " - " + loadedContacto.getNombre() + " -|- " +
                               loadedContacto.getPaterno() + " -|- " + loadedContacto.getMaterno() + " -|- " +
                               loadedContacto.getMail() + " -|- " + loadedContacto.getEdad());
        }
        System.out.println("\n\n\n");

        /*
            // INSERTA
            Contacto newCont = new Contacto();
            newCont.setNombre("Jorge");
            newCont.setPaterno("Flores");
            newCont.setMaterno("Jorge");
            newCont.setMail("Jorge");
            newSession.save(newCont);
        */

        /*
            // ACTUALIZAR
            Contacto newCont = (Contacto) newSession.get(Contacto.class, new Integer(3));
            System.out.println("--->" + newCont.getId() + " - " + newCont.getNombre() + " " + newCont.getPaterno() + " " + newCont.getMaterno() + " " + newCont.getMail());
            newCont.setNombre("Armando");
            newSession.merge(newCont);
        */

        /*
             // ELIMINAR
             Contacto newCont = (Contacto) newSession.get(Contacto.class, new Integer(3));
             System.out.println("--->" + newCont.getId() + " - " + newCont.getNombre() + " " + newCont.getPaterno() + " " + newCont.getMaterno() + " " + newCont.getMail());
             newSession.delete(newCont);
        */

        newTransaction.commit();
        newSession.close();

        // Shutting down the application
        HibernateUtil.shutdown();
    }
}
