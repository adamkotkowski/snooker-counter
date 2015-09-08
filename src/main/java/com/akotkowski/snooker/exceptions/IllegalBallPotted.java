package com.akotkowski.snooker.exceptions;

import com.akotkowski.snooker.Ball;

public class IllegalBallPotted extends RuntimeException {
    Ball ballPotted;

    public IllegalBallPotted(Ball ballPotted, String message) {
        super(message);
        this.ballPotted = ballPotted;
    }
}
