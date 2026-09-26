package org.firstinspires.ftc.teamcode.NueralNetworks.Classes;

import java.io.File;
import java.io.FileInputStream;

public class NNFileReader {

    public static double threeByteToDouble(byte[] b) {
        return b[0]+128+((b[1]+128)*256)+((b[2]+128)*65536);
    }

    public static Network read(File f) {
        try {
            FileInputStream fI = new FileInputStream(f);
            byte[] data = new byte[Math.toIntExact(f.length())];
            fI.read(data);
        }catch(Exception ignored) {}
        return new Network(0,0,0,0);
    }

}
