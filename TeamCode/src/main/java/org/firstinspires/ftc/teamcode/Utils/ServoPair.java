package org.firstinspires.ftc.teamcode.Utils;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

public class ServoPair implements Servo {

    Servo s1;
    Servo s2;
    public ServoPair(Servo s1, Servo s2) {
        this.s1 = s1;
        this.s2 = s2;
    }

    @Override
    public ServoController getController() {
        return null;
    }

    @Override
    public int getPortNumber() {
        return 0;
    }

    @Override
    public void setDirection(Direction direction) {
        s1.setDirection(direction);
        s2.setDirection(direction);
    }
    @Override
    public Direction getDirection() {
        return s1.getDirection();
    }

    @Override
    public void setPosition(double position) {
        s1.setPosition(position);
        s2.setPosition(position);
    }

    @Override
    public double getPosition() {
        return s1.getPosition();
    }

    @Override
    public void scaleRange(double min, double max) {

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
