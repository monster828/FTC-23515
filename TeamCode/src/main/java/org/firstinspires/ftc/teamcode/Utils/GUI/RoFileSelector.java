package org.firstinspires.ftc.teamcode.Utils.GUI;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Utils.RobopathFilter;

import java.io.File;
import java.io.FileFilter;

/**
 * A menu for selecting files in a specific file directory.
 */
public class RoFileSelector extends RoDropdown {

    File[] files;

    /**
     * A menu for selecting files in a specific file directory.
     * @param filter a {@link FileFilter} for only allowing certain files.
     */
    public RoFileSelector(String label, Telemetry telemetry, String path, FileFilter filter) {
        super(label, telemetry);
        this.tem = telemetry;
        this.label = label;
        this.gui = new RoGUI(tem);
        File d = new File(path);
        if(d.exists()) {
            if(d.isFile()) {
                d = d.getParentFile();
            }
            if(d != null) files = d.listFiles(filter);
        }
        if(files != null) {
            for (File f : files) this.addOption(f.getName());
        }
    }

    /**
     * A menu for selecting files in a specific file directory.
     */
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
            if(d != null) files = d.listFiles();
        }
        if(files != null) {
            for (File f : files) this.addOption(f.getName());
        }
    }

    @Override
    public Object getOutput() {
        return files[selected];
    }

    public int getSelectedI() {
        return selected;
    }

}
