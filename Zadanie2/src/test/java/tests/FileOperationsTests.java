/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import fileUtils.FileReader;

import java.util.List;

import model.DataPoint;
import org.junit.Test;

/**
 * @author Adrian
 */
public class FileOperationsTests {
    @Test
    public void testFileReader() {
        FileReader fr = new FileReader();
        List<DataPoint> dataPoints = fr.readFromFile("shortened.txt");
        for(DataPoint dataPoint : dataPoints) {
            System.out.println(dataPoint.getX() + " " + dataPoint.getY());
        }
    }
}
