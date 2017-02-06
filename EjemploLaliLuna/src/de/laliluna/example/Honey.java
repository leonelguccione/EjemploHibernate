package de.laliluna.example;

import java.io.Serializable;

import java.util.HashSet;
import java.util.Set;

public class Honey implements Serializable
{

    private Integer id;
    private String name;
    private String taste;
    private Set<Bee> bees = new HashSet<Bee>();

    public Honey()
    {
    }

    public Honey(String name, String taste)
    {
        this.name = name;
        this.taste = taste;
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

    public String getTaste()
    {
        return taste;
    }

    public void setTaste(String taste)
    {
        this.taste = taste;
    }

    public Set<Bee> getBees()
    {
        return bees;
    }

    public void setBees(Set<Bee> bees)
    {
        this.bees = bees;
    }

    public String toString()
    {
        return "Honey: " + getId() + " Name: " + getName() + " Taste: " + getTaste();
    }
}
