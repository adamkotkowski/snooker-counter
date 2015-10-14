package com.akotkowski.snooker.util;

import com.akotkowski.snooker.model.FrameEventModel;
import com.akotkowski.snooker.model.FrameModel;
import com.akotkowski.snooker.model.MatchModel;
import com.akotkowski.snooker.model.Player;

public class DomainMapper {

	public static void map(MatchModel to, MatchModel from) {
		to.setDate(from.getDate());
		to.setCompleted(from.isCompleted());
		to.setFrameCount(from.getFrameCount());
		to.setPlayer(Player.ONE, from.getPlayer(Player.ONE));
		to.setPlayer(Player.TWO, from.getPlayer(Player.TWO));
		to.setResult(Player.ONE, from.getResult(Player.ONE));
		to.setResult(Player.TWO, from.getResult(Player.TWO));
		to.setStarted(from.isStarted());
		to.setWinner(from.getWinner());
	}

	public static void map(FrameModel to, FrameModel from) {
		to.setCompleted(from.isCompleted());
		to.setResult(Player.ONE, from.getResult(Player.ONE));
		to.setResult(Player.TWO, from.getResult(Player.TWO));
		to.setWinner(from.getWinner());
	}


	public static void map(FrameEventModel to, FrameEventModel from) {
		to.setBallsPotted(from.getBallsPotted());
		to.setBallsPottedCount(from.getBallsPottedCount());
		to.setDate(from.getDate());
		to.setInColors(from.isInColors());
		to.setOpen(from.isOpen());
		to.setPlayerScored(from.getPlayerScored());
		to.setScore(from.getScore());
		to.setShotTime(from.getShotTime());
		to.setType(from.getType());
		
	}
}
