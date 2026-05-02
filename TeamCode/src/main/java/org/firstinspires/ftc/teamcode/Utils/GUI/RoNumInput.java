package org.firstinspires.ftc.teamcode.Utils.GUI;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class RoNumInput extends RoGraphic {

    /*
    Credits to Dad for this idea
     */

    Float currentInput = 0.00f;
    int ints;
    int decimals;
    int selected2 = 1;
    int selected = 1;
    Telemetry tem;
    String label;

    public RoNumInput(String label, int integers, int decimals, Telemetry telemetry) {
        ints = integers;
        this.decimals = decimals;
        tem = telemetry;
        this.label=label;
    }

    @Override
    public Object getOutput() {
        return currentInput;
    }

    @Override
    public void setOutput(Object o) {
        if(o instanceof Float) {
            currentInput = (Float)o;
        }
    }

    @Override
    public void interactRight() {
        if(selected2 < ints+decimals) {
            selected2++;
        }
    }

    @Override
    public void interactLeft() {
        if(selected2 > 1) {
            selected2--;
        }
    }

    @Override
    public void interactUp() {

    }

    @Override
    public void interactDown() {

    }

    @Override
    public void interact() {
        currentInput += (float) Math.pow(10,ints-selected2);
        if((float)Math.floor(currentInput/Math.pow(10,ints-selected2)+0.0005)%10==0) {
            currentInput -= (float) Math.pow(10,ints-selected2+1);
        }
        currentInput = (float) Math.round(currentInput*Math.pow(10,decimals))/(float)Math.pow(10,decimals);
    }

    @Override
    public void render(boolean selected) {

        if(selected) {
            this.selected=selected2;
        } else {
            this.selected=0;
        }

        String data;
        if(selected) {
            data = "▓"+label+": ";
        } else {
            data = " "+label+": ";
        }
        String dataNum = ((Integer)((int)Math.floor(currentInput))).toString();
        StringBuilder builder0 = new StringBuilder();
        int b = ints-dataNum.length()+1;
        int a = dataNum.length();
        for(int i = 0; i < dataNum.length(); i++) {
            if(b == this.selected) {
                builder0.append(">");
                builder0.append(dataNum.charAt(i));
                builder0.append("<");
            } else {
                builder0.append("[");
                builder0.append(dataNum.charAt(i));
                builder0.append("]");
            }
            b++;
        }
        dataNum = builder0.toString();
        b = ints-a;
        while(a < ints) {
            if(b == this.selected) {
                dataNum = ">0<" + dataNum;
            } else {
                dataNum = "[0]" + dataNum;
            }
            a++;
            b--;
        }
        data += dataNum;
        float temp = (float)(currentInput-Math.floor(currentInput));
        temp = (float)Math.round(temp*Math.pow(10,decimals))/(float)Math.pow(10,decimals);
        dataNum = ((Float)temp).toString();
        StringBuilder builder = new StringBuilder();
        a = 0;
        b = ints+1;
        for(int i = 2; i < dataNum.length(); i++) {
            if(b == this.selected) {
                builder.append(">");
                builder.append(dataNum.charAt(i));
                builder.append("<");
            } else {
                builder.append("[");
                builder.append(dataNum.charAt(i));
                builder.append("]");
            }
            a++;
            b++;
        }
        while(a < decimals) {
            if(b == this.selected) {
                builder.append(">0<");
            } else {
                builder.append("[0]");
            }
            a++;
            b++;
        }
        data += "."+builder;
        tem.addData("",data);
    }

    @Override
    public boolean takeControls() {
        return false;
    }
}
