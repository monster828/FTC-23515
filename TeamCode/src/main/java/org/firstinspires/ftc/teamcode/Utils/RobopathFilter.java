package org.firstinspires.ftc.teamcode.Utils;

import java.io.File;
import java.io.FileFilter;

public class RobopathFilter implements FileFilter {
    @Override
    public boolean accept(File f) {
        if(f.isDirectory()) return true;
        String fileName = f.getName();
        return fileName.substring(fileName.lastIndexOf(".")).equals(".robopath");
    }
}
