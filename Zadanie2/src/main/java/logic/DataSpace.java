package logic;

import fileUtils.FileRead;
import fileUtils.FileWrite;
import model.Centroid;
import model.DataPoint;
import model.Group;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Adrian on 2017-04-29.
 */
public class DataSpace {
    private List<Centroid> centroids;
    private List<DataPoint> dataPoints;
    private List<Group> groups;
    private int numberOfCentroids;
    String method;

    public void kMeanProcess(int numberOfIterations) throws IOException {
        for (int i = 0; i < numberOfIterations; i++) {
            setAffiliations();
            choseNewCentroids();
            assignElementsToGroups();
            FileWrite fw = new FileWrite();
            if (i % 100 == 0) {
                fw.writeGroups(this, "out" + i + ".csv");
            }
            if (i < 100) {
                System.out.println(countErrors());
            }
        }
    }

    public DataSpace() {

    }

    public DataSpace(String fileName, int numberOfCentroids) {
        FileRead fileRead = new FileRead();
        this.dataPoints = fileRead.readFromFile(fileName);
        this.centroids = setRandomCentroids(dataPoints, numberOfCentroids);
        this.numberOfCentroids = numberOfCentroids;
    }

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

    /**
     * Dla kazdego punktu okresla jego odleglosc do poszczegolnych centroidow
     */
    public void calculateDistanceToCentroids() {
        for (DataPoint dp : dataPoints) {
            List<Double> distances = new ArrayList<>();
            for (Centroid c : centroids) {
                double distance = pointToCentroidDistance(dp, c);
                distances.add(distance);
            }
            dp.setDistancesToCentroids(distances);
        }
    }

    /**
     * Czysci liste punktow dla kazdego centroidu
     */
    public void clearCentroidsDataPointsList() {
        for (Centroid c : centroids) {
            c.setPoints(new ArrayList<DataPoint>());
        }
    }

    /**
     * Dla kazdego punktu wybiera najmniejsza odleglosc i ustawia centroid odpowiadajacy tej odleglosci
     * Dla kazdego centroidu przypisuje punkt mial najblizej do tego centroidu
     */
    public void setAffiliations() {
        calculateDistanceToCentroids();
        clearCentroidsDataPointsList();

        //Uaktualnienie przynaleznosci: przypiecie do datapointu centroidu a do centroidu datapointu
        for (DataPoint dp : dataPoints) {
            int minDistCentroidId = dp.findMinDistIndex();
            dp.setCentroid(centroids.get(minDistCentroidId));
            centroids.get(minDistCentroidId).getPoints().add(dp);
        }

    }

    /**
     * Oblicza nowe srodki skupien
     */
    public void choseNewCentroids() {
        for (Centroid c : centroids) {
            double sumX = 0.0, sumY = 0.0;
            int countXs = 0, countYs = 0;
            for (DataPoint dp : dataPoints) {
                if (dp.getCentroid().equals(c)) {
                    sumX += dp.getX();
                    sumY += dp.getY();
                    countXs++;
                    countYs++;
                }
            }
            c.setX(sumX / countXs);
            c.setY(sumY / countYs);
        }
    }

    /**
     * Przypisuje centroidy i punkty do grup. Taki podzial umozliwi latwiejsze zapisywanie danych do pliku
     */
    public void assignElementsToGroups() {
        List<Group> groups = new ArrayList<>();
        for (Centroid c : centroids) {
            Group group = new Group();
            group.setCentroid(c);
            List<DataPoint> dataPoints = new ArrayList<>();
            for (DataPoint dp : dataPoints) {
                if (dp.getCentroid().equals(c)) {
                    dataPoints.add(dp);
                }
            }
            group.setDataPoints(dataPoints);
            groups.add(group);
        }
        this.setGroups(groups);
    }

    public double countErrors() {
        double sumDist = 0.0;
        int count = 0;
        for (Centroid c : this.getCentroids()) {
            for (DataPoint dp : c.getPoints()) {
                sumDist += pointToCentroidDistance(dp, c);
                count++;
            }
        }
        return sumDist / count;
    }

    public List<Centroid> getCentroids() {
        return centroids;
    }

    public void setCentroids(List<Centroid> centroids) {
        this.centroids = centroids;
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public int getNumberOfCentroids() {
        return numberOfCentroids;
    }

    public void setNumberOfCentroids(int numberOfCentroids) {
        this.numberOfCentroids = numberOfCentroids;
    }
}
