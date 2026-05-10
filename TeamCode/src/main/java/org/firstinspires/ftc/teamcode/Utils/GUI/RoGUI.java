package org.firstinspires.ftc.teamcode.Utils.GUI;

import static java.lang.Thread.sleep;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.ArrayList;
import java.util.List;

public class RoGUI {

    private Telemetry telemetry;
    private ArrayList<RoGraphic> graphics = new ArrayList<RoGraphic>();
    private int selected = 0;
    private boolean captive = false;

    /**
     * Create a UI in the telemetry.
     * @param tem telemetry to write to.
     */
    public RoGUI(Telemetry tem) {
        telemetry = tem;
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
     * Displays the UI
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
