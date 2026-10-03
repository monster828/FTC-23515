package org.firstinspires.ftc.teamcode.NueralNetworks.Classes;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utils.MiscUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class NNFileReader {

    public static Telemetry telemetry;

    public static double threeByteToDouble(byte[] b) {
        return ((b[0]+128+((b[1]+128)*256)+((b[2]+128)*65536))/838860.75)-10;
    }

    public static Byte[] doubleToThreeByte(double d) {
        Byte[] out = new Byte[3];
        int i = (int) ((d+10)*838860.75);
        out[0] = (byte) ((i%256)-128);
        out[1] = (byte) (((i%65536)/256)-128);
        out[2] = (byte) ((i/65536)-128);
        return out;
    }

    public static int twoByteToInt(byte[] b) {
        return b[0]+128+((b[1]+128)*256);
    }

    public static Network read(File f) {
        int ins = 0;
        Layer[] layers = new Layer[0];
        try {
            FileInputStream fI = new FileInputStream(f);
            byte[] data = new byte[Math.toIntExact(f.length())];
            fI.read(data);
            boolean back = data[0]/2 == data[0]/2.0f;
            Neuron.ActivationFunctionNueralNetwork activation;
            switch(data[0]/2) {
                case(0): activation = Neuron.ActivationFunctionNueralNetwork.Linear; break;
                case(1): activation = Neuron.ActivationFunctionNueralNetwork.RELU; break;
                case(2): activation = Neuron.ActivationFunctionNueralNetwork.Sigmoid; break;
                default: activation = Neuron.ActivationFunctionNueralNetwork.Linear;
            }
            ins = twoByteToInt(new byte[]{data[1],data[2]});
            layers = new Layer[data[3]];
            int[] lS = new int[data[3]];
            for(int i = 0; i < data[3]*2; i+=2) {
                lS[i/2] = twoByteToInt(new byte[] {data[4+i],data[5+i]});
            }
            int read = (data[3]*2)+4;
            for(int i = 0; i < lS.length; i++) {
                Neuron[] neurons = new Neuron[lS[i]];
                int s = i==0 ? ins : lS[i-1];
                for(int a = 0; a < lS[i]; a++) {
                    byte[] temp = new byte[3];
                    System.arraycopy(data,read,temp,0,3);
                    double bias = threeByteToDouble(temp);
                    read += 3;
                    //int s = i+1<lS.length ? lS[i+1] : 0;
                    double[] weights = new double[s];
                    for(int b = 0; b < weights.length; b++) {
                        System.arraycopy(data,read,temp,0,3);
                        weights[b] = threeByteToDouble(temp);
                        read += 3;
                    }
                    neurons[a] = new Neuron(weights,bias,activation);
                }
                layers[i] = new Layer(s,neurons);
            }
            return new Network(layers);
        }catch(Exception e) {
            if(telemetry != null) {
                telemetry.addData("Error",e.toString());
                StackTraceElement[] St = e.getStackTrace();
                for(int i = 0; i < St.length; i++) telemetry.addLine(St[i].toString());
                telemetry.addData("Layers",Arrays.toString(layers));
                telemetry.addData("Inputs",ins);
                telemetry.update();
            }
        }
        return new Network(0,0,0,0);
    }

    public static byte[] writeB(Network n) {
        ArrayList<Byte> out = new ArrayList<>();
        switch (n.getLayers()[0].GetNeurons()[0].activationFunctionNueralNetwork){
            case Linear: out.add((byte) 0); break;
            case RELU: out.add((byte) 2); break;
            case Sigmoid: out.add((byte) 4); break;
        }
        int iS = n.getLayers()[0].GetNeurons()[0].GetWeights().length;
        out.add((byte) ((iS%256)-128));
        out.add((byte) ((iS/256)-128));
        out.add((byte) n.getLayers().length);
        for(int i = 0; i < n.getLayers().length; i++) {
            int l = n.getLayers()[i].GetNeurons().length;
            if(telemetry != null) telemetry.addLine(String.valueOf(l));
            out.add((byte) ((l%256)-128));
            out.add((byte) ((l/256)-128));
        }
        if(telemetry != null) telemetry.update();
        for(int i = 0; i < n.getLayers().length; i++) {
            int l = n.getLayers()[i].GetNeurons().length;
            for(int a = 0; a < l; a++) {
                out.addAll(Arrays.asList(doubleToThreeByte(n.getLayers()[i].GetNeurons()[a].GetBias())));
                for(int b = 0; b < n.getLayers()[i].GetNeurons()[a].GetWeights().length; b++) {
                    out.addAll(Arrays.asList(doubleToThreeByte(n.getLayers()[i].GetNeurons()[a].GetWeights()[b])));
                }
            }
        }
        byte[] out2 = new byte[out.size()];
        for(int i = 0; i < out2.length; i++) out2[i] = out.get(i);
        return out2;
    }

    public static void write(Network n, File f) {
        byte[] b = writeB(n);
        try {
            if(f.exists()) f.delete();
            if (f.createNewFile()) {
                FileOutputStream fO = new FileOutputStream(f);
                fO.write(b);
                fO.close();
            }
        } catch (Exception ignored) {}
    }

    /**
     * Saves a NN to the data folder
     * @param n the network to save
     * @param name name of the network
     */
    public static void writeC(Network n, String name) {
        String name2 = name;
        if(!name.contains(".")) name2 = name2 + ".nn";
        File f = new File(MiscUtils.dataFolder+"/"+name2);
        write(n,f);
    }

    /**
     * Reads NN from the data folder
     * @param name name of the network
     * @return the network
     */
    public static Network readC(String name) {
        String name2 = name;
        if(!name.contains(".")) name2 = name2 + ".nn";
        File f = new File(MiscUtils.dataFolder+"/"+name2);
        return read(f);
    }

}
