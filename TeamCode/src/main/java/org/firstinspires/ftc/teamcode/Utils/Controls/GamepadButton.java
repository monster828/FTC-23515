package org.firstinspires.ftc.teamcode.Utils.Controls;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.lang.reflect.Field;

public class GamepadButton {

    GButton b;
    Gamepad g;

    /**
     * Create a class to represent a button on a gamepad.
     * @param controller the gamepad.
     * @param button the button.
     */
    public GamepadButton(Gamepad controller, GButton button) {
        b = button;
        g = controller;
    }

    public enum GButton {
        left_stick_button,
        right_stick_button,
        dpad_up,
        dpad_down,
        dpad_left,
        dpad_right,
        a,
        b,
        x,
        y,
        guide,
        start,
        back,
        left_bumper,
        right_bumper
    }

    /**
     * Check if the button on the gamepad is pressed.
     * @return if pressed, false if exception is thrown.
     */
    public boolean isPressed() {
        try {
            Field f = g.getClass().getField(b.toString());
            return (boolean)f.get(g);
        } catch (Exception e) {
            return false;
        }
    }
}
