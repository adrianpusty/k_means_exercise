/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileUtils;

import logic.DataSpace;
import model.Centroid;
import model.DataPoint;
import model.Group;

import java.io.*;

/**
 * @author Adrian
 */
public class FileWrite {
    public void writeGroups(DataSpace dataSpace, String outputName) throws IOException {
        File fout = new File(outputName);
        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for(int i = 0; i < dataSpace.getCentroids().size(); i++) {
            //Zapisuje do pliku dane centroidu
            bw.write(dataSpace.getCentroids().get(i).getX() + getCommas(2 * i + 1) + dataSpace.getCentroids().get(i).getY());
            bw.newLine();
            for(DataPoint dp : dataSpace.getCentroids().get(i).getPoints()) {
                //Zapisuje do pliku dane punktu
                bw.write(dp.getX() + getCommas(2 * i + 2) + dp.getY());
                bw.newLine();
            }
        }
    }

    public String getCommas(int howMany) {
        String commas = "";
        for (int i = 0; i < howMany; i++) {
            commas += ",";
        }
        return commas;
    }
}
