package org.firstinspires.ftc.teamcode.NueralNetworks.Classes;

import java.io.File;
import java.io.FileInputStream;

public class NNFileReader {

    public static double threeByteToDouble(byte[] b) {
        return ((b[0]+128+((b[1]+128)*256)+((b[2]+128)*65536))/838860.75)-10;
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

}
