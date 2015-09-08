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

    void setFrames(List<? extends FrameModel> list);

    Date getDate();

    void setDate(Date date);

    int getCurrentBreak();

    Long getId();

    void setId(Long id);

    void setCurrentBreak(int currentBreak);

    boolean isStarted();

    void setStarted(boolean started);

    int getReds();

    void setReds(int reds);

}
