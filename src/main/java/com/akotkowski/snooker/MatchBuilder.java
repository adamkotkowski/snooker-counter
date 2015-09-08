package com.akotkowski.snooker;

import com.akotkowski.snooker.model.ModelFactory;
import com.akotkowski.snooker.model.Player;

public class MatchBuilder {


    int frames = 1;

    String player1 = "player 1";

    String player2 = "player 2";

    ModelFactory modelFactory;

    public MatchBuilder frames(int frames) {
        this.frames = frames;
        return this;
    }

    public MatchBuilder player1(String player) {
        this.player1 = player;
        return this;
    }

    public MatchBuilder player2(String player) {
        this.player2 = player;
        return this;
    }

    public Match build() {
        Match match;
        if(modelFactory!=null){
            match = new Match(frames, modelFactory);
        }else{
            match = new Match(frames);
        }

        match.setPlayer(Player.ONE, player1);
        match.setPlayer(Player.TWO, player2);
        return match;
    }

    public MatchBuilder modelFactory(ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
        return this;

    }
}
