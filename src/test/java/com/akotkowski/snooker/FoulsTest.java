package com.akotkowski.snooker;

import com.akotkowski.snooker.exceptions.FreeBallException;
import com.akotkowski.snooker.model.Player;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class FoulsTest {

    Match match;

    @Before
    public void setup(){
        match = new MatchBuilder().frames(1).build();
    }

    @Test
    public void should_handle_foul(){
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
    public void should_handle_foul_with_red_potted(){
        //given
        Player playerScoring = Player.ONE;

        //when
        match.startNewFrame();

        match.registerPot(Ball.RED, playerScoring);
        match.registerPot(Ball.BLUE, playerScoring);
        match.registerPot(Ball.RED, playerScoring);
        match.registerFaul(4, playerScoring, 2);
        match.registerPot(Ball.RED, playerScoring);

        //then
        assertThat(match.getCurrentFrame().getTable().getReds()).isEqualTo(10);
    }

    @Test
    public void should_handle_free_ball_when_reds_on_the_table(){
        //given
        Player playerScoring = Player.ONE;

        //when
        match.startNewFrame();

        match.registerPot(Ball.RED, playerScoring);
        match.registerPot(Ball.BLUE, playerScoring);
        match.registerPot(Ball.RED, playerScoring);
        match.registerFaul(6, playerScoring.getOponent(), 0);
        match.registerFreeBall(Ball.RED, playerScoring);
        match.registerPot(Ball.BLACK, playerScoring);
        match.registerPot(Ball.RED, playerScoring);
        match.registerPot(Ball.BLACK, playerScoring);

        //then
        assertThat(match.getCurrentFrame().getResult(playerScoring)).isEqualTo(7 + 6 + 16);
        assertThat(match.getCurrentFrame().getResult(playerScoring.getOponent())).isEqualTo(0);
        assertThat(match.getCurrentFrame().getCurrentBreak(playerScoring)).isEqualTo(16);

    }

    @Test
    public void should_handle_free_ball_when_on_colors(){
        //given
        Player playerScoring = Player.ONE;
        int numberOfReds = 15;

        //when
        match.startNewFrame();
        for(int i=0; i< numberOfReds; i++){
            match.registerPot(Ball.RED, playerScoring);
            match.registerPot(Ball.BROWN, playerScoring);
        }
        match.registerFaul(6, playerScoring.getOponent(), 0);
        match.registerFreeBall(Ball.YELLOW, playerScoring);
        match.registerPot(Ball.YELLOW, playerScoring);
        match.registerPot(Ball.GREEN, playerScoring);

        //then
        assertThat(match.getCurrentFrame().getResult(playerScoring)).isEqualTo(75+6+7);
        assertThat(match.getCurrentFrame().getResult(playerScoring.getOponent())).isEqualTo(0);
        assertThat(match.getCurrentFrame().getCurrentBreak(playerScoring)).isEqualTo(7);

    }

    @Test(expected = FreeBallException.class)
    public void should_throw_exception_when_free_ball_taken_by_wrong_player(){
        //given
        Player playerScoring = Player.ONE;
        int numberOfReds = 15;

        //when
        match.startNewFrame();
        for(int i=0; i< numberOfReds; i++){
            match.registerPot(Ball.RED, playerScoring);
            match.registerPot(Ball.BROWN, playerScoring);
        }
        match.registerFaul(6, playerScoring, 0);
        match.registerFreeBall(Ball.YELLOW, playerScoring);

    }

}
