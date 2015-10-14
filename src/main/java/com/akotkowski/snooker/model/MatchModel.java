package com.akotkowski.snooker.model;

import java.util.Date;
import java.util.List;

public interface MatchModel {

    int getResult(Player player);

    void setResult(Player player, int result1);

    int getMaxResult();

    void setMaxResult(int maxResult);

    int getFrameCount();

    void setFrameCount(int frameCount);

    Player getWinner();

    void setWinner(Player winner);

    boolean isCompleted();

    void setCompleted(boolean completed);

    String getPlayer(Player player);

    void setPlayer(Player player, String name);

    List<? extends FrameModel> getFrames();

    Date getDate();

    void setDate(Date date);

    boolean isStarted();

    void setStarted(boolean started);

}
