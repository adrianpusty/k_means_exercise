package tests;

import fileUtils.FileRead;
import logic.DataSpace;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Centroid;
import model.DataPoint;

import static org.junit.Assert.assertEquals;

/**
 * Created by Adrian on 2017-04-29.
 */
public class DataSpaceTests {
    @Test
    public void setRandomCentroidsTest() {
        DataSpace dataSpace = new DataSpace();
        FileRead fileRead = new FileRead();
        List<DataPoint> data = fileRead.readFromFile("attract.txt");
        List<Centroid> centroids = dataSpace.setRandomCentroids(data, 5);
        for (Centroid centroid : centroids) {
            System.out.print(centroid.getX() + " ");
            System.out.print(centroid.getY() + " ");
            System.out.println("");
        }
    }

    @Test
    public void pointToCentroidDistanceTest() {
        DataSpace dataSpace = new DataSpace();
        DataPoint point = new DataPoint();
        Centroid centroid = new Centroid();
        point.setX(2.3);
        point.setY(4.6);
        centroid.setX(-1.2);
        centroid.setY(3.33);
        double dist = dataSpace.pointToCentroidDistance(point, centroid);
        assertEquals(dist, 3.72329, 0.0001);
    }

    @Test
    public void testDataSpace() throws IOException {
        DataSpace dataSpace = new DataSpace("attract.txt", 6);
        dataSpace.kMeanProcess();
    }

    @Test
    public void dataSpaceSpecifiedCentroidsTest() throws IOException {
        List<Centroid> centroids = new ArrayList<>();
        centroids.add(new Centroid(4, 2));
        centroids.add(new Centroid(2, 4));
        centroids.add(new Centroid(3, 3));
        centroids.add(new Centroid(2, 2));
        centroids.add(new Centroid(4, 4));
        DataSpace dataSpace = new DataSpace("train.txt", centroids);
        dataSpace.kMeanProcess(5);
    }

    @Test
    public void dataSpaceSpecifiedCentroids2Test() throws IOException {
        List<Centroid> centroids = new ArrayList<>();
        centroids.add(new Centroid(1, 2));
        centroids.add(new Centroid(1, 3));
        centroids.add(new Centroid(2, 1));
        centroids.add(new Centroid(3, 1));

        centroids.add(new Centroid(6, 1));
        centroids.add(new Centroid(7, 1));
        centroids.add(new Centroid(8, 2));
        centroids.add(new Centroid(8, 3));

        centroids.add(new Centroid(1, 6));
        centroids.add(new Centroid(1, 7));
        centroids.add(new Centroid(2, 8));
        centroids.add(new Centroid(3, 8));

        centroids.add(new Centroid(6, 8));
        centroids.add(new Centroid(7, 8));
        centroids.add(new Centroid(8, 7));
        centroids.add(new Centroid(8, 6));
        DataSpace dataSpace = new DataSpace("train.txt", centroids);
        dataSpace.kMeanProcess(1000);
    }

    @Test
    public void dataSpaceRandomCentroidsTest() throws IOException {
        DataSpace dataSpace = new DataSpace("train.txt", 16);
        dataSpace.kMeanProcess(1000);
    }

    @Test
    public void equalsTrueTest() {
        DataPoint dp1 = new DataPoint(3.437891274, 8.3128778484);
        DataPoint dp2 = new DataPoint(3.437891274, 8.3128778484);
        boolean areEqual = dp1.equals(dp2);
        assertEquals(true, areEqual);
    }

    @Test
    public void equalsFalseTest() {
        DataPoint dp1 = new DataPoint(3.437891274, 8.3128778484484);
        DataPoint dp2 = new DataPoint(3.437891274, 8.3128778484483);
        boolean areEqual = dp1.equals(dp2);
        assertEquals(false, areEqual);
    }
}
