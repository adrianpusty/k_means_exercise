/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileUtils;

import model.DataPoint;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Adrian
 */
public class FileReader {

    /**
     * Wczytuje wspolrzedne z pliku i kazda pare x i y zapisuje w postaci obiektu klasy DataPoint
     * @param fileName
     * @return
     */
    public List<DataPoint> readFromFile(String fileName) {
        String line;
        List<DataPoint> fileContent = new ArrayList<>();
        try {
            InputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);

            while ((line = br.readLine()) != null) {
                List<String> temporaryStringList = Arrays.asList(line.split(","));

                DataPoint singlePoint = new DataPoint();
                singlePoint.setX(Double.parseDouble(temporaryStringList.get(0)));
                singlePoint.setY(Double.parseDouble(temporaryStringList.get(1)));
                fileContent.add(singlePoint);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }
}
