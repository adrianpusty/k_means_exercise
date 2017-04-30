package model;

import java.util.List;

/**
 * Created by Adrian on 2017-04-29.
 */
public class Group {
    private Centroid centroid;
    private List<DataPoint> dataPoints;

    public Centroid getCentroid() {
        return centroid;
    }

    public void setCentroid(Centroid centroid) {
        this.centroid = centroid;
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }
}
