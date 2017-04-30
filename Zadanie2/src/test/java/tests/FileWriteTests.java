package tests;

import fileUtils.FileRead;
import fileUtils.FileWrite;
import logic.DataSpace;
import model.DataPoint;
import org.junit.Test;

import java.io.*;

/**
 * Created by Adrian on 2017-04-30.
 */
public class FileWriteTests {

    @Test
    public void getCommasTest() {
        FileWrite fileWrite = new FileWrite();
        String c = fileWrite.getCommas(5);
        System.out.println(c);
    }

    @Test
    public void getCommasTest2() throws IOException {
        FileWrite fw = new FileWrite();
        DataSpace ds = new DataSpace("attract.txt", 5);

        File fout = new File("podzialNaGrupy.txt");
        FileOutputStream fos = new FileOutputStream(fout);

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        for (int i = 0; i < ds.getDataPoints().size(); i++) {
            int group = (i % 5 + 1);
            bw.write(ds.getDataPoints().get(i).getX() + fw.getCommas(group) + ds.getDataPoints().get(i).getY());
            bw.newLine();
        }
    }
}

