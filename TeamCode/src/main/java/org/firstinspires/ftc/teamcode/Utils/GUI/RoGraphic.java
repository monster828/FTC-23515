package org.firstinspires.ftc.teamcode.Utils.GUI;

public abstract class RoGraphic {

    abstract public Object getOutput();
    abstract public void setOutput(Object o);
    abstract public void interactRight();
    abstract public void interactLeft();
    abstract public void interactUp();
    abstract public void interactDown();
    abstract public void interact();
    abstract public void render(boolean selected);
    abstract public boolean takeControls();
}
