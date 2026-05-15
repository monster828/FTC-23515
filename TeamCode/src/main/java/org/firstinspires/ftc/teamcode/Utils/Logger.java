package org.firstinspires.ftc.teamcode.Utils;

import java.util.Arrays;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class Logger {

    /*
    LOG FILE:
    [LABEL]10000000[DATA]10000000[TIME 16b2B]
     */

    File f;
    long start;

    public Logger(File file) {
        f = file;
        start = System.currentTimeMillis();
        try {
            f.delete();
            f.createNewFile();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DATA CANNOT CONTAIN 10000000
     * @param name label for the data
     * @param data the data
     */
    public void add(String name, byte[] data) {
        byte[] in;
        if(f.exists()) {
            in = new byte[Math.toIntExact(f.length())];
            try {
                FileInputStream i = new FileInputStream(f);
                i.read(in);
                i.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            in = new byte[0];
        }

        byte[] out = new byte[in.length+name.length()+ data.length+5];
        for(int i = 0; i < in.length; i++) {
            out[i] = in[i];
        }
        int i = in.length;

        byte[] a = name.getBytes();
        for(byte b : a) {
            out[i] = b;i++;
        }
        out[i] = (byte) -128;i++;
        for (byte b : data) {
            out[i] = b;i++;
        }
        out[i] = (byte) -128;i++;
        out[i] = (byte)(((System.currentTimeMillis()-start)%256)-128);i++;
        out[i] = (byte)(Math.floor(((double) (System.currentTimeMillis() - start) /256))-128);
        try {
            FileOutputStream o = new FileOutputStream(f);
            o.write(out);
            o.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * reads the log
     * @return an Object array (out). out[x][0] = the name (String), out[x][1] = the data (byte[]), out[x][2] = the timestamp for the data (long)
     */
    public Object[][] read() {
        ArrayList<Object[]> o = new ArrayList<>();
        byte[] in = new byte[Math.toIntExact(f.length())];
        try {
            FileInputStream i = new FileInputStream(f);
            i.read(in);
            i.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int i = 0;
        while(i < in.length-1) {
            Object[] oh = new Object[3];
            StringBuilder s = new StringBuilder();
            while(in[i] != -128) {
                s.append(new String(new byte[]{in[i]}));
                i++;
            }
            i++;
            //System.out.println("String read");
            oh[0] = s.toString();
            ArrayList<Byte> b = new ArrayList<>();
            while(in[i] != -128) {
                b.add(in[i]);
                i++;
            }
            byte[] b2 = new byte[b.size()];
            for(int a = 0; a < b.size(); a++) {
                b2[a] = b.get(a);
            }
            oh[1] = b2;
            long time = 0;
            time += in[i+1]+128;
            time += (in[i+2]+128)*128;
            i += 4;
            oh[2] = time;
            o.add(oh);
        }
        return o.toArray(new Object[][]{});
    }

}
