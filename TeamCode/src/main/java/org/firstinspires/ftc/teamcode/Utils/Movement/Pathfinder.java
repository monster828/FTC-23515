package org.firstinspires.ftc.teamcode.Utils.Movement;

import java.util.ArrayList;

public class Pathfinder {

    public static float[][] field = new float[141][141];
    public static boolean[][] obstacle = new boolean[141][141];
    public static float[][] moveT = {
            {5,4,3,4,5},
            {4,3,2,3,4},
            {3,2,1,2,3},
            {2,1,0.00001f,1,2},
            {3,2,1,2,3},
            {4,3,2,3,4},
            {5,4,3,4,5}
    };
    public static int[][] lastPath;

    public static void compileCircleMoveT(int r) {
        moveT = new float[(r*2)+1][(r*2)+1];
        for(int x = 0; x < moveT.length; x++) {
            for(int y = 0; y < moveT.length; y++) {
                int x0 = x-r; int y0 = y-r;
                moveT[x][y] = (float) Math.sqrt((x0*x0)+(y0*y0));
            }
        }
    }

    public static void pathFrom(int x, int y) {
        field[x][y] = -0.0001f;
        ArrayList<int[]> queue = new ArrayList<>();
        queue.add(new int[] {x,y});
        while(!queue.isEmpty()) {
            for(int x0 = -(moveT.length/2)+1; x0 < (moveT.length/2); x0++) {
                for(int y0 = -(moveT.length/2)+1; y0 < (moveT[0].length/2); y0++) {
                    //System.out.println(x0+(moveT.length/2));
                    int x1 = queue.get(0)[0]; int y1 = queue.get(0)[1];
                    float t = moveT[x0+(moveT.length/2)][y0+(moveT[0].length/2)]+field[x1][y1];
                    if(x1+x0 >= field.length || x1+x0 < 0 || y1+y0 >= field[0].length || y1+y0 < 0) continue;
                    if ((t < field[x1 + x0][y1 + y0] || field[x1 + x0][y1 + y0] == 0) && !obstacle[x1 + x0][y1 + y0]) {
                        field[x1 + x0][y1 + y0] = t;
                        queue.add(new int[]{x1 + x0, y1 + y0});
                    }
                }
            }
            //System.out.println(Arrays.deepToString(queue.toArray()));
            queue.remove(0);
        }
    }

    public static int[][] getPathTo(int x, int y) {
        ArrayList<int[]> path = new ArrayList<>();
        path.add(new int[] {x,y});
        while(field[path.get(path.size()-1)[0]][path.get(path.size()-1)[1]] > 0) {
            float min = -1; int minX = x; int minY = y;
            for(int x0 = -1; x0 <= 1; x0++) {
                for(int y0 = -1; y0 <= 1; y0++) {
                    int x1 = path.get(path.size()-1)[0]; int y1 = path.get(path.size()-1)[1];
                    if(x1+x0 >= field.length || x1+x0 < 0 || y1+y0 >= field[0].length || y1+y0 < 0) continue;
                    float v = field[x1 + x0][y1 + y0];
                    if ((v < min || min == -1) && !obstacle[x1 + x0][y1 + y0]) {
                        min = v;
                        minX = x1 + x0;
                        minY = y1 + y0;
                    }
                }
            }
            path.add(new int[] {minX,minY});
        }
        int[][] out = new int[path.size()][2];
        for(int i = 0; i < out.length; i++) out[i] = path.get(i);
        lastPath = out;
        return out;
    }

}
