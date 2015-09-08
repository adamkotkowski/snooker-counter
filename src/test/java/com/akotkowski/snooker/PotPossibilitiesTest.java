package com.akotkowski.snooker;

import com.akotkowski.snooker.exceptions.IllegalBallPotted;
import com.akotkowski.snooker.model.Player;
import org.junit.Before;
import org.junit.Test;


public class PotPossibilitiesTest {

    Match match;

    @Before
    public void setup(){
        match = new MatchBuilder().frames(1).build();
    }

    @Test(expected = IllegalBallPotted.class)
    public void should_throw_exception_when_potted_red_instead_of_color(){
        //given
        Player playerScoring = Player.ONE;

        //when
        match.startNewFrame();

        match.registerPot(Ball.RED, playerScoring);
        match.registerPot(Ball.BLUE, playerScoring);
        match.registerPot(Ball.RED, playerScoring);
        match.registerPot(Ball.RED, playerScoring);

        //then


    }

    @Test(expected = IllegalBallPotted.class)
    public void should_throw_exception_when_potted_color_after_color(){
        //given
        Player playerScoring = Player.ONE;

        //when
        match.startNewFrame();

        match.registerPot(Ball.RED, playerScoring);
        match.registerPot(Ball.BLUE, playerScoring);
        match.registerPot(Ball.YELLOW, playerScoring);

        //then


    }

}
