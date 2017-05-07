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
    private String method;

    /**
     * Kolejne kroki obliczeniowe dla zadanej ilosci iteracji
     *
     * @param numberOfIterations
     * @throws IOException
     */
    public void kMeanProcess(int numberOfIterations) throws IOException {
        List<Double> quantError = new ArrayList<>();

        FileWrite fw = new FileWrite();

        for (int i = 0; i < numberOfIterations; i++) {
            choseNewCentroids();
            
            if (i == 0) {
                fw.writeGroups(this, "Xout1" + this.method + ".csv");
                fw.writeStats(this, "Xstat1" + this.method + ".txt");
            }
            
            setAffiliations();

            quantError.add(countQuantization());
        }

        fw.writeGroups(this, "Xout2" + this.method + ".csv");
        fw.writeStats(this, "Xstat2" + this.method + ".txt");
        fw.writeQuantErrors(quantError, "XqErrors2" + this.method + ".txt");
        System.out.println("Wykonano " + numberOfIterations + " iteracji");
    }

    /**
     * Kolejne kroki obliczeniowe do osiagniecia zadanej dokladnosci (Jesli
     * miedzy dwoma kolejnymi iteracjami blad kwantyzacji nie zmieni sie to
     * przerywamy petle)
     *
     * @throws IOException
     */
    public void kMeanProcess() throws IOException {
        List<Double> quantError = new ArrayList<>();
        double currentCountQuant = 1;
        double previouseCountQuant = 0;
        int counter = 0;

        FileWrite fw = new FileWrite();

        while (Math.abs(currentCountQuant - previouseCountQuant) > 0) {
            choseNewCentroids();
            
            if (counter == 0) {
                fw.writeGroups(this, "Xout1" + this.method + ".csv");
                fw.writeStats(this, "Xstat1" + this.method + ".txt");
            }
            
            setAffiliations();

            previouseCountQuant = currentCountQuant;
            currentCountQuant = countQuantization();
            quantError.add(currentCountQuant);

            counter++;
        }
        fw.writeGroups(this, "Xout2" + this.method + ".csv");
        fw.writeStats(this, "Xstat2" + this.method + ".txt");
        fw.writeQuantErrors(quantError, "XqErrors2" + this.method + ".txt");
        System.out.println("Wykonano " + counter + " iteracji");
    }

    public DataSpace() {

    }

    /**
     * Konstruktor uwzgledniajacy wybor Forgy badz Random Partition
     *
     * @param fileName
     * @param numberOfCentroids
     */
    public DataSpace(String fileName, int numberOfCentroids, String method) {
        FileRead fileRead = new FileRead();
        this.dataPoints = fileRead.readFromFile(fileName);
        this.numberOfCentroids = numberOfCentroids;
        this.method = method;
        if (method == "F") {
            this.centroids = forgyRandomCentroids(dataPoints, numberOfCentroids);
            setAffiliations();
        } else if (method == "RP") {
            this.centroids = randomPartition(numberOfCentroids);
        }
    }

    /**
     * KONSTRUKTOR DO CELOW TESTOWYCH.
     *
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
     * Z zadanego zbioru punktow wybiera losowo centroidy (ich liczba okreslona
     * przez parametr numberOfCentroids) Uzywana na poczatku dzialania algorytmu
     * w celu ustalenia poczatkowych centroidow
     *
     * @param dataPoints
     * @param numberOfCentroids
     * @return
     */
    public List<Centroid> forgyRandomCentroids(List<DataPoint> dataPoints, int numberOfCentroids) {
        List<Centroid> initialCentroids = new ArrayList<Centroid>();
        for (int i = 0; i < numberOfCentroids; i++) {
            int randomRow = ThreadLocalRandom.current().nextInt(0, dataPoints.size());
            initialCentroids.add(dataPoints.get(randomRow).toCentroid());
        }
        return initialCentroids;
    }

    /**
     * Tworzy okreslona liczbe centroidow i kazdy punkt ze zbioru punktow
     * przydziela losowo do jednego z centroidow
     *
     * @param numberOfCentroids
     */
    public List<Centroid> randomPartition(int numberOfCentroids) {
        List<Centroid> initialCentroids = new ArrayList<Centroid>();
        for (int i = 0; i < numberOfCentroids; i++) {

            Centroid centroid = new Centroid();
            centroid.setPoints(new ArrayList<>());
            initialCentroids.add(centroid);
        }
        for (DataPoint dp : this.dataPoints) {
            int randomRow = ThreadLocalRandom.current().nextInt(0, (numberOfCentroids - 1));
            initialCentroids.get(randomRow).getPoints().add(dp);
        }

        return initialCentroids;
    }

    /**
     * Oblicza odleglosc miedzy danym punktem w przestrzeni dwuwymiarowej a
     * jednym z centroidow
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
     * Dla kazdego punktu wybiera najmniejsza odleglosc i ustawia centroid
     * odpowiadajacy tej odleglosci Dla kazdego centroidu przypisuje punkt mial
     * najblizej do tego centroidu
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
     * Przypisuje centroidy i punkty do grup. Taki podzial umozliwi latwiejsze
     * zapisywanie danych do pliku
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
    /**
     * Dla wszystkich centroidow i nalezacych do nich punktow oblicza odleglosc
     * punktow od centroidu i oblicza srednia.
     *
     * @return
     */
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
