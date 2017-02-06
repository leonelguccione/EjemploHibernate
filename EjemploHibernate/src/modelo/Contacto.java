package modelo;


// import java.io.Serializable;

public class Contacto
{ // implements Serializable {

    private Integer id;
    private String nombre;
    private String paterno;
    private String materno;
    private String mail;
    private Integer edad;

    public Contacto()
    {
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setPaterno(String paterno)
    {
        this.paterno = paterno;
    }

    public String getPaterno()
    {
        return paterno;
    }

    public void setMaterno(String materno)
    {
        this.materno = materno;
    }

    public String getMaterno()
    {
        return materno;
    }

    public void setMail(String mail)
    {
        this.mail = mail;
    }

    public String getMail()
    {
        return mail;
    }

    public void setEdad(Integer edad)
    {
        this.edad = edad;
    }

    public Integer getEdad()
    {
        return edad;
    }
}
