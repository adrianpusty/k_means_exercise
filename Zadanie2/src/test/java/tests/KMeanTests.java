package tests;

import fileUtils.FileReader;
import logic.KMean;
import org.junit.Test;

import java.util.List;
import model.Centroid;
import model.DataPoint;

import static org.junit.Assert.assertEquals;

/**
 * Created by Adrian on 2017-04-29.
 */
public class KMeanTests {
    @Test
    public void setRandomCentroidsTest() {
        KMean kMean = new KMean();
        FileReader fileReader = new FileReader();
        List<DataPoint> data = fileReader.readFromFile("attract.txt");
        List<Centroid> centroids = kMean.setRandomCentroids(data, 5);
        for(Centroid centroid : centroids) {
            System.out.print(centroid.getX() + " ");
            System.out.print(centroid.getY() + " ");
            System.out.println("");
        }
    }

    @Test
    public void pointToCentroidDistanceTest() {
        KMean kMean = new KMean();
        DataPoint point = new DataPoint();
        Centroid centroid = new Centroid();
        point.setX(2.3);
        point.setY(4.6);
        centroid.setX(-1.2);
        centroid.setY(3.33);
        double dist = kMean.pointToCentroidDistance(point, centroid);
        assertEquals(dist, 3.72329, 0.0001);
    }
}
