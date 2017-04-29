package model;

import java.util.List;

/**
 * Created by Adrian on 2017-04-29.
 */
public class Centroid {
    private double x;
    private double y;
    private List<List<DataPoint>> points;

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

    public List<List<DataPoint>> getPoints() {
        return points;
    }

    public void setPoints(List<List<DataPoint>> points) {
        this.points = points;
    }
}
