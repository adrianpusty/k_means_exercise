package logic;

import model.Centroid;
import model.DataPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Adrian on 2017-04-29.
 */
public class KMean {
    int numberOfCentroids;
    String method;

    /**
     * Z zadanego zbioru punktow wybiera losowo centroidy (ich liczba okreslona przez parametr numberOfCentroids)
     *
     * @param dataPoints
     * @param numberOfCentroids
     * @return
     */
    public List<Centroid> setRandomCentroids(List<DataPoint> dataPoints, int numberOfCentroids) {
        List<Centroid> initialCentroids = new ArrayList<Centroid>();
        for (int i = 0; i < numberOfCentroids; i++) {
            int randomRow = ThreadLocalRandom.current().nextInt(0, dataPoints.size());
            initialCentroids.add(dataPoints.get(randomRow).toCentroid());
        }
        return initialCentroids;
    }

    /**
     * Oblicza odleglosc miedzy danym punktem w przestrzeni dwuwymiarowej a jednym z centroidow
     *
     * @param specificPoint
     * @param specificCentroid
     * @return
     */
    public double pointToCentroidDistance(DataPoint specificPoint, Centroid specificCentroid) {
        double xDist = specificPoint.getX() - specificCentroid.getX();
        double yDist = specificPoint.getY() - specificCentroid.getY();
        return Math.sqrt(xDist * xDist + yDist * yDist);
    }
}
