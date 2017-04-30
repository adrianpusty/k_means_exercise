/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import fileUtils.FileRead;

import java.util.List;

import model.DataPoint;
import org.junit.Test;

/**
 * @author Adrian
 */
public class FileReadTests {
    @Test
    public void testFileReader() {
        FileRead fr = new FileRead();
        List<DataPoint> dataPoints = fr.readFromFile("shortened.txt");
        for(DataPoint dataPoint : dataPoints) {
            System.out.println(dataPoint.getX() + " " + dataPoint.getY());
        }
    }
}
