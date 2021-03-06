package com.akotkowski.snooker;

import com.akotkowski.snooker.model.*;
import com.akotkowski.snooker.model.impl.SimpleModelFactory;
import com.akotkowski.snooker.util.PotPossibilities;
import com.akotkowski.snooker.util.Util;

import java.util.*;

public class Match {

    private Frame currentFrame = null;
    private MatchEventListener listener;
    private MatchModel match;
    private final ModelFactory modelFactory;


    public Match(int frameCount, ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
        this.match = modelFactory.createMatch();
        init(frameCount);
    }

    public Match(int frameCount) {
        this.modelFactory = new SimpleModelFactory();
        this.match = modelFactory.createMatch();
        init(frameCount);
    }

    public Match(int frameCount, MatchModel match2, ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
        this.match = match2;
        init(frameCount);
    }

    public Match(MatchModel match) {
        this(match, new SimpleModelFactory());
    }

    public Match(MatchModel match, ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
        this.match = match;
        this.init(match.getFrameCount());
        for (final FrameModel frame : match.getFrames()) {
            if (!frame.isCompleted()) {
                this.currentFrame = new Frame(frame, modelFactory, this);
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
        currentFrame = new Frame(frame, modelFactory, this);
        frame.setMatch(match);
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
                results.put(frame.getWinner(), Util.getOrDefault(results, frame.getWinner(), 0) + 1);
        }
        if (Util.getOrDefault(results, Player.ONE, 0) > Util.getOrDefault(results, Player.TWO, 0)) {
            if (Util.getOrDefault(results, Player.ONE, 0) >= match.getMaxResult()) {
                match.setWinner(Player.ONE);
                match.setCompleted(true);

            }
        } else {
            if (Util.getOrDefault(results, Player.TWO, 0) >= match.getMaxResult()) {
                match.setWinner(Player.TWO);
                match.setCompleted(true);

            }
        }
        match.setResult(Player.ONE, Util.getOrDefault(results, Player.ONE, 0));
        match.setResult(Player.TWO, Util.getOrDefault(results, Player.TWO, 0));
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
        this.recountFrames();
        if (match.isCompleted() == true) {
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
        Collections.sort(ret, new Comparator<FrameEventModel>() {
            @Override
            public int compare(FrameEventModel o1, FrameEventModel o2) {
                return Integer.compare(o1.getScore(), o2.getScore());
            }
        });
        return ret;
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
                this.currentFrame.setMatch(this);
                Player player = this.getCurrentFrame().getFrame().getWinner();
                if(player!=null) {
                    getMatch().setResult(player, getMatch().getResult(player) - 1);
                }
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

    public void registerMiss() {
        this.getCurrentFrame().registerMiss();
    }


    public void setReds(int reds) {
        this.getMatch().setReds(reds);
    }
}
