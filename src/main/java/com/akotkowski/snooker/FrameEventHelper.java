package com.akotkowski.snooker;

import com.akotkowski.snooker.exceptions.IllegalBallPotted;
import com.akotkowski.snooker.model.FrameEventModel;

import java.io.Serializable;

public class FrameEventHelper implements Serializable {

    public static int pot(FrameEventModel fe, Ball ball) {
        Ball last = FrameEventHelper.getLastPot(fe);
        if (last != null && last.getType() == Ball.Type.COLOR && ball.getType() == Ball.Type.COLOR) {
            if (!(isFreeBall(fe) && fe.getBallsPotted().size() < 2)) {
                if (!fe.isInColors())
                    throw new IllegalBallPotted(ball, "Illegal ball potted - COLOR after COLOR when still reds on the table");
            }
        } else if (last != null && last.getType() == Ball.Type.RED && ball.getType() == Ball.Type.RED) {
            throw new IllegalBallPotted(ball, "Illegal ball potted - RED after RED");
        }
        fe.setScore(fe.getScore() + ball.getValue());
        FrameEventHelper.addBallPoted(fe, ball);
        return fe.getScore();
    }

    public static void addBallPoted(FrameEventModel fe, Ball ball) {
        fe.getBallsPotted().add(ball);
    }

    public static int potReds(FrameEventModel fe, int amount) {
        Ball last = FrameEventHelper.getLastPot(fe);
        Ball ball = Ball.RED;
        if (last != null && last.getType() == Ball.Type.RED) {
            throw new RuntimeException("Illegal ball potted - red after red");
        }
        fe.setScore(fe.getScore() + ball.getValue() * amount);
        for (int i = 0; i < amount; i++)
            FrameEventHelper.addBallPoted(fe, ball);

        return fe.getScore();
    }

    public static Ball getLastPot(FrameEventModel fe) {
        if (fe.getBallsPotted() == null) return null;
        int size = fe.getBallsPotted().size();
        if (size == 0) return null;
        return fe.getBallsPotted().get(size - 1);
    }

    public static void setOpen(FrameEventModel fe, boolean open) {
        fe.setOpen(open);
    }

    public static boolean isOpen(FrameEventModel fe) {
        return fe.isOpen();
    }

    public static Ball removeLastPot(FrameEventModel event) {
        int size = event.getBallsPotted().size();
        if (size == 0) return null;
        Ball ret = event.getBallsPotted().get(size - 1);
        event.getBallsPotted().remove(size - 1);
        return ret;
    }

    public static boolean isFreeBall(FrameEventModel fe) {
        return fe.getFreeBallScore()!=0;
    }
    public static boolean isBreak(FrameEventModel fe){ return fe.getType()== FrameEventModel.Type.BREAK; }
    public static boolean isFoul(FrameEventModel fe){ return fe.getType()== FrameEventModel.Type.FOUL; }
    public static boolean isResignFrame(FrameEventModel fe){ return fe.getType()== FrameEventModel.Type.FRAME_SURRENDER; }
    public static boolean isResignMatch(FrameEventModel fe){ return fe.getType()== FrameEventModel.Type.MATCH_SURRENDER; }

}
