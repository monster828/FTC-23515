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
            boolean back = data[0]/2 == data[0]/2.0f;
            Neuron.ActivationFunctionNueralNetwork activation;
            switch(data[0]/2) {
                case(0): activation = Neuron.ActivationFunctionNueralNetwork.Linear;
                case(1): activation = Neuron.ActivationFunctionNueralNetwork.RELU;
                case(2): activation = Neuron.ActivationFunctionNueralNetwork.Sigmoid;
                default: activation = Neuron.ActivationFunctionNueralNetwork.Linear;
            }
            Layer[] layers = new Layer[data[1]];
            int nL = 0;
            int i;
            for(i = 0; i < data[1]; i++) {
                nL += data[3+i];
            }
            Neuron[] neurons = new Neuron[nL];
            for(i = i; i < data.length; i++) {

            }
        }catch(Exception ignored) {}
        return new Network(0,0,0,0);
    }

}
