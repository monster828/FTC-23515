package org.firstinspires.ftc.teamcode.NueralNetworks.Classes;

public class Network {
    private int _initialDimentionSize;
    private int _hiddenLayers;
    private int _hiddenLayersSize;
    private int _actionLayer;


    public Network(int initialDimentionSize, int hiddenLayers, int hiddenLayersSize, int actionLayer){
        _initialDimentionSize = initialDimentionSize;
        _hiddenLayers = hiddenLayers;
        _hiddenLayersSize = hiddenLayersSize;
        _actionLayer = actionLayer;
    }



}
