package org.firstinspires.ftc.teamcode.Utils.GUI;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Arrays;
import java.util.Random;

public class RoDisplay extends RoGraphic {

    String[] display;
    int sizeX = 0;
    int sizeY = 0;
    Telemetry tem;
    Random rand = new Random();

    public RoDisplay(int sizeX, int sizeY, Telemetry tem) {
        this.tem = tem;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        display = new String[sizeX*sizeY];
        Arrays.fill(display, "      ");
    }

    @Override
    public Object getOutput() {
        return null;
    }

    @Override
    public void setOutput(Object o) {

    }

    public void clear() {
        Arrays.fill(display, "      ");
    }

    public void setPixel(int x, int y, String d) {
        display[x+(y*sizeX)] = d;
    }
    public void setPixel(int x, int y, boolean b) {
        if(b) {
            display[x+(y*sizeX)] = "██";
        } else {
            display[x+(y*sizeX)] = "      ";
        }
    }

    @Override
    public void interactRight() {

    }

    @Override
    public void interactLeft() {

    }

    @Override
    public void interactUp() {

    }

    @Override
    public void interactDown() {

    }

    @Override
    public void interact() {
        int x = rand.nextInt(sizeX);
        int y = rand.nextInt(display.length/sizeX);
        this.setPixel(x,y,true);
    }

    @Override
    public void render(boolean selected) {
        for(int line = 0; line < sizeY; line++) {
            String lineString = "";
            for (int i = line*sizeX; i < sizeX+(line*sizeX); i++) {
                lineString += display[i];
            }
            tem.addLine(lineString);
        }
    }

    @Override
    public boolean takeControls() {
        return false;
    }
}
