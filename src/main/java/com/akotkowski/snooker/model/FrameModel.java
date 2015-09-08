package com.akotkowski.snooker.model;

import java.util.List;


public interface FrameModel {

    int getResult(Player player);

    void setResult(Player player, int result);

    Player getWinner();

    void setWinner(Player winner);

    boolean isCompleted();

    void setCompleted(boolean completed);

    List<? extends FrameEventModel> getFrameEvents();

    MatchModel getMatch();

    void setMatch(MatchModel match);

    Long getId();

    void setId(Long id);
}