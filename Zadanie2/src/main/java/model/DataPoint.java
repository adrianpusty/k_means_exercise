package model;

import java.util.List;

/**
 * Created by Adrian on 2017-04-29.
 */
public class DataPoint {
    private double x;
    private double y;
    private Centroid centroid;
    private List<Double> distancesToCentroids;

    public DataPoint() {

    }

    public DataPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Zwraca indeks centroidu (dokladniej indeks z listy odleglosci do centroidow, ktory bedzoe tozsamy z indeksem z listy centroidow
     * w obiekcie DataSpace) do ktorego dany datapoint ma najblizej
     *
     * @return
     */
    public int findMinDistIndex() {
        double minDist = Double.MAX_VALUE;
        int minDistCentroidId = Integer.MAX_VALUE;

        for (int i = 0; i < this.getDistancesToCentroids().size(); i++) {
            if (this.getDistancesToCentroids().get(i) < minDist) {
                minDist = this.getDistancesToCentroids().get(i);
                minDistCentroidId = i;
            }
        }
        return minDistCentroidId;
    }

    public boolean equals(DataPoint other) {
        if ((this.getX() - other.getX()) > 0.0000000001) {
            return false;
        }
        if ((this.getY() - other.getY()) > 0.0000000001) {
            return false;
        }
        return true;
    }

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

    public List<Double> getDistancesToCentroids() {
        return distancesToCentroids;
    }

    public void setDistancesToCentroids(List<Double> distancesToCentroids) {
        this.distancesToCentroids = distancesToCentroids;
    }
}
