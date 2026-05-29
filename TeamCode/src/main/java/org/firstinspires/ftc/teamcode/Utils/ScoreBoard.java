package org.firstinspires.ftc.teamcode.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ScoreBoard {

    /*
        [P1 4b][P2 4b][SCORE          16b 2B] 3B
     */

    File file;
    ArrayList<Byte> data = new ArrayList<>();

    public ScoreBoard(String filePath, boolean reset) {
        file = new File(filePath);
        try {
            if(reset) file.delete();
            file.createNewFile();
            byte[] in = new byte[Math.toIntExact(file.length())];
            FileInputStream i = new FileInputStream(file);
            i.read(in);
            i.close();
            for(byte b : in) data.add(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * adds a score to the scoreboard
     * @param P1 player 1's id# (0-15)
     * @param P2 player 2's id# (0-15)
     * @param score the score (0-65536)
     */
    public void addScore(int P1, int P2, int score) {
        byte[] out = new byte[3];
        out[0] = (byte) (-128+(P1+(P2*16)));
        out[1] = (byte) (-128 + (score % 256));
        out[2] = (byte) (-128+Math.floor((double) score /256));
        try {
            FileOutputStream o = new FileOutputStream(file);
            o.write(out);
            o.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(byte b : out) {
            data.add(b);
        }
    }

    /**
     * Returns the current leaderboard
     * @param l length of the leaderboard (to get the top 5 put in 5)
     * @param r false for a leaderboard of the top pairs, true for a leaderboard of the top scores.
     * @return an int array (array[x][0] is P1's number, array[x][1] is P2's number, array[x][2] is the score)
     */
    public int[][] getLeaderboard(int l, boolean r) {
        for(int n = 0; n < l+1; n++) {
            int highest = 0;
            int highestI = -1;
            for(int i = 2+(n*3); i < data.size(); i+= 3) {
                int s = ((data.get(i)+128)*256)+(data.get(i-1)+128);
                if(s > highest) {
                    highest = s;
                    highestI = i;
                }
            }
            if(highestI != -1) {
                Byte[] b = {data.get(highestI-2),data.get(highestI-1),data.get(highestI)};
                for(int i = 0; i < 3; i++)data.remove(highestI-2);
                for(int i = 2; i >= 0; i--) data.add(n*3,b[i]);
            }
        }
        int[][] out = new int[l][3];
        if(r) {
            for(int i = 0; i < l; i++) {
                if(i*3 < data.size()) {
                    out[i][0] = (data.get(i * 3) + 128) % 16;
                    out[i][1] = (int) Math.floor((double) (data.get(i * 3) + 128) / 16);
                    out[i][2] = data.get((i * 3) + 1) + 128 + ((data.get((i * 3) + 2) + 128) * 256);
                }
            }
        }else {
            ArrayList<Byte> pairs = new ArrayList<>();
            int i = 0;
            int c = 0;
            while(i < data.size() && c < l) {
                if(!pairs.contains(data.get(i))) {
                    out[c][0] = (data.get(i) + 128) % 16;
                    out[c][1] = (int) Math.floor((double) (data.get(i) + 128) / 16);
                    out[c][2] = data.get((i) + 1) + 128 + ((data.get((i) + 2) + 128) * 256);
                    pairs.add(data.get(i));
                    c++;
                }
                i+=3;
            }
        }
        return out;
    }

}
