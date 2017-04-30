package logic;

import fileUtils.FileRead;
import fileUtils.FileWrite;
import model.Centroid;
import model.DataPoint;

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
    private int numberOfCentroids;
    String method;

    public void kMeanProcess(int numberOfIterations) throws IOException {
        List<Double> quantError = new ArrayList<>();

        FileWrite fw = new FileWrite();
        fw.writeGroups(this, "Xout1.csv");
        fw.writeStats(this, "Xstat1.txt");

        for (int i = 0; i < numberOfIterations; i++) {
            choseNewCentroids();
            setAffiliations();

            quantError.add(countQuantization());
        }

        fw.writeGroups(this, "Xout2.csv");
        fw.writeStats(this, "Xstat2.txt");
        fw.writeQuantErrors(quantError, "XqErrors2.txt");
        System.out.println("Wykonano " + numberOfIterations + " iteracji");
    }

    public void kMeanProcess() throws IOException {
        List<Double> quantError = new ArrayList<>();
        double currentCountQuant = 1;
        double previouseCountQuant = 0;
        int counter = 0;

        FileWrite fw = new FileWrite();
        fw.writeGroups(this, "Xout1.csv");
        fw.writeStats(this, "Xstat1.txt");

        while (Math.abs(currentCountQuant - previouseCountQuant) > 0) {
            choseNewCentroids();
            setAffiliations();

            previouseCountQuant = currentCountQuant;
            currentCountQuant = countQuantization();
            quantError.add(currentCountQuant);

            counter++;
        }
        fw.writeGroups(this, "Xout2.csv");
        fw.writeStats(this, "Xstat2.txt");
        fw.writeQuantErrors(quantError, "XqErrors2.txt");
        System.out.println("Wykonano " + counter + " iteracji");
    }

    public DataSpace() {

    }

    public DataSpace(String fileName, int numberOfCentroids) {
        FileRead fileRead = new FileRead();
        this.dataPoints = fileRead.readFromFile(fileName);
        this.centroids = setRandomCentroids(dataPoints, numberOfCentroids);
        this.numberOfCentroids = numberOfCentroids;
        setAffiliations();
    }

    /**
     * Uzywajac tego konstruktora trzeba pamietac o ustawieniu centroidow poczatkowych
     * @param fileName
     */
    public DataSpace(String fileName, List<Centroid> centroids) {
        FileRead fileRead = new FileRead();
        this.dataPoints = fileRead.readFromFile(fileName);
        this.setCentroids(centroids);
        this.numberOfCentroids = numberOfCentroids;
        setAffiliations();
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

            for (DataPoint dp : c.getPoints()) {
                    sumX += dp.getX();
                    sumY += dp.getY();
                    countXs++;
                    countYs++;
            }
            c.setX(sumX / countXs);
            c.setY(sumY / countYs);
        }
    }

    /**
     * Przypisuje centroidy i punkty do grup. Taki podzial umozliwi latwiejsze zapisywanie danych do pliku
     */
//    public void assignElementsToGroups() {
//        List<Group> groups = new ArrayList<>();
//        for (Centroid c : centroids) {
//            Group group = new Group();
//            group.setCentroid(c);
//            List<DataPoint> dataPoints = new ArrayList<>();
//            for (DataPoint dp : dataPoints) {
//                if (dp.getCentroid().equals(c)) {
//                    dataPoints.add(dp);
//                }
//            }
//            group.setDataPoints(dataPoints);
//            groups.add(group);
//        }
//        this.setGroups(groups);
//    }
    public double countQuantization() {
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
        this.setNumberOfCentroids(centroids.size());
    }

    public List<DataPoint> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<DataPoint> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public int getNumberOfCentroids() {
        return numberOfCentroids;
    }

    public void setNumberOfCentroids(int numberOfCentroids) {
        this.numberOfCentroids = numberOfCentroids;
    }
}
