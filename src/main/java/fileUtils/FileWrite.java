/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileUtils;

import logic.DataSpace;
import model.Centroid;
import model.DataPoint;

import java.io.*;
import java.util.List;

/**
 * @author Adrian
 */
public class FileWrite {
    public void writeGroups(DataSpace dataSpace, String outputName) throws IOException {
        File fout = new File(outputName);
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        int i = 0;

        for(Centroid c : dataSpace.getCentroids()) {
            //Zapisuje do pliku dane centroidu
            bw.write(c.getX() + getCommas(2 * i + 1) + c.getY());
            bw.newLine();

            for(DataPoint dp : c.getPoints()) {
                //Zapisuje do pliku dane punktu
                bw.write(dp.getX() + getCommas(2 * i + 2) + dp.getY());
                bw.newLine();
            }
            i++;
        }
        bw.close();
    }

    public void writeStats(DataSpace dataSpace, String outputName) throws IOException {
        File fout = new File(outputName);
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        int numberOfPoints = dataSpace.getDataPoints().size();
        int avgPointsPerCentroid = numberOfPoints / dataSpace.getNumberOfCentroids();
        double sum = 0.0;

        for(Centroid c : dataSpace.getCentroids()) {
            sum += Math.abs(c.getPoints().size() - avgPointsPerCentroid);
            bw.write("Centroid: [" + c.getX() + ", " + c.getY() + "] liczba punktow: " + c.getPoints().size());
            bw.newLine();
        }
        bw.newLine();
        bw.write("o ile rozni sie przecietna ilosc punktow dla centroidu od sredniej: " + (sum / dataSpace.getNumberOfCentroids()));
        bw.close();
    }

    public void writeQuantErrors(List<Double> quantErros, String fileName) throws IOException {
        File fout = new File(fileName);
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for(double d : quantErros) {
            String valueOfD = String.valueOf(d);
            bw.write(valueOfD);
            bw.newLine();
        }
        bw.close();
    }

    public String getCommas(int howMany) {
        String commas = "";
        for (int i = 0; i < howMany; i++) {
            commas += ",";
        }
        return commas;
    }
}
