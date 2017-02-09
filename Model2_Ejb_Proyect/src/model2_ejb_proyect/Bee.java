package model2_ejb_proyect;

import java.io.Serializable;

public class Bee implements Serializable
{
    private Integer id;
    private String name;
    private Honey honey;
    
    public Bee()
    {
        super();
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Honey getHoney()
    {
        return honey;
    }

    public void setHoney(Honey honey)
    {
        this.honey = honey;
    }
}
