package com.akotkowski.snooker;

import com.akotkowski.snooker.model.Player;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class BreaksTest {

    Match match;

    @Before
    public void setup(){
        match = new MatchBuilder().frames(1).build();
    }


    @Test
    public void should_go_through_147_break(){
        //given
        int ballsCount = 15;
        Player playerScoring = Player.ONE;

        //when
        match.startNewFrame();
        for(int i=0; i<ballsCount; i++){
            match.getCurrentFrame().registerPot(Ball.RED, playerScoring);
            match.getCurrentFrame().registerPot(Ball.BLACK, playerScoring);
        }
        match.registerPot(Ball.YELLOW, playerScoring);
        match.registerPot(Ball.GREEN, playerScoring);
        match.registerPot(Ball.BROWN, playerScoring);
        match.registerPot(Ball.BLUE, playerScoring);
        match.registerPot(Ball.PINK, playerScoring);
        match.registerPot(Ball.BLACK, playerScoring);

        //then
        assertThat(match.getCurrentFrame().getResult(playerScoring)).isEqualTo(147);
        assertThat(match.getCurrentFrame().getResult(playerScoring.getOponent())).isEqualTo(0);
    }

    @Test
    public void should_foul_interrupt_the_break(){
        //given
        Player playerScoring = Player.ONE;

        //when
        match.startNewFrame();

        match.registerPot(Ball.RED, playerScoring);
        match.registerPot(Ball.BLUE, playerScoring);
        match.registerPot(Ball.RED, playerScoring);
        match.registerFaul(4, playerScoring, 0);
        match.registerPot(Ball.RED, playerScoring);

        //then
        assertThat(match.getCurrentFrame().getResult(playerScoring)).isEqualTo(8);
        assertThat(match.getCurrentFrame().getResult(playerScoring.getOponent())).isEqualTo(4);
        assertThat(match.getCurrentFrame().getCurrentBreak(playerScoring)).isEqualTo(1);

    }

    @Test
    public void should_opponent_pot_interrupt_the_break(){
        //given
        Player playerScoring = Player.ONE;

        //when
        match.startNewFrame();

        match.registerPot(Ball.RED, playerScoring);
        match.registerPot(Ball.BLUE, playerScoring);
        match.registerPot(Ball.RED, playerScoring);
        match.registerPot(Ball.RED, playerScoring.getOponent());
        match.registerPot(Ball.RED, playerScoring);

        //then
        assertThat(match.getCurrentFrame().getResult(playerScoring)).isEqualTo(8);
        assertThat(match.getCurrentFrame().getResult(playerScoring.getOponent())).isEqualTo(1);
        assertThat(match.getCurrentFrame().getCurrentBreak(playerScoring)).isEqualTo(1);

    }

    @Test
    public void should_miss_interrupt_the_break(){
        //given
        Player playerScoring = Player.ONE;

        //when
        match.startNewFrame();

        match.registerPot(Ball.RED, playerScoring);
        match.registerPot(Ball.BLUE, playerScoring);
        match.registerPot(Ball.RED, playerScoring);
        match.getCurrentFrame().registerMiss();

        match.registerPot(Ball.RED, playerScoring);

        //then
        assertThat(match.getCurrentFrame().getResult(playerScoring)).isEqualTo(8);
        assertThat(match.getCurrentFrame().getResult(playerScoring.getOponent())).isEqualTo(0);
        assertThat(match.getCurrentFrame().getCurrentBreak(playerScoring)).isEqualTo(1);

    }

}
