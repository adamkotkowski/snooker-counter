package com.akotkowski.snooker;

import com.akotkowski.snooker.exceptions.FreeBallException;
import com.akotkowski.snooker.exceptions.IllegalBallPotted;
import com.akotkowski.snooker.model.FrameEventModel;
import com.akotkowski.snooker.model.FrameModel;
import com.akotkowski.snooker.model.ModelFactory;
import com.akotkowski.snooker.model.Player;
import com.akotkowski.snooker.model.impl.SimpleModelFactory;
import com.akotkowski.snooker.util.PotPossibilities;

import java.io.Serializable;
import java.util.*;

public class Frame implements Serializable {
    private static final long serialVersionUID = 7344395968895146751L;

    public static final int FRAME_ENDED = 1;
    private Match match;

    Table table = new Table();

    FrameModel frameModel;

    private final ModelFactory modelFactory;

    public Frame() {
        this(new SimpleModelFactory().createFrame(), new SimpleModelFactory());
    }

    public Frame(FrameModel frameModel) {
        this(frameModel, new SimpleModelFactory());
    }

    public Frame(FrameModel frameModel, ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
        this.frameModel = frameModel;
        for (FrameEventModel fe : this.getFrame().getFrameEvents()) {
            if (fe.getType() == FrameEventModel.Type.BREAK) {
                if (fe.getBallsPotted() != null) {
                    int i = 0;
                    for (Ball ball : fe.getBallsPotted()) {
                        if (i == 0 && fe.isFreeBall())
                            continue;
                        this.table.pot(ball);
                        i++;
                    }
                }
            } else if (fe.getType() == FrameEventModel.Type.FOUL) {
                int potCount = fe.getBallsPotted().size();
                if (potCount != 0) {
                    this.table.setReds(this.table.getReds() - potCount);

                }
            }
        }
        this.recalculateScore();
    }


    public Player getWinner() {
        if (this.getFrame().isCompleted() == false)
            throw new RuntimeException("Cannot get winner of a frame, that is not completed");
        return this.getFrame().getWinner();
    }

    public int getResult(Player player) {
        return this.getFrame().getResult(player);
    }

    public void setResult(Player player, int result) {
        this.getFrame().setResult(player, result);
    }

    public void registerPot(Ball ball, Player player) {
        if (this.getFrame().isCompleted()) {
            throw new RuntimeException("Cannot pot in completed frame");
        }
        FrameEventModel event = getlastEvent();
        if (this.table.getReds() == 0 && event != null) {
            event.setInColors(true);
        }
        if (event != null && event.getType() == FrameEventModel.Type.BREAK && event.isOpen() && event.getPlayerScored() == player) {
            // add pot to the current break
            FrameEventHelper.pot(this.getlastEvent(), ball);
        } else {
            if (this.getlastEvent() != null && this.getlastEvent().getType() == FrameEventModel.Type.BREAK)
                FrameEventHelper.setOpen(this.getlastEvent(), false);
            // start new brake
            FrameEventModel t = modelFactory.createFrameEvent();
            t.setPlayerScored(player);
            FrameEventHelper.pot(t, ball);
            t.setFrame(this.getFrame());
            t.setType(FrameEventModel.Type.BREAK);
            t.setOpen(true);
            if (this.table.getReds() == 0) {
                t.setInColors(true);
            }
            List<FrameEventModel> l = (List<FrameEventModel>) this.getFrame().getFrameEvents();
            l.add(t);

        }
        this.table.pot(ball);
        // FIX for:
        // if someone pots last red ball, than pots yellow, then Table will set next color to green, which is bad behavior (it should be yellow)
        List<Ball> ballsPotted = this.getlastEvent().getBallsPotted();
        int ballsPottedSize = ballsPotted.size();
        if (ballsPottedSize > 1 && ballsPotted.get(ballsPottedSize - 1) == Ball.YELLOW && ballsPotted.get(ballsPottedSize - 2) == Ball.RED && this.table.getColorRemained() == Ball.GREEN) {
            table.setColorRemained(Ball.YELLOW);
        }
        // END of FIX

        this.recalculateScore();
        if (this.table.isCleared()) {
            if (this.getResult(Player.ONE) != this.getResult(Player.TWO)) {
                if (this.getlastEvent().getType() == FrameEventModel.Type.BREAK)
                    FrameEventHelper.setOpen(this.getlastEvent(), false);
                this.getFrame().setCompleted(true);
                calculateWinner();
                this.match.onFrameCompleted();
            } else {
                // fight for the black
                if (this.getlastEvent().getType() == FrameEventModel.Type.BREAK)
                    FrameEventHelper.setOpen(this.getlastEvent(), false);
                table.setColorRemained(Ball.BLACK);
            }
        }
    }

    public void registerFreeBall(Ball ball, Player player) {
        if (this.getFrame().isCompleted()) {
            throw new IllegalBallPotted(ball, "Cannot pot in ended frame");
        }
        FrameEventModel lastEvent = this.getlastEvent();
        if (lastEvent == null) {
            throw new FreeBallException("Free ball cannot be potted without any foul");
        }

        FrameEventModel event = getlastEvent();
        if (lastEvent.getType() != FrameEventModel.Type.FOUL || lastEvent.getPlayerScored() != player) {
            throw new FreeBallException("Free ball cannot be potted by player who commited the foul");
        }
        // start new brake
        FrameEventModel t = modelFactory.createFrameEvent();
        t.setPlayerScored(player);
        FrameEventHelper.pot(t, ball);
        t.setType(FrameEventModel.Type.BREAK);
        t.setOpen(true);
        if (this.getTable().getReds() > 0) {
            t.setScore(1);
            t.setFreeBallScore(1);
        } else {
            t.setScore(this.getTable().getColorRemained().getValue());
            t.setFreeBallScore(this.getTable().getColorRemained().getValue());
        }
        List<FrameEventModel> l = (List<FrameEventModel>) this.getFrame().getFrameEvents();
        t.setFrame(this.getFrame());
        l.add(t);
        if (this.table.getReds() == 0) {
            this.getlastEvent().setInColors(true);
        }
        this.recalculateScore();

    }

    public void registerMultipleRedPot(int i, Player player) {
        if (i > table.getReds()) {
            return;
            //throw new RuntimeException("Not enough reds on the table.");
        }
        if (this.getFrame().isCompleted()) {
            throw new RuntimeException("Cannot pot in ended frame");
        }
        FrameEventModel event = getlastEvent();
        if (event != null && event.getType() == FrameEventModel.Type.BREAK && event.isOpen() && event.getPlayerScored() == player) {
            // add pot to the current break
            FrameEventHelper.potReds(this.getlastEvent(), i);
        } else {
            if (this.getlastEvent() != null && this.getlastEvent().getType() == FrameEventModel.Type.BREAK)
                FrameEventHelper.setOpen(this.getlastEvent(), false);
            // start new brake
            FrameEventModel t = modelFactory.createFrameEvent();
            t.setPlayerScored(player);
            t.setType(FrameEventModel.Type.BREAK);
            FrameEventHelper.potReds(t, i);
            List<FrameEventModel> l = (List<FrameEventModel>) this.getFrame().getFrameEvents();
            t.setFrame(this.getFrame());
            l.add(t);
        }
        for (int k = 0; k < i; k++)
            this.table.pot(Ball.RED);

        this.recalculateScore();

    }

    public void registerMiss() {
        if (this.getlastEvent() != null && this.getlastEvent().getType() == FrameEventModel.Type.BREAK)
            FrameEventHelper.setOpen(this.getlastEvent(), false);
        if ((this.table.isOnlyBlackOnTheTable() && Math.abs(this.getResult(Player.ONE) - this.getResult(Player.TWO)) > 7) || this.table.isCleared()) {
            this.getFrame().setCompleted(true);
            calculateWinner();
            this.match.onFrameCompleted();
        }

    }

    public FrameEventModel getlastEvent() {
        if (this.getFrame().getFrameEvents().size() == 0) return null;
        return this.getFrame().getFrameEvents().get(this.getFrame().getFrameEvents().size() - 1);
    }

    public void registerFaul(Ball ball, Player player, int reds) {
        List<FrameEventModel> l = (List<FrameEventModel>) this.getFrame().getFrameEvents();

        FrameEventModel event = modelFactory.createFrameEvent();
        event.setPlayerScored(player.getOponent());
        event.setScore(ball.getValue() < 4 ? 4 : ball.getValue());
        event.setType(FrameEventModel.Type.FOUL);
        int redsOnTable = table.getReds();
        redsOnTable -= reds;
        if (redsOnTable < 0) {
            reds += redsOnTable;
            redsOnTable = 0;
        }
        for (int m = 0; m < reds; m++) {
            FrameEventHelper.addBallPoted(event, Ball.RED);
        }
        table.setReds(redsOnTable);
        event.setFrame(this.getFrame());
        l.add(event);
        this.recalculateScore();
    }

    private void recalculateScore() {
        Map<Player, Integer> results = new HashMap<Player, Integer>();
        for (FrameEventModel e : this.getFrame().getFrameEvents()) {
            results.put(e.getPlayerScored(), results.getOrDefault(e.getPlayerScored(), 0) + e.getScore());
        }
        this.getFrame().setResult(Player.ONE, results.getOrDefault(Player.ONE, 0));
        this.getFrame().setResult(Player.TWO, results.getOrDefault(Player.TWO, 0));
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public void registerRetire(Player player) {
        this.getFrame().setCompleted(true);
        FrameEventModel fe = modelFactory.createFrameEvent();
        fe.setPlayerScored(player);
        fe.setType(FrameEventModel.Type.FRAME_SURRENDER);
        fe.setDate(new Date());
        List<FrameEventModel> lst = (List<FrameEventModel>) this.getFrame().getFrameEvents();
        fe.setFrame(this.getFrame());
        lst.add(fe);
        this.getFrame().setWinner(player.getOponent());
        this.getFrame().setCompleted(true);
        this.recalculateScore();
        this.match.onFrameCompleted();

    }

    private void calculateWinner() {
        if (this.getFrame().isCompleted()) {
            if (this.getFrame().getResult(Player.ONE) > this.getFrame().getResult(Player.TWO)) {
                this.getFrame().setWinner(Player.ONE);
            } else {
                this.getFrame().setWinner(Player.TWO);
            }
        }
    }

    public Match getMatch() {
        return match;
    }

    public void resign(Player player) {
        this.getFrame().setWinner(player);
        this.getFrame().setCompleted(true);
        this.match.onFrameCompleted();
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public static List<FrameEventModel> getBreaks(FrameModel frame) {
        ArrayList<FrameEventModel> ret = new ArrayList<FrameEventModel>();
        for (FrameEventModel fe : frame.getFrameEvents()) {
            if (fe.getType().equals(FrameEventModel.Type.BREAK.name)) ret.add(fe);
        }
        return ret;
    }

    public PotPossibilities getPotPossiibilities(Player player) {
        FrameEventModel ev = this.getlastEvent();
        if (ev != null && ev.getType() == FrameEventModel.Type.BREAK) {
            if (ev.getPlayerScored() == player) {
                if (ev.isFreeBall() && ev.getBallsPottedCount() == 1) {
                    if (table.getReds() > 0) {
                        return PotPossibilities.COLOR;
                    } else {
                        return new PotPossibilities(this.table.getColorRemained());
                    }
                }

                if (ev.isOpen() && ev.isInColors() &&
                        this.table.getReds() == 0 &&
                        FrameEventHelper.getLastPot(ev).getType() == Ball.Type.COLOR) {
                    return new PotPossibilities(this.table.getColorRemained());
                } else if (FrameEventHelper.isOpen(ev) && FrameEventHelper.getLastPot(ev).getType() == Ball.Type.COLOR && !(ev.getScore() == 1 && ev.isFreeBall())) {
                    return PotPossibilities.RED;
                } else if (FrameEventHelper.isOpen(ev) && (FrameEventHelper.getLastPot(ev).getType() == Ball.Type.RED || (ev.getScore() == 1 && ev.isFreeBall()))) {
                    return PotPossibilities.COLOR;
                } else if (!FrameEventHelper.isOpen(ev) && this.table.getReds() == 0) {
                    return new PotPossibilities(this.table.getColorRemained());
                }


            }
        }
        if (this.table.getReds() == 0) {
            return new PotPossibilities(this.table.getColorRemained());
        }
        return PotPossibilities.RED;
    }

    public int getBehind() {
        return Math.abs(this.getResult(Player.ONE) - this.getResult(Player.TWO));
    }

    public int getRemaining() {
        int ret = this.table.getReds() * 8;
        Ball lastPotted = null;
        if (this.getlastEvent().getBallsPotted().size() > 0) {
            lastPotted = this.getlastEvent().getBallsPotted().get(this.getlastEvent().getBallsPotted().size() - 1);
        }
        if (this.getlastEvent() != null && this.getlastEvent().getType() == FrameEventModel.Type.BREAK && this.getlastEvent().isOpen() && lastPotted == Ball.RED)
            ret += 7;
        if (ret > 0) {
            ret += 27;
        } else {
            if (this.table != null && this.table.getColorRemained() != null)
                for (int i = 7; i >= this.table.getColorRemained().getValue(); i--) {
                    ret += i;
                }
        }
        return ret;
    }

    public int getCurrentBreak(Player player) {
        if (this.getlastEvent() == null || this.getlastEvent().getType() != FrameEventModel.Type.BREAK || !this.getlastEvent().isOpen() || this.getlastEvent().getPlayerScored() != player)
            return 0;
        return this.getlastEvent().getScore();
    }

    public boolean undo() {
        if (this.getFrame().isCompleted()) {
            this.getFrame().setCompleted(false);
        }
        if (this.getFrame().getFrameEvents() != null && this.getFrame().getFrameEvents().size() > 0) {
            if (this.getlastEvent() != null) {
                if (this.getlastEvent().getType() == FrameEventModel.Type.BREAK) {
                    if (this.getlastEvent().isOpen()) {
                        Ball ball = FrameEventHelper.removeLastPot(this.getlastEvent());
                        if (this.getlastEvent().getBallsPotted() == null || this.getlastEvent().getBallsPotted().isEmpty()) {
                            FrameEventModel toDelete = this.getlastEvent();
                            modelFactory.removeFrameEvent(this.getlastEvent());
                            this.getFrame().getFrameEvents().remove(this.getFrame().getFrameEvents().size() - 1);

                        }
                        if (ball == Ball.RED) {
                            this.table.setReds(this.table.getReds() + 1);
                        } else if (this.table.getReds() == 0 && this.table.getColorRemained().getValue() == ball.getValue() + 1) {
                            this.table.setColorRemained(ball);
                        } else if (this.table.getReds() == 0 && ball.getType() == Ball.Type.COLOR) {
                            this.getlastEvent().setInColors(false);
                        }
                    } else {
                        this.getlastEvent().setOpen(true);
                    }
                } else {
                    int potCount = this.getlastEvent().getBallsPotted().size();
                    if (this.getlastEvent().getType() == FrameEventModel.Type.FOUL && potCount != 0) {
                        this.table.setReds(this.table.getReds() + potCount);
                    }
                    modelFactory.removeFrameEvent(this.getlastEvent());
                    this.getFrame().getFrameEvents().remove(this.getFrame().getFrameEvents().size() - 1);
                }
            }
            this.recalculateScore();
            return true;
        }
        return false;
    }

    public FrameModel getFrame() {
        return this.frameModel;
    }

    public void setFrame(FrameModel frame) {
        this.frameModel = frame;
    }
}
