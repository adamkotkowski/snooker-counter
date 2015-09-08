package com.akotkowski.snooker;

import com.akotkowski.snooker.model.impl.SimpleModelFactory;
import com.akotkowski.snooker.util.PotPossibilities;
import com.akotkowski.snooker.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Match {

    private Frame currentFrame = null;
    private MatchEventListener listener;
    private MatchModel match;
    private ModelFactory modelFactory = new SimpleModelFactory();

    public Match(int frameCount) {
        this.match = modelFactory.createMatch();
        init(frameCount);
    }

    public Match(int frameCount, MatchModel match2) {
        this.match = match2;
        init(frameCount);
    }

    public Match(MatchModel match2) {
        this.match = match2;
        this.init(match.getFrameCount());
        for (final FrameModel frame : match.getFrames()) {
            if (!frame.isCompleted()) {
                this.currentFrame = new Frame(frame, modelFactory);
                currentFrame.setMatch(this);
            }
        }
        this.recountFrames();
    }

    public void init(int frameCount) {
        match.setFrameCount(frameCount);
    }

    public void startNewFrame() {
        FrameModel frame = modelFactory.createFrame();
        List<FrameModel> l = (List<FrameModel>) match.getFrames();
        l.add(frame);
        frame.setMatch(match);
        currentFrame = new Frame(frame, modelFactory);
        frame.setMatch(match);
        currentFrame.setMatch(this);
        frame.setMatch(this.getMatch());
    }

    public void resignMatch(Player player) {
        this.currentFrame.resign(player);
        this.recountFrames();
        if (this.listener != null) this.listener.matchEnded(this);
    }

    private void recountFrames() {
        Map<Player, Integer> results = new HashMap<Player, Integer>();

        for (FrameModel frame : match.getFrames()) {
            if (frame.isCompleted())
                results.put(frame.getWinner(), results.getOrDefault(frame.getWinner(), 0) + 1);
        }
        if (results.getOrDefault(Player.ONE, 0) > results.getOrDefault(Player.TWO, 0)) {
            if (results.getOrDefault(Player.ONE, 0) >= match.getMaxResult()) {
                match.setWinner(Player.ONE);
                match.setCompleted(true);

            }
        } else {
            if (results.getOrDefault(Player.TWO, 0) >= match.getMaxResult()) {
                match.setWinner(Player.TWO);
                match.setCompleted(true);

            }
        }
        match.setResult(Player.ONE, results.getOrDefault(Player.ONE, 0));
        match.setResult(Player.TWO, results.getOrDefault(Player.TWO, 0));
    }

    public Frame getCurrentFrame() {
        return currentFrame;
    }

    public int getResult(Player player) {
        return match.getResult(player);
    }

    public void setMaxResult(int maxResult) {
        if (maxResult >= match.getFrames().size())
            match.setMaxResult(maxResult);
    }

    public String getPlayer(Player player) {
        return match.getPlayer(player);
    }

    public void setPlayer(Player player, String playerName) {
        match.setPlayer(player, playerName);
    }

    public void registerPot(Ball ball, Player player) {
        this.getCurrentFrame().registerPot(ball, player);
    }

    public void registerFreeBall(Ball ball, Player player) {
        this.getCurrentFrame().registerFreeBall(ball, player);
    }

    public void onFrameCompleted() {
        boolean endedTemp = match.isCompleted();
        this.recountFrames();
        if (endedTemp == false && match.isCompleted() == true) {
            if (this.listener != null)
                this.listener.matchEnded(this);
        } else {
            if (this.listener != null)
                this.listener.frameEnded(this);
        }

    }


    public List<FrameEventModel> getBreaks() {
        List<FrameEventModel> ret = new ArrayList<FrameEventModel>();
        for (FrameModel frame : match.getFrames()) {
            List<FrameEventModel> brakeInFrame = Frame.getBreaks(frame);
            ret.addAll(brakeInFrame);
        }
        // BUBBLE sort :)
        // TODO: improve
        List<FrameEventModel> ret1 = new ArrayList<FrameEventModel>();
        while (!ret.isEmpty()) {
            FrameEventModel best = ret.get(0);
            for (FrameEventModel b : ret) {
                if (b.getScore() > best.getScore()) {
                    best = b;
                }
            }
            ret1.add(best);
            ret.remove(best);
        }
        return ret1;
    }

    public List<FrameEventModel> getEvents() {
        List<FrameEventModel> list = new ArrayList<FrameEventModel>();
        for (FrameModel frame : match.getFrames()) {
            for (FrameEventModel inFrame : frame.getFrameEvents()) {
                list.add(inFrame);
            }
        }
        return list;
    }

    public PotPossibilities getCurrentPotPossibilities(Player pl) {
        if (this.getCurrentFrame() == null) return null;
        return this.getCurrentFrame().getPotPossiibilities(pl);
    }

    public void endMatch() {
        match.setCompleted(true);
    }

    public MatchEventListener getListener() {
        return listener;
    }

    public void setListener(MatchEventListener listener) {
        this.listener = listener;
    }

    public int getMaxFrameCount() {
        return match.getMaxResult() * 2 - 1;
    }

    public void registerMultipleRedPot(int i, Player player) {
        this.getCurrentFrame().registerMultipleRedPot(i, player);

    }

    public void registerFaul(int i, Player actualPlayer, int reds) {
        this.getCurrentFrame().registerFaul(Ball.getByValue(i), actualPlayer, reds);
    }

    public MatchModel getMatch() {
        return match;
    }

    public void setMatch(MatchModel match) {
        this.match = match;
    }

    public ModelFactory getModelFactory() {
        return modelFactory;
    }

    public void setModelFactory(ModelFactory _domainFactory) {
        modelFactory = _domainFactory;
    }

    public boolean undo(int n) {
        boolean result = false;
        for (int i = 0; i < n; i++) if (this.undo()) result = true;
        return result;
    }

    public boolean undo() {
        if (this.getCurrentFrame() == null) return false;
        if (this.getCurrentFrame().undo() == false) {
            if (this.match.getFrames().size() > 1) {
                modelFactory.removeFrame(this.currentFrame.getFrame());
                this.match.getFrames().remove(this.currentFrame.getFrame());
                final FrameModel frame = this.match.getFrames().get(this.match.getFrames().size() - 1);
                this.currentFrame = new Frame(frame);
                this.getCurrentFrame().undo();
            }
        } else {
            return true;
        }
        return false;
    }

    public void surrenderFrame(Player player) {
        this.getCurrentFrame().registerRetire(player);
        this.recountFrames();
        if (this.match.isCompleted()) {
            if (this.listener != null) this.listener.matchEnded(this);
        }
    }

    public void surrenderMatch(Player player) {

        this.getCurrentFrame().registerRetire(player);
        FrameEventModel fe = modelFactory.createFrameEvent();
        fe.setPlayerScored(player);
        fe.setType(FrameEventModel.Type.MATCH_SURRENDER);
        fe.setFrame(this.getCurrentFrame().getFrame());
        List<FrameEventModel> lst = (List<FrameEventModel>) this.getCurrentFrame().getFrame().getFrameEvents();
        lst.add(fe);
        this.getCurrentFrame().getFrame().setWinner(player.getOponent());
        this.getCurrentFrame().getFrame().setCompleted(true);
        this.recountFrames();
        this.getMatch().setWinner(player.getOponent());
        this.getMatch().setCompleted(true);
        if (this.listener != null) this.listener.matchEnded(this);
    }

    public FrameModel getFrameNumber(int nmb) {
        if (nmb < 0 || nmb > this.getMatch().getFrames().size() - 1)
            return null;
        return this.getMatch().getFrames().get(nmb);
    }

}
