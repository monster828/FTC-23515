package org.firstinspires.ftc.teamcode.Utils.GUI;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utils.Controls.ControlsTrigger;
import org.firstinspires.ftc.teamcode.Utils.Controls.GamepadButton;

import java.util.ArrayList;

/**
 * Create a UI in the telemetry. You must run {@link Telemetry#update()} after running {@link RoGUI#render()} in your loop.
 */
public class RoGUI {

    private Telemetry telemetry;
    private ArrayList<RoGraphic> graphics = new ArrayList<RoGraphic>();
    private int selected = 0;
    private boolean captive = false;
    private Gamepad c;
    private ControlsTrigger up, down, left, right, interact;

    /**
     * Create a UI in the telemetry.
     * You must run {@link Telemetry#update()} after running {@link RoGUI#render()} in your loop.
     * @param tem telemetry to write to.
     */
    public RoGUI(Telemetry tem) {
        telemetry = tem;
    }

    /**
     * Create a UI in the telemetry.
     * You must run {@link Telemetry#update()} after running {@link RoGUI#render()} in your loop.
     * This constructor allows for using default loop.
     * @param tem telemetry to write to.
     */
    public RoGUI(Telemetry tem, Gamepad controller) {
        telemetry = tem; c = controller;
        up = new ControlsTrigger(controller, GamepadButton.GButton.dpad_up);
        down = new ControlsTrigger(controller, GamepadButton.GButton.dpad_down);
        left = new ControlsTrigger(controller, GamepadButton.GButton.dpad_left);
        right = new ControlsTrigger(controller, GamepadButton.GButton.dpad_right);
        interact = new ControlsTrigger(controller, GamepadButton.GButton.left_bumper);
    }

    /**
     * Run this when the user presses up
     */
    public void up() {
        if(!captive) {
            if (selected > 0) {
                selected--;
            }
        } else {
            graphics.get(selected).interactUp();
        }
    }

    /**
     * Run this when the user presses down
     */
    public void down() {
        if(!captive) {
            if (selected < graphics.size() - 1) {
                selected++;
            }
        } else {
            graphics.get(selected).interactDown();
        }
    }

    /**
     * Run this when the user presses left
     */
    public void left() {
        graphics.get(selected).interactLeft();
    }

    /**
     * Run this when the user presses right
     */
    public void right() {
        graphics.get(selected).interactRight();
    }

    /**
     * Run this when the user interacts with the selected UI element
     */
    public void interact() {
        graphics.get(selected).interact();
    }

    /**
     * Does loop stuff, doesn't include
     */
    public void defaultLoop() {
        if(up.IsTriggered()) up();
        if(down.IsTriggered()) down();
        if(left.IsTriggered()) left();
        if(right.IsTriggered()) right();
        if(interact.IsTriggered()) interact();
        render();
    }

    /**
     * Returns the output of a specific UI element
     * @param n the ID of the UI element to get the output of
     * @return the output, or -1 if there is no such UI element
     */
    public Object getOutOf(int n) {
        if(n > -1 && n < graphics.size()) {
            return graphics.get(n).getOutput();
        } else {
            return 0;
        }
    }

    /**
     * returns the UI element with a specific ID
     * @param n the ID of the UI element
     * @return
     */
    public RoGraphic get(int n) {return graphics.get(n);}
    public Object getOutSelected() {
        return graphics.get(selected).getOutput();
    }

    /**
     * Displays the UI, run this in your loop before updating telemetry.
     */
    public void render() {
        captive = graphics.get(selected).takeControls();
        if(!captive) {
            for (int i = 0; i < graphics.size(); i++) {
                graphics.get(i).render(i == selected);
            }
        } else {
            graphics.get(selected).render(true);
        }
    }

    /**
     * Sets the output of a specific UI element
     * @param n the ID of the element
     * @param data the data to set the output to
     */
    public void setOutput(int n, Object data) {
        graphics.get(n).setOutput(data);
    }

    /**
     * Resets the UI
     */
    public void clear() {
        graphics.clear();
        selected = 0;
        captive = false;
    }

    /**
     * get the ID of the selected element
     */
    public int getSelected() {
        return selected;
    }

    /**
     * Add a UI element to the UI
     * @param g
     */
    public void add(RoGraphic g) {
        graphics.add(g);
    }

    /**
     * Add a slider to the UI (outputs an integer)
     * @param min minimum value of the slider
     * @param max maximum value of the slider
     */
    public void addSlider(int min, int max) {
        graphics.add(new RoSlider(min,max,telemetry));
    }

    /**
     * Add a button to the UI (outputs a boolean)
     * @param label name of the button
     */
    public void addButton(String label) {
        graphics.add(new RoButton(telemetry,label));
    }

    /**
     * Add a label to the UI (literally just some text)
     * @param label the text
     */
    public void addLabel(String label) {
        graphics.add(new RoLabel(label,telemetry));
    }

    /**
     * Add a number input to the UI (outputs a float)
     * @param label label for the number
     * @param Integers amount of whole digits
     * @param Decimals amount of decimal digits? (does this make sense?)
     */
    public void addNumInput(String label,int Integers, int Decimals) {
        graphics.add(new RoNumInput(label,Integers,Decimals,telemetry));
    }

    /**
     * Add a switch to the UI (outputs a boolean)
     * @param label label for the switch
     */
    public void addSwitch(String label){graphics.add(new RoSwitch(label,telemetry));}

}
