package com.akotkowski.snooker.model.impl;

import com.akotkowski.snooker.Ball;
import com.akotkowski.snooker.model.FrameEventModel;
import com.akotkowski.snooker.model.FrameModel;
import com.akotkowski.snooker.model.Player;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimpleFrameEventModel implements FrameEventModel {

    FrameModel frame;

    Type type;

    Player playerScored;

    int score;

    int ballsPottedCount;

    boolean open;

    List<Ball> ballsPotted = new ArrayList<Ball>();

    int freeBallScore;

    boolean freeBall;

    boolean inColors;

    Date date;

    long shotTime;

    @Override
    public void setFrame(FrameModel frameModel) {
        this.frame = frameModel;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public Player getPlayerScored() {
        return playerScored;
    }

    @Override
    public void setPlayerScored(Player playerScored) {
        this.playerScored = playerScored;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int getBallsPottedCount() {
        return ballsPottedCount;
    }

    @Override
    public void setBallsPottedCount(int ballsPottedCount) {
        this.ballsPottedCount = ballsPottedCount;
    }

    @Override
    public boolean isOpen() {
        return open;
    }

    @Override
    public void setOpen(boolean open) {
        this.open = open;
    }

    @Override
    public List<Ball> getBallsPotted() {
        return ballsPotted;
    }

    public void setBallsPotted(List<Ball> ballsPotted) {
        this.ballsPotted = ballsPotted;
    }

    @Override
    public int getFreeBallScore() {
        return freeBallScore;
    }

    @Override
    public void setFreeBallScore(int freeBallScore) {
        this.freeBallScore = freeBallScore;
    }

    @Override
    public boolean isInColors() {
        return inColors;
    }

    @Override
    public void setInColors(boolean inColors) {
        this.inColors = inColors;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public long getShotTime() {
        return shotTime;
    }

    @Override
    public void setShotTime(long shotTime) {
        this.shotTime = shotTime;
    }

    @Override
    public String toString() {
        return "SimpleFrameEventModel{" +
                ", type=" + type +
                ", playerScored=" + playerScored +
                ", score=" + score +
                ", ballsPottedCount=" + ballsPottedCount +
                ", open=" + open +
                ", ballsPotted=" + ballsPotted +
                ", freeBallScore=" + freeBallScore +
                ", freeBall=" + freeBall +
                ", inColors=" + inColors +
                ", date=" + date +
                ", shotTime=" + shotTime +
                '}';
    }
}
