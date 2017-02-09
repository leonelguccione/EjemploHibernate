package model2_ejb_proyect;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Version;

@Entity
@NamedQueries({ @NamedQuery(name = "Honey.findAll", query = "select o from Honey o") })
public class Honey implements Serializable
{
    private static final long serialVersionUID = -5140906016257115833L;
    @Id
    private Integer id;
    @Version
    private Integer version;

    public Honey()
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
}
