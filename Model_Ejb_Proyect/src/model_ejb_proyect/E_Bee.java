package model_ejb_proyect;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

@Entity
@NamedQueries({ @NamedQuery(name = "E_Bee.findAll", query = "select o from E_Bee o") })
public class E_Bee implements Serializable
{
    private static final long serialVersionUID = 7924248291265824336L;
    @Id
    private Integer id;
    @Version
    private Integer version;
    @ManyToOne
    private E_Honey e_Honey;

    public E_Bee()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion(Integer version)
    {
        this.version = version;
    }

    public E_Honey getE_Honey()
    {
        return e_Honey;
    }

    public void setE_Honey(E_Honey e_Honey)
    {
        this.e_Honey = e_Honey;
    }
}
