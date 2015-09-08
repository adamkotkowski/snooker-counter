package com.akotkowski.snooker;


public enum Ball {
    RED(1, "red"),
    BLACK(7, "black"),
    PINK(6, "pink"),
    BLUE(5, "blue"),
    BROWN(4, "brown"),
    GREEN(3, "green"),
    YELLOW(2, "yellow");


    public enum Type {
        RED, COLOR
    }

    private Type type;

    private final int value;
    private String name;

    Ball(int value, String name) {
        this.value = value;
        this.name = name;

        if (this.value == 1) {
            this.type = Type.RED;
        } else {
            this.type = Type.COLOR;
        }
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static Ball getByValue(int score) {
        for (Ball b : Ball.values()) {
            if (b.getValue() == score) return b;
        }
        return null;
    }

    public static Ball getNext(Ball ball) {
        if (ball == BLACK) return null;
        if (ball == RED) throw new RuntimeException("Cannot get next color ball of RED ball");
        return getByValue(ball.getValue() + 1);
    }

    public Type getType() {
        return type;
    }


}
