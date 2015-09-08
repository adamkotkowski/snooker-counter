package com.akotkowski.snooker;

public interface MatchEventListener {

    void registerPot(Ball yellow, Object source);

    void frameEnded(Object source);

    void matchEnded(Object source);

    void registerMultipleRedPoted(Object counterFragment);

    void registerFaul(int points, int redsGone, Object source);

    void registerResignFrame(Object ballsPanel);

    void registerResignMatch(Object ballsPanel);

}
