package org.firstinspires.ftc.teamcode.Utils.GUI;

public abstract class RoGraphic {

    /**
     * Gets the output of the GUI element
     * @return the output
     */
    abstract public Object getOutput();

    /**
     * Sets the output of the GUI element
     * @param o the output
     */
    abstract public void setOutput(Object o);

    /**
     * Causes something to happen when the user presses right with the element selected
     */
    abstract public void interactRight();

    /**
     * Causes something to happen when the user presses left with the element selected
     */
    abstract public void interactLeft();

    /**
     * Causes something to happen when the user presses up with the element selected (GUI selection must be locked)
     */
    abstract public void interactUp();

    /**
     * Causes something to happen when the user presses down with the element selected (GUI selection must be locked)
     */
    abstract public void interactDown();

    /**
     * Causes something to happen when the user presses interact with the element selected
     */
    abstract public void interact();

    /**
     * Add the element to the telemetry
     * @param selected if the element is selected by the user
     */
    abstract public void render(boolean selected);

    /**
     * maybe locks the selection of the GUI
     * @return boolean if the selection should be locked
     */
    abstract public boolean takeControls();
}
