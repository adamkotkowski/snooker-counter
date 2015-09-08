package com.akotkowski.snooker.model;

/**
 * Created by adam on 07/09/15.
 */
public enum Player {
    ONE(0), TWO(1);

    private final int index;

    Player(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public Player getOponent() {
        return this == ONE ? TWO : ONE;
    }
}
