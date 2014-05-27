package za.co.prescient.activity.model;


import java.io.Serializable;

public class TouchPoint implements Serializable {
    Long id;
    String name;

    public void setId(Long id)
    {
        this.id=id;
    }

    public void setName(String name)
    {
        this.name=name;
    }

    public Long getId(){
        return this.id;
    }

    public String getName()
    {
       return this.name;
    }

    public String toString()
    {
       return getId()+"::"+getName();
    }
}
