package com.akotkowski.snooker.util;

import com.akotkowski.snooker.Ball;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class PotPossibilities implements Serializable {
    private static final long serialVersionUID = -6081078542538970366L;

    public static final PotPossibilities RED = new PotPossibilities(Ball.Type.RED);
    public static final PotPossibilities COLOR = new PotPossibilities(Ball.Type.COLOR);

    private Ball leastBall = null;
    private Ball.Type ballType = null;
    private List<Ball> ballsPossible = new ArrayList<Ball>();

    public PotPossibilities() {

    }

    public PotPossibilities(Ball leastBall) {
        this.setLeastBall(leastBall);
    }
    public PotPossibilities(List<Ball> ballsPossible) {
        this.ballsPossible = ballsPossible;
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

    public boolean canBePotted(Ball ball){
        if(ballType!=null){
            if(ballType == Ball.Type.RED){
                return ball == Ball.RED;
            }else{
               return ball.getType() == Ball.Type.COLOR;
            }
        }
        if(getLeastBall()!=null){
            return ball == getLeastBall();
        }
        return ballsPossible.contains(ball);
    }

    @Override
    public String toString() {
        return "PotPossibilities{" +
                "leastBall=" + leastBall +
                ", ballType=" + ballType +
                '}';
    }
}