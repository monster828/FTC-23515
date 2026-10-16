package org.firstinspires.ftc.teamcode.Utils;

public class ArrayDrawer {

    public static boolean[][] drawRect(boolean[][] in, int x, int y, int w, int h, boolean v) {
        boolean[][] out = in.clone();
        for(int x0 = x; x0 < x+w; x0++) {
            for(int y0 = y; y0 < y+h; y0++) {
                if(x0 < out.length && y0 < out[0].length) out[x0][y0] = v;
            }
        }
        return out;
    }

}
