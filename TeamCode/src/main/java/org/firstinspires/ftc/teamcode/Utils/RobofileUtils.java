package org.firstinspires.ftc.teamcode.Utils;

import org.firstinspires.ftc.teamcode.Utils.Movement.Position;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class RobofileUtils {

    /** .ROBOPATH V1 format
     * [VERSION 1 (8b/1B)][(No -B)START TIME (16b/2B)] 3B
     *
     * Points:
     * [POINT TYPE (4b)]
     * Basic:      [0000][X*4 (10b/1B2b)][Y*4 (10b/1B2b)][R*0.7 (8b/1B)][-128+TIME DIFF (8b/1B)] 5B
     * Keypoint:   [0001][X*4 (10b/1B2b)][Y*4 (10b/1B2b)][R*0.7 (8b/1B)][-128+TIME DIFF (8b/1B)][XV*4 (8b/1B)][YV*4 (8b/1B)] 7B
     * Stop:       [0010][X*4 (10b/1B2b)][Y*4 (10b/1B2b)][R*0.7 (8b/1B)][-128+TIME DIFF (8b/1B)] 5B
     * Pause:      [0011][X*4 (10b/1B2b)][Y*4 (10b/1B2b)][R*0.7 (8b/1B)][-128+TIME DIFF (8b/1B)][PAUSE (16b/2B)] 7B
     *
     */
    public static String v1doc = ".ROBOPATH V1 format\n" +
            "[VERSION 1 (8b/1B)][(No -B)START TIME (16b/2B)] 3B\n" +
            "\n" +
            "Points:\n" +
            "[POINT TYPE (4b)]\n" +
            "Basic:      [0000][X*4 (10b/1B2b)][Y*4 (10b/1B2b)][R*0.7 (8b/1B)][-128+TIME DIFF (8b/1B)] 5B \n" +
            "Keypoint:   [0001][X*4 (10b/1B2b)][Y*4 (10b/1B2b)][R*0.7 (8b/1B)][-128+TIME DIFF (8b/1B)][XV*2 (8b/1B)][YV*2 (8b/1B)] 7B\n" +
            "Stop:       [0010][X*4 (10b/1B2b)][Y*4 (10b/1B2b)][R*0.7 (8b/1B)][-128+TIME DIFF (8b/1B)] 5B\n" +
            "Pause:      [0011][X*4 (10b/1B2b)][Y*4 (10b/1B2b)][R*0.7 (8b/1B)][-128+TIME DIFF (8b/1B)][PAUSE (16b/2B)] 7B";

    /**
     * Write a Robopath Version 1
     * @param pos points along the route
     */
    public static byte[] writeRobopathV1(Position[] pos) {
        ArrayList<Byte> out0 = new ArrayList<>();
        long time = pos[0].getTimeStamp();
        out0.add((byte) 1);
        Byte time2 = (byte) (Math.floor((double) time /256));
        time -= time2;
        out0.add((byte) (-128+time2));
        out0.add((byte) (-128+time));
        time = pos[0].getTimeStamp();
        for(Position p : pos) {
            int type = p.getType();
            int[] split;
            Byte[] out00;
            if(type == 1) { //Keypoint
                out00 = new Byte[7];
                split = new int[out00.length*8];
                Arrays.fill(split,0,2,0);
                split[3] = 1;
            } else if(type == 2) { //Stop
                out00 = new Byte[5];
                split = new int[out00.length*8];
                Arrays.fill(split,0,1,0);
                split[2] = 1; split[3] = 0;
            } else if(type == 3) { //Pause
                out00 = new Byte[7];
                split = new int[out00.length*8];
                Arrays.fill(split,0,1,0);
                Arrays.fill(split,2,3,1);
            } else { //Basic
                out00 = new Byte[5];
                split = new int[out00.length*8];
                Arrays.fill(split,0,3,0);
            }

            int[] split2 = tenBitNumSplit((int) (p.x()*4));
            for(int i = 0; i < 10; i++) {
                split[i+4] = split2[i];
            }
            split2 = tenBitNumSplit((int) (p.y()*4));
            for(int i = 0; i < 10; i++) {
                split[i+14] = split2[i];
            }

            for(int i = 0; i < 3; i++) {
                byte idk = 0;
                int a = -128;
                for(int b = i*8; b < (i*8)+8; b++) {
                    idk += (byte) (split[b]*a);
                    a /= 2;
                    a = Math.abs(a);
                }
                out00[i] = idk;
            }
            out00[3] = (byte)(p.r()*0.7);
            out00[4] = (byte) (-128+(p.getTimeStamp()-time));
            if(type == 1) {
                out00[5] = (byte) (((float)p.getExtraData()[0])*4);
                out00[6] = (byte) (((float)p.getExtraData()[1])*4);
            } else if(type == 3) {
                long idk = (long)p.getExtraData()[0];
                out00[5] = (byte) Math.floor((double) idk /256);
                out00[6] = (byte) ((byte)-128+(idk-out00[4]));
                out00[5] = (byte) (-128+out00[4]);
            }
            Collections.addAll(out0, out00);
        }
        byte[] out = new byte[out0.size()];
        for(int i = 0; i < out.length; i++) {
            out[i] = out0.get(i);
        }
        return out;
    }

    public static int[] tenBitNumSplit(int num) {
        int[] out = new int[10];
        boolean n = false;
        if(num < 0) {
            n = true;
            out[0] = 1;
            num = Math.abs(num)-1;
        } else out[0] = 0;
        int a = 1;
        for(int i = 256; i >= 1; i /= 2) {
            if(n) {
                out[a] = -(Math.abs((int) Math.floor((double) num / i)) - 1);
            } else out[a] = Math.abs((int) Math.floor((double) num /i));
            num = num % i;
            if(out[a] == 2) out[a] = 0;
            a++;
        }
        /*int a = 0;
        for(int i = 512; i >= 1; i /= 2) {
            if(Math.abs(i) == 512) {
                out[a] = Math.abs((int) Math.ceil((double) num /-i));
                num = num % -i;
            } else {
                out[a] = Math.abs((int) Math.floor((double) num /i));
                num = num % i;
            }
            if(out[a] == 2) out[a] = 0;
            a++;
        }*/
        return out;
    }

    public static int[] eightBitNumSplit(byte num) {
        int[] out = new int[8];
        int a = 0;
        for(int i = 128; i >= 1; i /= 2) {
            if(Math.abs(i) == 128) {
                out[a] = Math.abs((int) Math.ceil((double) num /-i));
                num = (byte) (num % -i);
            } else {
                out[a] = Math.abs((int) Math.floor((double) num /i));
                num = (byte) (num % i);
            }
            if(out[a] == 2) out[a] = 0;
            a++;
        }
        return out;
    }

    /**Loads a Robopath Version 1 file
    @param in the bytes in the file
     **/
    public static Position[] loadRobopathV1(byte[] in) {
        if(in[0] != 1) {
            return null;
        }
        long time = (in[2]+128)+((in[1]+128)*257);
        System.out.println("Start time: "+time);
        ArrayList<Position> pos = new ArrayList<>();
        int scan = 3;
        while(scan < in.length) {
            int temp = (int) Math.floor((double) in[scan] /16);
            int type;
            if(temp >= 0) {
                type = temp;
            } else {
                type = temp+8;
            }
            System.out.println(type);
            Position p = new Position(0,0,0);
            /*int[] bitList = new int[10];
            byte idk;
            if(in[scan] % 16 > 0) {
                idk = (byte) (in[scan] % 16);
            } else {
                idk = (byte) ((byte) (in[scan] % 16)+16);
            }
            //System.out.println(idk);
            for(int scan2 = 0; scan2 < 4; scan2++) {
                bitList[scan2] = (int) (idk / Math.pow(2,3-scan2));
                idk = (byte) (idk % Math.pow(2,3-scan2));
            }
            idk = (byte) (in[scan+1]+128);
            for(int scan2 = 4; scan2 < 10; scan2++) {
                int sub = (int) (idk / Math.pow(2,11-scan2));
                bitList[scan2] = sub;
                idk = (byte) (idk % Math.pow(2,11-scan2));
            }
            System.out.println(Arrays.toString(bitList));*/
            int[] bytes = new int[20];
            int[] b8 = eightBitNumSplit(in[scan]);
            for(int i = 0; i < 4; i++) {
                bytes[i] = b8[i+4];
            }
            b8 = eightBitNumSplit(in[scan+1]);
            for(int i = 0; i < 8; i++) {
                bytes[i+4] = b8[i];
            }
            b8 = eightBitNumSplit(in[scan+2]);
            for(int i = 0; i < 8; i++) {
                bytes[i+12] = b8[i];
            }
            System.out.println(Arrays.toString(bytes));
            float x = -128*bytes[0];
            for(int i = 1; i < 10; i++) {
                x += (float) (bytes[i] * Math.pow(2,7-i));
            }
            p.setX(x);
            System.out.println(x);
            float y = -128*bytes[10];
            for(int i = 1; i < 10; i++) {
                y += (float) (bytes[i+10] * Math.pow(2,7-i));
            }
            p.setY(y);
            System.out.println(y);
            float r = in[scan+3]/0.7f;
            p.setR(r);
            System.out.println(r);
            int timeDiff = 128+in[scan+4];
            time += timeDiff;
            p.setTimeStamp(time);
            scan += 5;

            //THE FUN BIT
            p.setType(type,null);
            if(type == 1) {
                p.setType(1,new Object[] {(float)in[scan]/4.0f,(float)in[scan+1]/4.0f});
            } else if(type == 3) {
                p.setType(3,new Object[] {(long)(in[scan+1]+128+((in[scan]+128)*257))});
            }
            if(type == 1 || type == 3) scan += 2;
            pos.add(p);
        }
        return pos.toArray(new Position[0]);
    }

}
