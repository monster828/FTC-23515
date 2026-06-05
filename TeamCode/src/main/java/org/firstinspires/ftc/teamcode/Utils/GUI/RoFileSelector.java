package org.firstinspires.ftc.teamcode.Utils.GUI;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utils.RobopathFilter;

import java.io.File;

public class RoFileSelector extends RoDropdown {

    File[] files;

    public RoFileSelector(String label, Telemetry telemetry, String path) {
        super(label, telemetry);
        this.tem = telemetry;
        this.label = label;
        this.gui = new RoGUI(tem);
        File d = new File(path);
        if(d.exists()) {
            if(d.isFile()) {
                d = d.getParentFile();
            }
            if(d != null) files = d.listFiles(new RobopathFilter());
        }
        if(files != null) {
            for (File f : files) this.addOption(f.getName());
        }
    }

    @Override
    public Object getOutput() {
        return files[selected];
    }

}
