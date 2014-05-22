package za.co.prescient.activity.model;

/**
 * Created by Bibhuti on 2014/05/13.
 */
public class CoordinateSystem {

    Integer x;
    Integer y;
    public CoordinateSystem(){}

    public CoordinateSystem(Integer x,Integer y)
    {
        this.x=x;
        this.y=y;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getX() {

        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }
}
