package org.firstinspires.ftc.teamcode.Utils.GUI;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RoDropdown extends RoTab {

    int selected = -1;

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

    @Override
    public void render(boolean selected) {
        if(gui.getOutOf(0).equals(true)) {
            open = false;
        }
        if(!open) {
            String dat = "";
            if (selected) {
                dat += "▓" + label;
            } else {
                dat += " " + label;
            }
            if(this.selected != -1) {
                dat += gui.getOutOf(this.selected);
            }
            while (dat.length() < 25) {
                dat += "-";
            }
            dat += ">";
            tem.addData("",dat);
        } else {
            gui.render();
        }
    }
}
