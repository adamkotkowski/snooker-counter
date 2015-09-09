package com.akotkowski.snooker;

import com.akotkowski.snooker.model.MatchModel;
import com.akotkowski.snooker.model.ModelFactory;
import com.akotkowski.snooker.model.Player;

public class MatchBuilder {


    int frames = 1;

    private String player1 = "player 1";

    private String player2 = "player 2";

    private ModelFactory modelFactory;

    private MatchModel matchModel;

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

    public MatchBuilder matchModel(MatchModel matchModel) {
        this.matchModel = matchModel;
        return this;
    }

    public Match build() {
        Match match;
        if(modelFactory!=null){
            match = matchModel!=null? new Match(matchModel, modelFactory) : new Match(frames, modelFactory);
        }else{
            match = matchModel!=null? new Match(matchModel) :new Match(frames);
        }
        if(matchModel==null){
            match.setPlayer(Player.ONE, player1);
            match.setPlayer(Player.TWO, player2);
        }
        return match;
    }

    public MatchBuilder modelFactory(ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
        return this;

    }
}
