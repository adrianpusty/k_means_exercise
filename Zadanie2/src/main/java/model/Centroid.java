package model;

import java.util.List;

/**
 * Created by Adrian on 2017-04-29.
 */
public class Centroid {
    private double x;
    private double y;
    private List<DataPoint> points;

    public Centroid() {

    }

    public Centroid(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Centroid other) {
        if ((this.getX() - other.getX()) > 0.0000000001) {
            return false;
        }
        if ((this.getY() - other.getY()) > 0.0000000001) {
            return false;
        }
        return true;
    }

    public DataPoint toDataPoint() {
        DataPoint dp = new DataPoint();
        dp.setX(this.x);
        dp.setY(this.y);
        return dp;
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

    public List<DataPoint> getPoints() {
        return points;
    }

    public void setPoints(List<DataPoint> points) {
        this.points = points;
    }
}
