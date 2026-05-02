package org.firstinspires.ftc.teamcode.Utils.Movement;

import androidx.annotation.NonNull;

public class Position {

    private float[] xyr;
    public Position(float x,float y,float r) {
        xyr = new float[] {x,y,r};
    }

    public float getDistTo(Position pos) {
        return (float) Math.sqrt(Math.pow(pos.x()-xyr[0],2)+Math.pow(pos.y()-xyr[1],2));
    }

    public Position offset(float x, float y, float r) {
        return new Position(xyr[0]+x, xyr[1]+y,xyr[2]+r);
    }

    public float x() {
        return xyr[0];
    }
    public float y() {
        return xyr[1];
    }
    public float r() {
        return xyr[2];
    }
    public Position setX(float x) {
        xyr[0] = x;
        return this;
    }
    public Position setY(float y) {
        xyr[1] = y;
        return this;
    }
    public Position setR(float r) {
        xyr[2] = r;
        return this;
    }

    /**
     * Rotates the position by the given value in Degrees
     * @param rotation The angle (in Degrees) to rotate the movement
     */
    public Position rotateCoords(float rotation) {
        if(rotation != 0) {
            xyr[0] = (float) (-Math.sin(Math.toRadians(rotation))*xyr[1]+Math.cos(Math.toRadians(rotation))*xyr[0]);
            xyr[1] = (float) (Math.sin(Math.toRadians(rotation))*xyr[0]+Math.cos(Math.toRadians(rotation))*xyr[1]);
            xyr[2] += rotation;
        }
        return this;
    }

    @NonNull
    @Override
    public String toString() {
        return "X: "+xyr[0]+",Y: "+xyr[1]+",R: "+xyr[2];
    }
}
