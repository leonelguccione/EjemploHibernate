package model2_ejb_proyect;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

@Entity
@NamedQueries({ @NamedQuery(name = "E_Bee.findAll", query = "select o from E_Bee o") })
public class E_Bee extends Bee implements Serializable
{
    private static final long serialVersionUID = -7357389235551954082L;
    @Id
    private Integer id;
    @Version
    private Integer version;
    @ManyToOne
    private Honey honey1;

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


    public Honey getHoney1()
    {
        return honey1;
    }

    public void setHoney1(Honey honey1)
    {
        this.honey1 = honey1;
    }
}
