package com.akotkowski.snooker.model.impl;

import com.akotkowski.snooker.model.FrameModel;
import com.akotkowski.snooker.model.MatchModel;
import com.akotkowski.snooker.model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SimpleMatchModel implements MatchModel {

    int[] results = new int[2];

    String[] players = new String[2];

    int maxResult;

    int FrameCount;

    Player winner;

    boolean completed;

    List<? extends FrameModel> frames = new ArrayList<FrameModel>();

    Date date;

    boolean started;

    @Override
    public int getResult(Player player) {
        return results[player.getIndex()];
    }

    @Override
    public void setResult(Player player, int result1) {
        this.results[player.getIndex()] = result1;
    }

    @Override
    public int getMaxResult() {
        return maxResult;
    }

    @Override
    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    @Override
    public int getFrameCount() {
        return FrameCount;
    }

    @Override
    public void setFrameCount(int frameCount) {
        FrameCount = frameCount;
    }

    @Override
    public Player getWinner() {
        return winner;
    }

    @Override
    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String getPlayer(Player player) {
        return players[player.getIndex()];
    }

    @Override
    public void setPlayer(Player player, String name) {
        this.players[player.getIndex()] = name;
    }

    @Override
    public List<? extends FrameModel> getFrames() {
        return frames;
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
    public boolean isStarted() {
        return started;
    }

    @Override
    public void setStarted(boolean started) {
        this.started = started;
    }

    @Override
    public String toString() {
        return "SimpleMatchModel{" +
                "results=" + Arrays.toString(results) +
                ", players=" + Arrays.toString(players) +
                ", maxResult=" + maxResult +
                ", FrameCount=" + FrameCount +
                ", winner=" + winner +
                ", completed=" + completed +
                ", frames=" + frames +
                ", date=" + date +
                ", started=" + started +
                '}';
    }
}
