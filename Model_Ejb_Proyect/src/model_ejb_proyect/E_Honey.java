package model_ejb_proyect;

import java.io.Serializable;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Version;

@Entity
@NamedQueries({ @NamedQuery(name = "E_Honey.findAll", query = "select o from E_Honey o") })
public class E_Honey implements Serializable
{
    private static final long serialVersionUID = 5533192720911670334L;
    @Id
    private Integer id;
    @Version
    private Integer version;
    @OneToMany(mappedBy = "e_Honey")
    private List<E_Bee> e_BeeList;

    public E_Honey()
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

    public List<E_Bee> getE_BeeList()
    {
        return e_BeeList;
    }

    public void setE_BeeList(List<E_Bee> e_BeeList)
    {
        this.e_BeeList = e_BeeList;
    }
}
