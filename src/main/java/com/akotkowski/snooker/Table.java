package com.akotkowski.snooker;

import java.io.Serializable;

public class Table implements Serializable {
    private static final long serialVersionUID = 2830676779553594618L;
    private int reds = 15;
    private Ball colorRemained = Ball.YELLOW;

    public void pot(Ball ball) {
        if (ball == Ball.RED) {
            if (reds > 0) reds--;
            else throw new RuntimeException("Red potted when no reds on the table");
        } else if (reds == 0 && ball.getType() == Ball.Type.COLOR) {
            if (ball == colorRemained) {
                colorRemained = Ball.getNext(ball);
            }
        }
    }

    public int getReds() {
        return reds;
    }

    public void setReds(int reds) {
        if (reds >= 0)
            this.reds = reds;
        else
            this.reds = 0;
    }

    public Ball getColorRemained() {
        return colorRemained;
    }

    public void setColorRemained(Ball colorRemained) {
        this.colorRemained = colorRemained;
    }

    public boolean isCleared() {
        return (reds == 0 && colorRemained == null) ? true : false;
    }

    public boolean isOnlyBlackOnTheTable() {
        return (reds == 0 && colorRemained == Ball.BLACK) ? true : false;
    }

    public int getScoreRemaining() {
        Ball color = colorRemained;
        int ret = 0;
        while (color != null) {
            ret += color.getValue();
            color = Ball.getNext(color);
        }
        ret += (this.reds * 1);
        return ret;
    }
}
