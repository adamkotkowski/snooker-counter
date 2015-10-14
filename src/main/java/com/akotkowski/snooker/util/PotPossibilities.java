package com.akotkowski.snooker.util;

import com.akotkowski.snooker.Ball;

import java.io.Serializable;


public class PotPossibilities implements Serializable {
    private static final long serialVersionUID = -6081078542538970366L;

    public static final PotPossibilities RED = new PotPossibilities(Ball.Type.RED);
    public static final PotPossibilities COLOR = new PotPossibilities(Ball.Type.COLOR);

    private Ball leastBall = null;
    private Ball.Type ballType = null;

    public PotPossibilities() {

    }

    public PotPossibilities(Ball leastBall) {
        this.setLeastBall(leastBall);
    }

    public PotPossibilities(Ball.Type ballType) {
        this.ballType = ballType;
    }

    public Ball getLeastBall() {
        return leastBall;
    }

    public void setLeastBall(Ball leastBall) {
        this.leastBall = leastBall;
    }

    public Ball.Type getBallType() {
        return ballType;
    }

    public void setBallType(Ball.Type ballType) {
        this.ballType = ballType;
    }

    @Override
    public String toString() {
        return "PotPossibilities{" +
                "leastBall=" + leastBall +
                ", ballType=" + ballType +
                '}';
    }
}