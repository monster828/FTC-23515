package org.firstinspires.ftc.teamcode.Utils;

import android.graphics.Point;

public class MatrixDrawer {

    public static void fillSquare(Object[][] a, int x, int y, int w, int h, Object o) {
        for(int x0 = x; x0 < x+w; x0++) {
            for(int y0 = y; y0 < y+h; y0++) {
                if(x0 > 0 && x0 < a.length && y0 > 0 && y0 < a[x0].length) a[x0][y0] = o;
            }
        }
    }
    public static void fillSquare(boolean[][] a, int x, int y, int w, int h, boolean o) {
        for(int x0 = x; x0 < x+w; x0++) {
            for(int y0 = y; y0 < y+h; y0++) {
                if(x0 > 0 && x0 < a.length && y0 > 0 && y0 < a[x0].length) a[x0][y0] = o;
            }
        }
    }
    public static void fillSquare(byte[][] a, int x, int y, int w, int h, byte o) {
        for(int x0 = x; x0 < x+w; x0++) {
            for(int y0 = y; y0 < y+h; y0++) {
                if(x0 > 0 && x0 < a.length && y0 > 0 && y0 < a[x0].length) a[x0][y0] = o;
            }
        }
    }
    public static void fillSquare(char[][] a, int x, int y, int w, int h, char o) {
        for(int x0 = x; x0 < x+w; x0++) {
            for(int y0 = y; y0 < y+h; y0++) {
                if(x0 > 0 && x0 < a.length && y0 > 0 && y0 < a[x0].length) a[x0][y0] = o;
            }
        }
    }
    public static void fillSquare(double[][] a, int x, int y, int w, int h, double o) {
        for(int x0 = x; x0 < x+w; x0++) {
            for(int y0 = y; y0 < y+h; y0++) {
                if(x0 > 0 && x0 < a.length && y0 > 0 && y0 < a[x0].length) a[x0][y0] = o;
            }
        }
    }
    public static void fillSquare(float[][] a, int x, int y, int w, int h, float o) {
        for(int x0 = x; x0 < x+w; x0++) {
            for(int y0 = y; y0 < y+h; y0++) {
                if(x0 > 0 && x0 < a.length && y0 > 0 && y0 < a[x0].length) a[x0][y0] = o;
            }
        }
    }
    public static void fillSquare(int[][] a, int x, int y, int w, int h, int o) {
        for(int x0 = x; x0 < x+w; x0++) {
            for(int y0 = y; y0 < y+h; y0++) {
                if(x0 > 0 && x0 < a.length && y0 > 0 && y0 < a[x0].length) a[x0][y0] = o;
            }
        }
    }
    public static void fillSquare(long[][] a, int x, int y, int w, int h, long o) {
        for(int x0 = x; x0 < x+w; x0++) {
            for(int y0 = y; y0 < y+h; y0++) {
                if(x0 > 0 && x0 < a.length && y0 > 0 && y0 < a[x0].length) a[x0][y0] = o;
            }
        }
    }
    public static void fillSquare(short[][] a, int x, int y, int w, int h, short o) {
        for(int x0 = x; x0 < x+w; x0++) {
            for(int y0 = y; y0 < y+h; y0++) {
                if(x0 > 0 && x0 < a.length && y0 > 0 && y0 < a[x0].length) a[x0][y0] = o;
            }
        }
    }

    public static void drawSquare(Object[][] a, int x, int y, int w, int h, Object o) {
        for(int x0 = x; x0 < x+w; x0++) {
            if(x0 > 0 && x0 < a.length && y > 0 && y < a[x0].length) a[x0][y] = o;
            if(x0 > 0 && x0 < a.length && y+h > 0 && y+h < a[x0].length) a[x0][y+h] = o;
        }
        for(int y0 = y; y0 < y+h; y0++) {
            if(x > 0 && x < a.length && y0 > 0 && y0 < a[x].length) a[x][y0] = o;
            if(x+w > 0 && x+w < a.length && y0 > 0 && y0 < a[x+w].length) a[x+w][y0] = o;
        }
    }
    public static void drawSquare(boolean[][] a, int x, int y, int w, int h, boolean o) {
        for(int x0 = x; x0 < x+w; x0++) {
            if(x0 > 0 && x0 < a.length && y > 0 && y < a[x0].length) a[x0][y] = o;
            if(x0 > 0 && x0 < a.length && y+h-1 > 0 && y+h-1 < a[x0].length) a[x0][y+h-1] = o;
        }
        for(int y0 = y; y0 < y+h; y0++) {
            if(x > 0 && x < a.length && y0 > 0 && y0 < a[x].length) a[x][y0] = o;
            if(x+w > 0 && x+w-1 < a.length && y0 > 0 && y0 < a[x+w-1].length) a[x+w-1][y0] = o;
        }
    }
    public static void drawSquare(byte[][] a, int x, int y, int w, int h, byte o) {
        for(int x0 = x; x0 < x+w; x0++) {
            if(x0 > 0 && x0 < a.length && y > 0 && y < a[x0].length) a[x0][y] = o;
            if(x0 > 0 && x0 < a.length && y+h-1 > 0 && y+h-1 < a[x0].length) a[x0][y+h-1] = o;
        }
        for(int y0 = y; y0 < y+h; y0++) {
            if(x > 0 && x < a.length && y0 > 0 && y0 < a[x].length) a[x][y0] = o;
            if(x+w > 0 && x+w-1 < a.length && y0 > 0 && y0 < a[x+w-1].length) a[x+w-1][y0] = o;
        }
    }
    public static void drawSquare(char[][] a, int x, int y, int w, int h, char o) {
        for(int x0 = x; x0 < x+w; x0++) {
            if(x0 > 0 && x0 < a.length && y > 0 && y < a[x0].length) a[x0][y] = o;
            if(x0 > 0 && x0 < a.length && y+h-1 > 0 && y+h-1 < a[x0].length) a[x0][y+h-1] = o;
        }
        for(int y0 = y; y0 < y+h; y0++) {
            if(x > 0 && x < a.length && y0 > 0 && y0 < a[x].length) a[x][y0] = o;
            if(x+w > 0 && x+w-1 < a.length && y0 > 0 && y0 < a[x+w-1].length) a[x+w-1][y0] = o;
        }
    }
    public static void drawSquare(double[][] a, int x, int y, int w, int h, double o) {
        for(int x0 = x; x0 < x+w; x0++) {
            if(x0 > 0 && x0 < a.length && y > 0 && y < a[x0].length) a[x0][y] = o;
            if(x0 > 0 && x0 < a.length && y+h-1 > 0 && y+h-1 < a[x0].length) a[x0][y+h-1] = o;
        }
        for(int y0 = y; y0 < y+h; y0++) {
            if(x > 0 && x < a.length && y0 > 0 && y0 < a[x].length) a[x][y0] = o;
            if(x+w > 0 && x+w-1 < a.length && y0 > 0 && y0 < a[x+w-1].length) a[x+w-1][y0] = o;
        }
    }
    public static void drawSquare(float[][] a, int x, int y, int w, int h, float o) {
        for(int x0 = x; x0 < x+w; x0++) {
            if(x0 > 0 && x0 < a.length && y > 0 && y < a[x0].length) a[x0][y] = o;
            if(x0 > 0 && x0 < a.length && y+h-1 > 0 && y+h-1 < a[x0].length) a[x0][y+h-1] = o;
        }
        for(int y0 = y; y0 < y+h; y0++) {
            if(x > 0 && x < a.length && y0 > 0 && y0 < a[x].length) a[x][y0] = o;
            if(x+w > 0 && x+w-1 < a.length && y0 > 0 && y0 < a[x+w-1].length) a[x+w-1][y0] = o;
        }
    }
    public static void drawSquare(int[][] a, int x, int y, int w, int h, int o) {
        for(int x0 = x; x0 < x+w; x0++) {
            if(x0 > 0 && x0 < a.length && y > 0 && y < a[x0].length) a[x0][y] = o;
            if(x0 > 0 && x0 < a.length && y+h-1 > 0 && y+h-1 < a[x0].length) a[x0][y+h-1] = o;
        }
        for(int y0 = y; y0 < y+h; y0++) {
            if(x > 0 && x < a.length && y0 > 0 && y0 < a[x].length) a[x][y0] = o;
            if(x+w > 0 && x+w-1 < a.length && y0 > 0 && y0 < a[x+w-1].length) a[x+w-1][y0] = o;
        }
    }
    public static void drawSquare(long[][] a, int x, int y, int w, int h, long o) {
        for(int x0 = x; x0 < x+w; x0++) {
            if(x0 > 0 && x0 < a.length && y > 0 && y < a[x0].length) a[x0][y] = o;
            if(x0 > 0 && x0 < a.length && y+h-1 > 0 && y+h-1 < a[x0].length) a[x0][y+h-1] = o;
        }
        for(int y0 = y; y0 < y+h; y0++) {
            if(x > 0 && x < a.length && y0 > 0 && y0 < a[x].length) a[x][y0] = o;
            if(x+w > 0 && x+w-1 < a.length && y0 > 0 && y0 < a[x+w-1].length) a[x+w-1][y0] = o;
        }
    }
    public static void drawSquare(short[][] a, int x, int y, int w, int h, short o) {
        for(int x0 = x; x0 < x+w; x0++) {
            if(x0 > 0 && x0 < a.length && y > 0 && y < a[x0].length) a[x0][y] = o;
            if(x0 > 0 && x0 < a.length && y+h-1 > 0 && y+h-1 < a[x0].length) a[x0][y+h-1] = o;
        }
        for(int y0 = y; y0 < y+h; y0++) {
            if(x > 0 && x < a.length && y0 > 0 && y0 < a[x].length) a[x][y0] = o;
            if(x+w > 0 && x+w-1 < a.length && y0 > 0 && y0 < a[x+w-1].length) a[x+w-1][y0] = o;
        }
    }

    public static void drawLine(Object[][] a, int x0, int y0, int x1, int y1, Object o) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
            int y = (int) (y0+((x-x0+1)*m));
            if(y-yO > 0) {
                for (int i = yO+1; i <= y; i++) {
                    a[x][i] = o;
                }
            } else if(y-yO < 0) {
                for (int i = y+1; i <= yO; i++) {
                    a[x][i] = o;
                }
            } else a[x][y] = o;
            yO = y;
        }
    }
    public static void drawLine(boolean[][] a, int x0, int y0, int x1, int y1, boolean o) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
           int y = (int) (y0+((x-x0+1)*m));
           if(y-yO > 0) {
               for (int i = yO+1; i <= y; i++) {
                   a[x][i] = o;
               }
           } else if(y-yO < 0) {
               for (int i = y+1; i <= yO; i++) {
                   a[x][i] = o;
               }
           } else a[x][y] = o;
           yO = y;
        }
    }
    public static void drawLine(byte[][] a, int x0, int y0, int x1, int y1, byte o) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
            int y = (int) (y0+((x-x0+1)*m));
            if(y-yO > 0) {
                for (int i = yO+1; i <= y; i++) {
                    a[x][i] = o;
                }
            } else if(y-yO < 0) {
                for (int i = y+1; i <= yO; i++) {
                    a[x][i] = o;
                }
            } else a[x][y] = o;
            yO = y;
        }
    }
    public static void drawLine(char[][] a, int x0, int y0, int x1, int y1, char o) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
            int y = (int) (y0+((x-x0+1)*m));
            if(y-yO > 0) {
                for (int i = yO+1; i <= y; i++) {
                    a[x][i] = o;
                }
            } else if(y-yO < 0) {
                for (int i = y+1; i <= yO; i++) {
                    a[x][i] = o;
                }
            } else a[x][y] = o;
            yO = y;
        }
    }
    public static void drawLine(double[][] a, int x0, int y0, int x1, int y1, double o) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
            int y = (int) (y0+((x-x0+1)*m));
            if(y-yO > 0) {
                for (int i = yO+1; i <= y; i++) {
                    a[x][i] = o;
                }
            } else if(y-yO < 0) {
                for (int i = y+1; i <= yO; i++) {
                    a[x][i] = o;
                }
            } else a[x][y] = o;
            yO = y;
        }
    }
    public static void drawLine(float[][] a, int x0, int y0, int x1, int y1, float o) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
            int y = (int) (y0+((x-x0+1)*m));
            if(y-yO > 0) {
                for (int i = yO+1; i <= y; i++) {
                    a[x][i] = o;
                }
            } else if(y-yO < 0) {
                for (int i = y+1; i <= yO; i++) {
                    a[x][i] = o;
                }
            } else a[x][y] = o;
            yO = y;
        }
    }
    public static void drawLine(int[][] a, int x0, int y0, int x1, int y1, int o) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
            int y = (int) (y0+((x-x0+1)*m));
            if(y-yO > 0) {
                for (int i = yO+1; i <= y; i++) {
                    a[x][i] = o;
                }
            } else if(y-yO < 0) {
                for (int i = y+1; i <= yO; i++) {
                    a[x][i] = o;
                }
            } else a[x][y] = o;
            yO = y;
        }
    }
    public static void drawLine(long[][] a, int x0, int y0, int x1, int y1, long o) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
            int y = (int) (y0+((x-x0+1)*m));
            if(y-yO > 0) {
                for (int i = yO+1; i <= y; i++) {
                    a[x][i] = o;
                }
            } else if(y-yO < 0) {
                for (int i = y+1; i <= yO; i++) {
                    a[x][i] = o;
                }
            } else a[x][y] = o;
            yO = y;
        }
    }
    public static void drawLine(short[][] a, int x0, int y0, int x1, int y1, short o) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
            int y = (int) (y0+((x-x0+1)*m));
            if(y-yO > 0) {
                for (int i = yO+1; i <= y; i++) {
                    a[x][i] = o;
                }
            } else if(y-yO < 0) {
                for (int i = y+1; i <= yO; i++) {
                    a[x][i] = o;
                }
            } else a[x][y] = o;
            yO = y;
        }
    }

    public static void drawQuad(Object[][] a, Point p0, Point p1, Point p2, Point p3, Object o) {
        drawLine(a,p0.x,p0.y,p1.x,p1.y,o);
        drawLine(a,p1.x,p1.y,p2.x,p2.y,o);
        drawLine(a,p2.x,p2.y,p3.x,p3.y,o);
        drawLine(a,p3.x,p3.y,p0.x,p0.y,o);
    }
    public static void drawQuad(boolean[][] a, Point p0, Point p1, Point p2, Point p3, boolean o) {
        drawLine(a,p0.x,p0.y,p1.x,p1.y,o);
        drawLine(a,p1.x,p1.y,p2.x,p2.y,o);
        drawLine(a,p2.x,p2.y,p3.x,p3.y,o);
        drawLine(a,p3.x,p3.y,p0.x,p0.y,o);
    }
    public static void drawQuad(byte[][] a, Point p0, Point p1, Point p2, Point p3, byte o) {
        drawLine(a,p0.x,p0.y,p1.x,p1.y,o);
        drawLine(a,p1.x,p1.y,p2.x,p2.y,o);
        drawLine(a,p2.x,p2.y,p3.x,p3.y,o);
        drawLine(a,p3.x,p3.y,p0.x,p0.y,o);
    }
    public static void drawQuad(char[][] a, Point p0, Point p1, Point p2, Point p3, char o) {
        drawLine(a,p0.x,p0.y,p1.x,p1.y,o);
        drawLine(a,p1.x,p1.y,p2.x,p2.y,o);
        drawLine(a,p2.x,p2.y,p3.x,p3.y,o);
        drawLine(a,p3.x,p3.y,p0.x,p0.y,o);
    }
    public static void drawQuad(double[][] a, Point p0, Point p1, Point p2, Point p3, double o) {
        drawLine(a,p0.x,p0.y,p1.x,p1.y,o);
        drawLine(a,p1.x,p1.y,p2.x,p2.y,o);
        drawLine(a,p2.x,p2.y,p3.x,p3.y,o);
        drawLine(a,p3.x,p3.y,p0.x,p0.y,o);
    }
    public static void drawQuad(float[][] a, Point p0, Point p1, Point p2, Point p3, float o) {
        drawLine(a,p0.x,p0.y,p1.x,p1.y,o);
        drawLine(a,p1.x,p1.y,p2.x,p2.y,o);
        drawLine(a,p2.x,p2.y,p3.x,p3.y,o);
        drawLine(a,p3.x,p3.y,p0.x,p0.y,o);
    }
    public static void drawQuad(int[][] a, Point p0, Point p1, Point p2, Point p3, int o) {
        drawLine(a,p0.x,p0.y,p1.x,p1.y,o);
        drawLine(a,p1.x,p1.y,p2.x,p2.y,o);
        drawLine(a,p2.x,p2.y,p3.x,p3.y,o);
        drawLine(a,p3.x,p3.y,p0.x,p0.y,o);
    }
    public static void drawQuad(long[][] a, Point p0, Point p1, Point p2, Point p3, long o) {
        drawLine(a,p0.x,p0.y,p1.x,p1.y,o);
        drawLine(a,p1.x,p1.y,p2.x,p2.y,o);
        drawLine(a,p2.x,p2.y,p3.x,p3.y,o);
        drawLine(a,p3.x,p3.y,p0.x,p0.y,o);
    }
    public static void drawQuad(short[][] a, Point p0, Point p1, Point p2, Point p3, short o) {
        drawLine(a,p0.x,p0.y,p1.x,p1.y,o);
        drawLine(a,p1.x,p1.y,p2.x,p2.y,o);
        drawLine(a,p2.x,p2.y,p3.x,p3.y,o);
        drawLine(a,p3.x,p3.y,p0.x,p0.y,o);
    }

    public static void drawCircle(Object[][] a, int x, int y, int r, Object o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            a[x0+x][y0+y] = o;
            a[-x0+x][y0+y] = o;
            a[x0+x][-y0+y] = o;
            a[-x0+x][-y0+y] = o;
            a[y0+x][x0+y] = o;
            a[-y0+x][x0+y] = o;
            a[y0+x][-x0+y] = o;
            a[-y0+x][-x0+y] = o;
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }
    public static void drawCircle(boolean[][] a, int x, int y, int r, boolean o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            a[x0+x][y0+y] = o;
            a[-x0+x][y0+y] = o;
            a[x0+x][-y0+y] = o;
            a[-x0+x][-y0+y] = o;
            a[y0+x][x0+y] = o;
            a[-y0+x][x0+y] = o;
            a[y0+x][-x0+y] = o;
            a[-y0+x][-x0+y] = o;
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }
    public static void drawCircle(byte[][] a, int x, int y, int r, byte o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            a[x0+x][y0+y] = o;
            a[-x0+x][y0+y] = o;
            a[x0+x][-y0+y] = o;
            a[-x0+x][-y0+y] = o;
            a[y0+x][x0+y] = o;
            a[-y0+x][x0+y] = o;
            a[y0+x][-x0+y] = o;
            a[-y0+x][-x0+y] = o;
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }
    public static void drawCircle(char[][] a, int x, int y, int r, char o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            a[x0+x][y0+y] = o;
            a[-x0+x][y0+y] = o;
            a[x0+x][-y0+y] = o;
            a[-x0+x][-y0+y] = o;
            a[y0+x][x0+y] = o;
            a[-y0+x][x0+y] = o;
            a[y0+x][-x0+y] = o;
            a[-y0+x][-x0+y] = o;
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }
    public static void drawCircle(double[][] a, int x, int y, int r, double o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            a[x0+x][y0+y] = o;
            a[-x0+x][y0+y] = o;
            a[x0+x][-y0+y] = o;
            a[-x0+x][-y0+y] = o;
            a[y0+x][x0+y] = o;
            a[-y0+x][x0+y] = o;
            a[y0+x][-x0+y] = o;
            a[-y0+x][-x0+y] = o;
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }
    public static void drawCircle(float[][] a, int x, int y, int r, float o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            a[x0+x][y0+y] = o;
            a[-x0+x][y0+y] = o;
            a[x0+x][-y0+y] = o;
            a[-x0+x][-y0+y] = o;
            a[y0+x][x0+y] = o;
            a[-y0+x][x0+y] = o;
            a[y0+x][-x0+y] = o;
            a[-y0+x][-x0+y] = o;
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }
    public static void drawCircle(int[][] a, int x, int y, int r, int o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            a[x0+x][y0+y] = o;
            a[-x0+x][y0+y] = o;
            a[x0+x][-y0+y] = o;
            a[-x0+x][-y0+y] = o;
            a[y0+x][x0+y] = o;
            a[-y0+x][x0+y] = o;
            a[y0+x][-x0+y] = o;
            a[-y0+x][-x0+y] = o;
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }
    public static void drawCircle(long[][] a, int x, int y, int r, long o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            a[x0+x][y0+y] = o;
            a[-x0+x][y0+y] = o;
            a[x0+x][-y0+y] = o;
            a[-x0+x][-y0+y] = o;
            a[y0+x][x0+y] = o;
            a[-y0+x][x0+y] = o;
            a[y0+x][-x0+y] = o;
            a[-y0+x][-x0+y] = o;
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }
    public static void drawCircle(short[][] a, int x, int y, int r, short o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            a[x0+x][y0+y] = o;
            a[-x0+x][y0+y] = o;
            a[x0+x][-y0+y] = o;
            a[-x0+x][-y0+y] = o;
            a[y0+x][x0+y] = o;
            a[-y0+x][x0+y] = o;
            a[y0+x][-x0+y] = o;
            a[-y0+x][-x0+y] = o;
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }

    public static void fillCircle(Object[][] a, int x, int y, int r, Object o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            for(int x1 = -x0; x1 <= x0; x1++) {
                a[x1+x][y0+y] = o;
                a[x1+x][-y0+y] = o;
            }
            for(int x1 = -y0; x1 <= y0; x1++) {
                a[x1+x][x0+y] = o;
                a[x1+x][-x0+y] = o;
            }
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }
    public static void fillCircle(boolean[][] a, int x, int y, int r, boolean o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            for(int x1 = -x0; x1 <= x0; x1++) {
                a[x1+x][y0+y] = o;
                a[x1+x][-y0+y] = o;
            }
            for(int x1 = -y0; x1 <= y0; x1++) {
                a[x1+x][x0+y] = o;
                a[x1+x][-x0+y] = o;
            }
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }
    public static void fillCircle(byte[][] a, int x, int y, int r, byte o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            for(int x1 = -x0; x1 <= x0; x1++) {
                a[x1+x][y0+y] = o;
                a[x1+x][-y0+y] = o;
            }
            for(int x1 = -y0; x1 <= y0; x1++) {
                a[x1+x][x0+y] = o;
                a[x1+x][-x0+y] = o;
            }
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }
    public static void fillCircle(char[][] a, int x, int y, int r, char o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            for(int x1 = -x0; x1 <= x0; x1++) {
                a[x1+x][y0+y] = o;
                a[x1+x][-y0+y] = o;
            }
            for(int x1 = -y0; x1 <= y0; x1++) {
                a[x1+x][x0+y] = o;
                a[x1+x][-x0+y] = o;
            }
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }
    public static void fillCircle(double[][] a, int x, int y, int r, double o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            for(int x1 = -x0; x1 <= x0; x1++) {
                a[x1+x][y0+y] = o;
                a[x1+x][-y0+y] = o;
            }
            for(int x1 = -y0; x1 <= y0; x1++) {
                a[x1+x][x0+y] = o;
                a[x1+x][-x0+y] = o;
            }
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }
    public static void fillCircle(float[][] a, int x, int y, int r, float o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            for(int x1 = -x0; x1 <= x0; x1++) {
                a[x1+x][y0+y] = o;
                a[x1+x][-y0+y] = o;
            }
            for(int x1 = -y0; x1 <= y0; x1++) {
                a[x1+x][x0+y] = o;
                a[x1+x][-x0+y] = o;
            }
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }
    public static void fillCircle(int[][] a, int x, int y, int r, int o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            for(int x1 = -x0; x1 <= x0; x1++) {
                a[x1+x][y0+y] = o;
                a[x1+x][-y0+y] = o;
            }
            for(int x1 = -y0; x1 <= y0; x1++) {
                a[x1+x][x0+y] = o;
                a[x1+x][-x0+y] = o;
            }
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }
    public static void fillCircle(long[][] a, int x, int y, int r, long o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            for(int x1 = -x0; x1 <= x0; x1++) {
                a[x1+x][y0+y] = o;
                a[x1+x][-y0+y] = o;
            }
            for(int x1 = -y0; x1 <= y0; x1++) {
                a[x1+x][x0+y] = o;
                a[x1+x][-x0+y] = o;
            }
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }
    public static void fillCircle(short[][] a, int x, int y, int r, short o) {
        int x0 = 0; int y0 = r;
        int d = 3-(2*r);
        while(x0 <= y0) {
            for(int x1 = -x0; x1 <= x0; x1++) {
                a[x1+x][y0+y] = o;
                a[x1+x][-y0+y] = o;
            }
            for(int x1 = -y0; x1 <= y0; x1++) {
                a[x1+x][x0+y] = o;
                a[x1+x][-x0+y] = o;
            }
            if(d < 0) {
                d+= (4*x0)+6;
            } else {
                d += (4*(x0-y0))+10;
                y0--;
            }
            x0++;
        }
    }

    public static void drawNGon(Object[][] a, Point[] p, Object o) {
        for(int i = 0; i < p.length-1; i++) {
            drawLine(a,p[i].x,p[i].y,p[i+1].x,p[i+1].y,o);
        }
        drawLine(a,p[p.length-1].x,p[p.length-1].y,p[0].x,p[0].y,o);
    }
    public static void drawNGon(boolean[][] a, Point[] p, boolean o) {
        for(int i = 0; i < p.length-1; i++) {
            drawLine(a,p[i].x,p[i].y,p[i+1].x,p[i+1].y,o);
        }
        drawLine(a,p[p.length-1].x,p[p.length-1].y,p[0].x,p[0].y,o);
    }
    public static void drawNGon(byte[][] a, Point[] p, byte o) {
        for(int i = 0; i < p.length-1; i++) {
            drawLine(a,p[i].x,p[i].y,p[i+1].x,p[i+1].y,o);
        }
        drawLine(a,p[p.length-1].x,p[p.length-1].y,p[0].x,p[0].y,o);
    }
    public static void drawNGon(char[][] a, Point[] p, char o) {
        for(int i = 0; i < p.length-1; i++) {
            drawLine(a,p[i].x,p[i].y,p[i+1].x,p[i+1].y,o);
        }
        drawLine(a,p[p.length-1].x,p[p.length-1].y,p[0].x,p[0].y,o);
    }
    public static void drawNGon(double[][] a, Point[] p, double o) {
        for(int i = 0; i < p.length-1; i++) {
            drawLine(a,p[i].x,p[i].y,p[i+1].x,p[i+1].y,o);
        }
        drawLine(a,p[p.length-1].x,p[p.length-1].y,p[0].x,p[0].y,o);
    }
    public static void drawNGon(float[][] a, Point[] p, float o) {
        for(int i = 0; i < p.length-1; i++) {
            drawLine(a,p[i].x,p[i].y,p[i+1].x,p[i+1].y,o);
        }
        drawLine(a,p[p.length-1].x,p[p.length-1].y,p[0].x,p[0].y,o);
    }
    public static void drawNGon(int[][] a, Point[] p, int o) {
        for(int i = 0; i < p.length-1; i++) {
            drawLine(a,p[i].x,p[i].y,p[i+1].x,p[i+1].y,o);
        }
        drawLine(a,p[p.length-1].x,p[p.length-1].y,p[0].x,p[0].y,o);
    }
    public static void drawNGon(long[][] a, Point[] p, long o) {
        for(int i = 0; i < p.length-1; i++) {
            drawLine(a,p[i].x,p[i].y,p[i+1].x,p[i+1].y,o);
        }
        drawLine(a,p[p.length-1].x,p[p.length-1].y,p[0].x,p[0].y,o);
    }
    public static void drawNGon(short[][] a, Point[] p, short o) {
        for(int i = 0; i < p.length-1; i++) {
            drawLine(a,p[i].x,p[i].y,p[i+1].x,p[i+1].y,o);
        }
        drawLine(a,p[p.length-1].x,p[p.length-1].y,p[0].x,p[0].y,o);
    }

    private static boolean[][] fill;
    private static void drawLineS(Object[][] a, int x0, int y0, int x1, int y1, Object o, int sX, int sY) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
            int y = (int) (y0+((x-x0+1)*m));
            if(y-yO > 0) {
                for (int i = yO+1; i <= y; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else if(y-yO < 0) {
                for (int i = y+1; i <= yO; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else {a[x][y] = o;fill[x-sX][y-sY] = true;}
            yO = y;
        }
    }
    private static void drawLineS(boolean[][] a, int x0, int y0, int x1, int y1, boolean o, int sX, int sY) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
            int y = (int) (y0+((x-x0+1)*m));
            if(y-yO > 0) {
                for (int i = yO+1; i <= y; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else if(y-yO < 0) {
                for (int i = y+1; i <= yO; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else {a[x][y] = o;fill[x-sX][y-sY] = true;}
            yO = y;
        }
    }
    private static void drawLineS(byte[][] a, int x0, int y0, int x1, int y1, byte o, int sX, int sY) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
            int y = (int) (y0+((x-x0+1)*m));
            if(y-yO > 0) {
                for (int i = yO+1; i <= y; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else if(y-yO < 0) {
                for (int i = y+1; i <= yO; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else {a[x][y] = o;fill[x-sX][y-sY] = true;}
            yO = y;
        }
    }
    private static void drawLineS(char[][] a, int x0, int y0, int x1, int y1, char o, int sX, int sY) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
            int y = (int) (y0+((x-x0+1)*m));
            if(y-yO > 0) {
                for (int i = yO+1; i <= y; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else if(y-yO < 0) {
                for (int i = y+1; i <= yO; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else {a[x][y] = o;fill[x-sX][y-sY] = true;}
            yO = y;
        }
    }
    private static void drawLineS(double[][] a, int x0, int y0, int x1, int y1, double o, int sX, int sY) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
            int y = (int) (y0+((x-x0+1)*m));
            if(y-yO > 0) {
                for (int i = yO+1; i <= y; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else if(y-yO < 0) {
                for (int i = y+1; i <= yO; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else {a[x][y] = o;fill[x-sX][y-sY] = true;}
            yO = y;
        }
    }
    private static void drawLineS(float[][] a, int x0, int y0, int x1, int y1, float o, int sX, int sY) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
            int y = (int) (y0+((x-x0+1)*m));
            if(y-yO > 0) {
                for (int i = yO+1; i <= y; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else if(y-yO < 0) {
                for (int i = y+1; i <= yO; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else {a[x][y] = o;fill[x-sX][y-sY] = true;}
            yO = y;
        }
    }
    private static void drawLineS(int[][] a, int x0, int y0, int x1, int y1, int o, int sX, int sY) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
            int y = (int) (y0+((x-x0+1)*m));
            if(y-yO > 0) {
                for (int i = yO+1; i <= y; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else if(y-yO < 0) {
                for (int i = y+1; i <= yO; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else {a[x][y] = o;fill[x-sX][y-sY] = true;}
            yO = y;
        }
    }
    private static void drawLineS(long[][] a, int x0, int y0, int x1, int y1, long o, int sX, int sY) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
            int y = (int) (y0+((x-x0+1)*m));
            if(y-yO > 0) {
                for (int i = yO+1; i <= y; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else if(y-yO < 0) {
                for (int i = y+1; i <= yO; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else {a[x][y] = o;fill[x-sX][y-sY] = true;}
            yO = y;
        }
    }
    private static void drawLineS(short[][] a, int x0, int y0, int x1, int y1, short o, int sX, int sY) {
        if(x0 > x1) {
            int x = x0; int y = y0;
            x0 = x1; y0 = y1;
            x1 = x; y1 = y;
        }
        float m = (float) (y1 - y0)/(x1-x0+1);
        int yO = y0;
        for(int x = x0; x <= x1; x++) {
            int y = (int) (y0+((x-x0+1)*m));
            if(y-yO > 0) {
                for (int i = yO+1; i <= y; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else if(y-yO < 0) {
                for (int i = y+1; i <= yO; i++) {
                    a[x][i] = o;
                    fill[x-sX][i-sY] = true;
                }
            } else {a[x][y] = o;fill[x-sX][y-sY] = true;}
            yO = y;
        }
    }

    /**DOESN'T YET SUPPORT NGONS WITH POINTS ON THE "INSIDE"*/
    public static void fillNGon(boolean[][] a, Point[] p, boolean o) {
        int miX = p[0].x;
        int maX = 0;
        int miY = p[0].y;
        int maY = 0;
        for(Point p0 : p) {if(p0.x < miX) miX = p0.x; if(p0.x > maX) maX = p0.x; if(p0.y < miY) miY = p0.y; if(p0.y > maY) maY = p0.y;}
        fill = new boolean[maX-miX+1][maY-miY+1];
        for(int i = 0; i < p.length-1; i++) {
            drawLineS(a,p[i].x,p[i].y,p[i+1].x,p[i+1].y,o,miX,miY);
        }
        drawLineS(a,p[p.length-1].x,p[p.length-1].y,p[0].x,p[0].y,o,miX,miY);
        for(int x = 0; x < fill.length; x++) {
            boolean f = fill[x][0];
            int last = 0;
            for(int i = 0; i < fill[x].length; i++) {
                if(fill[x][i]) last = i;
            }
            for(int y = 1; y < fill[x].length-1; y++) {
                if(f) {
                    if (fill[x][y] && !fill[x][y + 1]) f = false;
                } else {
                    if (fill[x][y] && !fill[x][y - 1]) f = true;
                }
                if(f) {
                    a[x+miX][y+miY] = o;
                    if(y == last) f = false;
                }
                if(y == last) break;
            }
        }
    }
}
