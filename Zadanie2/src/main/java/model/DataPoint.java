package model;

/**
 * Created by Adrian on 2017-04-29.
 */
public class DataPoint {
    private double x;
    private double y;
    private Centroid centroid;

    public Centroid toCentroid() {
        Centroid c = new Centroid();
        c.setX(this.x);
        c.setY(this.y);
        return c;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Centroid getCentroid() {
        return centroid;
    }

    public void setCentroid(Centroid centroid) {
        this.centroid = centroid;
    }
}
