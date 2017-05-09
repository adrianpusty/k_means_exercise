import logic.DataSpace;

import java.io.IOException;

/**
 * Created by Adrian on 2017-04-30.
 */
public class App {
    public static void main(String[] args) throws IOException {
        DataSpace dataSpace = new DataSpace("init.txt", 30, "RP");
        dataSpace.kMeanProcess();
    }
}
