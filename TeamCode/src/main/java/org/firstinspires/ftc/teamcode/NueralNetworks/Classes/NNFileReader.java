package org.firstinspires.ftc.teamcode.NueralNetworks.Classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class NNFileReader {

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
        try {
            FileInputStream fI = new FileInputStream(f);
            byte[] data = new byte[Math.toIntExact(f.length())];
            fI.read(data);
            boolean back = data[0]/2 == data[0]/2.0f;
            Neuron.ActivationFunctionNueralNetwork activation;
            switch(data[0]/2) {
                case(0): activation = Neuron.ActivationFunctionNueralNetwork.Linear;
                case(1): activation = Neuron.ActivationFunctionNueralNetwork.RELU;
                case(2): activation = Neuron.ActivationFunctionNueralNetwork.Sigmoid;
                default: activation = Neuron.ActivationFunctionNueralNetwork.Linear;
            }
            Layer[] layers = new Layer[data[1]];
            int[] lS = new int[data[1]];
            for(int i = 0; i < data[1]*2; i+=2) {
                lS[i/2] = twoByteToInt(new byte[] {data[2+i],data[3+i]});
            }
            int read = (data[1]*2)+2;
            for(int i = 0; i < lS.length; i++) {
                Neuron[] neurons = new Neuron[lS[i]];
                for(int a = 0; a < lS[i]; a++) {
                    byte[] temp = new byte[3];
                    System.arraycopy(data,read,temp,0,3);
                    double bias = threeByteToDouble(temp);
                    read += 3;
                    int s = i+1<lS.length ? lS[i+1] : 0;
                    double[] weights = new double[s];
                    for(int b = 0; b < weights.length; b++) {
                        System.arraycopy(data,read,temp,0,3);
                        weights[b] = threeByteToDouble(temp);
                        read += 3;
                    }
                    neurons[a] = new Neuron(weights,bias,activation);
                }
                int s = i==0 ? neurons.length : lS[i-1];
                layers[i] = new Layer(s,neurons);
            }
            return new Network(layers);
        }catch(Exception ignored) {}
        return new Network(0,0,0,0);
    }

    public static byte[] writeB(Network n) {
        ArrayList<Byte> out = new ArrayList<>();
        switch (n.getLayers()[0].GetNeurons()[0].activationFunctionNueralNetwork){
            case Linear: out.add((byte) 0);
            case RELU: out.add((byte) 2);
            case Sigmoid: out.add((byte) 4);
        }
        out.add((byte) n.getLayers().length);
        for(int i = 0; i < n.getLayers().length; i++) {
            int l = n.getLayers()[i].GetNeurons().length;
            out.add((byte) ((l%256)-128));
            out.add((byte) ((l/256)-128));
        }
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
            if (f.createNewFile()) {
                FileOutputStream fO = new FileOutputStream(f);
                fO.write(b);
                fO.close();
            }
        } catch (Exception ignored) {}
    }

}
