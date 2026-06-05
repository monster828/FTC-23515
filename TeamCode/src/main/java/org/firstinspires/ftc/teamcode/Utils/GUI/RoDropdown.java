package org.firstinspires.ftc.teamcode.Utils.GUI;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RoDropdown extends RoTab {

    int selected;

    public RoDropdown(String label, Telemetry telemetry) {
        super(label, telemetry);
        this.tem = telemetry;
        this.label = label;
        this.gui = new RoGUI(tem);
    }

    @Override
    public Object getOutput() {
        return selected;
    }

    @Override
    public void setOutput(Object o) {
        try {
            selected = (int)o;
        } catch (Exception e) {

        }
    }

    @Override
    public void interact() {
        if(!open) {
            open = true;
        } else {
            selected = gui.getSelected();
            open = false;
        }
    }

    public void addOption(String label) {
        this.gui.addLabel(label);
    }
}
