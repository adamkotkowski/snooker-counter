package com.akotkowski.snooker.util;

import com.akotkowski.snooker.model.FrameEventModel;
import com.akotkowski.snooker.model.FrameModel;
import com.akotkowski.snooker.model.MatchModel;

public class MatchStatisticsProcessor {
    public static MatchStatistics calculateStatistics(MatchModel match) {
        return calculateStatistics(match, new MatchStatistics());
    }

    public static MatchStatistics calculateStatistics(MatchModel match, MatchStatistics stats) {
        for (FrameModel frame : match.getFrames()) {
            calculateStatistics(frame, stats);
        }
        return stats;
    }

    public static MatchStatistics calculateStatistics(FrameModel frame, MatchStatistics stats) {

        for (FrameEventModel event : frame.getFrameEvents()) {
            MatchStatistics.PlayerMatchStatistics pl = stats.getPlayer(event.getPlayerScored());
            if (event.getType() == FrameEventModel.Type.BREAK) {
                pl.addPoints(event.getScore());
                pl.setPots(pl.getPots() + event.getBallsPotted().size());
                pl.setAvaragePotTime((pl.getAvaragePotTime() * pl.getPots() + event.getShotTime()) / (pl.getPots() + event.getBallsPottedCount()));
                if (event.getScore() >= 10) pl.setBreaksMore10(pl.getBreaksMore10() + 1);
                if (event.getScore() >= 20) pl.setBreaksMore20(pl.getBreaksMore20() + 1);
                if (event.getScore() >= 50) pl.setBreaksMore50(pl.getBreaksMore50() + 1);
                if (event.getScore() >= 70) pl.setBreaksMore70(pl.getBreaksMore70() + 1);
                if (event.getScore() >= 100) pl.setBreaksMore100(pl.getBreaksMore100() + 1);
                if (event.getScore() >= pl.getHighestBreak()) pl.setHighestBreak(event.getScore());
                if (pl.getHighestBreaks().size() < MatchStatistics.PlayerMatchStatistics.HGHEST_BREAK_COUNT) {
                    pl.getHighestBreaks().add(event.getScore());
                } else if (pl.getHighestBreaks().get(MatchStatistics.PlayerMatchStatistics.HGHEST_BREAK_COUNT - 1) < event.getScore()) {
                    int i = MatchStatistics.PlayerMatchStatistics.HGHEST_BREAK_COUNT - 1;
                    do {
                        i--;
                        if (i < 0) break;
                    } while (i > pl.getHighestBreaks().size() - 1 || pl.getHighestBreaks().get(i) < event.getScore());
                    if (i > 0)
                        pl.getHighestBreaks().add(i, event.getScore());
                }
            }
            if (event.getType() == FrameEventModel.Type.FOUL) {
                pl.addPoints(event.getScore());
                MatchStatistics.PlayerMatchStatistics plOposit = stats.getPlayer(event.getPlayerScored().getOponent());
                plOposit.setFauls(plOposit.getFauls() + 1);
                pl.setFaulPoints(pl.getFaulPoints() + event.getScore());
            }
        }

        return stats;
    }
}
