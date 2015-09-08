package com.akotkowski.snooker.model.impl;

import com.akotkowski.snooker.model.FrameEventModel;
import com.akotkowski.snooker.model.FrameModel;
import com.akotkowski.snooker.model.MatchModel;
import com.akotkowski.snooker.model.Player;

import java.util.ArrayList;
import java.util.List;

public class SimpleFrameModel implements FrameModel {

    int[] results = new int[2];

    Player winner;

    boolean completed;

    List<? extends FrameEventModel> frameEvents = new ArrayList<FrameEventModel>();

    MatchModel match;

    Long id;


    @Override
    public int getResult(Player player) {
        return results[player.getIndex()];
    }

    @Override
    public void setResult(Player player, int result) {
        results[player.getIndex()] = result;
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

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public List<? extends FrameEventModel> getFrameEvents() {
        return frameEvents;
    }

    public void setFrameEvents(List<? extends FrameEventModel> frameEvents) {
        this.frameEvents = frameEvents;
    }

    @Override
    public MatchModel getMatch() {
        return match;
    }

    @Override
    public void setMatch(MatchModel match) {
        this.match = match;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
