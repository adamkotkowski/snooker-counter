package com.akotkowski.snooker.util;

import com.akotkowski.snooker.model.MatchModel;
import com.akotkowski.snooker.model.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class MatchStatistics implements Serializable {
    private static final long serialVersionUID = 1L;
    private MatchModel match;
    PlayerMatchStatistics player1;
    PlayerMatchStatistics player2;


    MatchStatistics(MatchModel match) {
        this.match = match;
        player1 = new PlayerMatchStatistics();
        player2 = new PlayerMatchStatistics();
    }

    public MatchModel getMatch() {
        return match;
    }

    public void setMatch(MatchModel match) {
        this.match = match;
    }

    public PlayerMatchStatistics getPlayer(Player player) {
        if (player == Player.ONE) return player1;
        if (player == Player.TWO) return player2;
        return null;
    }

    public class PlayerMatchStatistics implements Serializable {
        private static final long serialVersionUID = 3361161862844895059L;
        public static final int HGHEST_BREAK_COUNT = 5;
        private ArrayList<Integer> highestBreaks = new ArrayList<Integer>();
        private int pots;
        private int points;
        private int fauls;
        private int faulPoints;
        private int breaksMore10;
        private int breaksMore20;
        private int breaksMore50;
        private int breaksMore70;
        private int breaksMore100;
        private int highestBreak;

        private float avaragePotTime;

        public ArrayList<Integer> getHighestBreaks() {
            return highestBreaks;
        }

        public void setHighestBreaks(ArrayList<Integer> highestBreaks) {
            this.highestBreaks = highestBreaks;
        }

        public int getPots() {
            return pots;
        }

        public void setPots(int pots) {
            this.pots = pots;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public int getFauls() {
            return fauls;
        }

        public void setFauls(int fauls) {
            this.fauls = fauls;
        }

        public float getAvaragePotTime() {
            return avaragePotTime;
        }

        public void setAvaragePotTime(float avaragePotTime) {
            this.avaragePotTime = avaragePotTime;
        }


        public int getFaulPoints() {
            return faulPoints;
        }

        public void setFaulPoints(int faulPoints) {
            this.faulPoints = faulPoints;
        }

        public void addPoints(int score) {
            this.points += score;
        }

        public void addFaul(int score) {
            this.faulPoints += score;
            this.fauls++;
        }

        public void addPot(int score) {
            this.pots++;
            this.points += score;
        }
//		public void addPoints(int score) {
//			this.points+=score;
//		}

        public int getBreaksMore10() {
            return breaksMore10;
        }

        public void setBreaksMore10(int breaksMore10) {
            this.breaksMore10 = breaksMore10;
        }

        public int getBreaksMore20() {
            return breaksMore20;
        }

        public void setBreaksMore20(int breaksMore20) {
            this.breaksMore20 = breaksMore20;
        }

        public int getBreaksMore50() {
            return breaksMore50;
        }

        public void setBreaksMore50(int breaksMore50) {
            this.breaksMore50 = breaksMore50;
        }

        public int getBreaksMore70() {
            return breaksMore70;
        }

        public void setBreaksMore70(int breaksMore70) {
            this.breaksMore70 = breaksMore70;
        }

        public int getBreaksMore100() {
            return breaksMore100;
        }

        public void setBreaksMore100(int breaksMore100) {
            this.breaksMore100 = breaksMore100;
        }

        public int getHighestBreak() {
            return highestBreak;
        }

        public void setHighestBreak(int highestBreak) {
            this.highestBreak = highestBreak;
        }


    }

}
