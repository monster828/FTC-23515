package org.firstinspires.ftc.teamcode.Utils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Logger {

    String filePath = MiscUtils.dataFolder;

    public Logger(String filePath) {
        this.filePath = MiscUtils.dataFolder+filePath;
    }

    public void write(String label, byte data) {
        byte[] file = MiscUtils.readFile(filePath);
        ArrayList<Byte> dataList = new ArrayList<Byte>();
        for(byte f : file) dataList.add(f);
        dataList.add(data);
        byte[] labelB = label.getBytes(StandardCharsets.UTF_8);
        for(byte b : labelB) dataList.add(b);
        dataList.add((byte) 0b1111111);
        byte[] out = new byte[dataList.size()];
        for(int i = 0; i < out.length; i++) {
            out[i] = dataList.get(0);
            dataList.remove(0);
        }
        MiscUtils.writeFile(filePath,out);
    }
    public void reset() {
        MiscUtils.writeFile(filePath,new byte[] {});
    }
    public String[] readLabels() {
        byte[] file = MiscUtils.readFile(filePath);
        int i = 1;
        ArrayList<String> out = new ArrayList<>();
        while(i < file.length) {
            StringBuilder builder = new StringBuilder();
            if(file[i] != (byte)0b1111111) {
                builder.append(new String(new byte[] {file[i]},StandardCharsets.UTF_8));
                i++;
            } else {
                i += 2;
                out.add(builder.toString());
                builder = new StringBuilder();
            }
        }
        return out.toArray(new String[0]);
    }
    public ArrayList<String> readLabelsList() {
        byte[] file = MiscUtils.readFile(filePath);
        int i = 1;
        ArrayList<String> out = new ArrayList<>();
        while(i < file.length) {
            StringBuilder builder = new StringBuilder();
            if(file[i] != (byte)0b1111111) {
                builder.append(new String(new byte[] {file[i]},StandardCharsets.UTF_8));
                i++;
            } else {
                i += 2;
                out.add(builder.toString());
                builder = new StringBuilder();
            }
        }
        return out;
    }
    public byte[] readData() {
        byte[] file = MiscUtils.readFile(filePath);
        int i = 1;
        ArrayList<Byte> data = new ArrayList<>();
        data.add(file[0]);
        while(i < file.length) {
            if(file[i] != (byte)0b1111111) {
                i++;
            } else {
                i++;
                if(i < file.length) {
                    data.add(file[i]);
                }
                i++;
            }
        }
        byte[] out = new byte[data.size()];
        for(i = 0; i < out.length; i++) {
           out[i] = data.get(0);
           data.remove(0);
        }
        return out;
    }

}
