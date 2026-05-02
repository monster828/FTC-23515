package org.firstinspires.ftc.teamcode.Utils;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;

public class ColorSensorPair implements ColorSensor {


    /*
    Credits to Alden Shurtz for this idea
     */

    ColorSensor c1;
    ColorSensor c2;

    public ColorSensorPair(ColorSensor c1, ColorSensor c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    public int red() {
        if(c1.alpha() > c2.alpha()) {
            return c1.red();
        } else {
            return c2.red();
        }
    }

    @Override
    public int green() {
        if(c1.alpha() > c2.alpha()) {
            return c1.green();
        } else {
            return c2.green();
        }
    }

    @Override
    public int blue() {
        if(c1.alpha() > c2.alpha()) {
            return c1.blue();
        } else {
            return c2.blue();
        }
    }

    @Override
    public int alpha() {
        if(c1.alpha() > c2.alpha()) {
            return c1.alpha();
        } else {
            return c2.alpha();
        }
    }

    @Override
    public int argb() {
        return 0;
    }

    @Override
    public void enableLed(boolean enable) {

    }

    @Override
    public void setI2cAddress(I2cAddr newAddress) {

    }

    @Override
    public I2cAddr getI2cAddress() {
        return null;
    }

    @Override
    public Manufacturer getManufacturer() {
        return null;
    }

    @Override
    public String getDeviceName() {
        return "";
    }

    @Override
    public String getConnectionInfo() {
        return "";
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {

    }

    @Override
    public void close() {

    }
}
