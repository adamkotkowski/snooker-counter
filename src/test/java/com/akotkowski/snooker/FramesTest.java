package com.akotkowski.snooker;

import com.akotkowski.snooker.model.Player;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class FramesTest {

    Match match;

    @Before
    public void setup(){
        match = new MatchBuilder().frames(5).build();
    }

    @Test
    public void should_frame_be_retired() {
        //given
        Player player = Player.ONE;

        //when
        match.startNewFrame();

        match.surrenderFrame(player);

        //then
        assertThat(match.getResult(player.getOponent())).isEqualTo(1);
        assertThat(match.getResult(player)).isEqualTo(0);
    }

    @Test
    public void should_match_end_after_correct_frame_amount() {
        //given
        Player player = Player.ONE;

        //when
        match.startNewFrame();

        match.surrenderFrame(player);
        match.startNewFrame();
        match.surrenderFrame(player);
        match.startNewFrame();
        match.surrenderFrame(player);


        //then
        assertThat(match.getMatch().isCompleted()).isTrue();
        assertThat(match.getResult(player.getOponent())).isEqualTo(3);
        assertThat(match.getResult(player)).isEqualTo(0);

    }

    @Test
    public void should_end_frame_when_only_black(){
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
        match.registerMiss();

        //then
        assertThat(match.getCurrentFrame().getFrame().isCompleted()).isTrue();
    }
    @Test
    public void shouldnt_end_frame_when_only_black(){
        //given
        int ballsCount = 15;

        //when
        match.startNewFrame();
        for(int i=0; i<9; i++){
            match.getCurrentFrame().registerPot(Ball.RED, Player.ONE);
            match.getCurrentFrame().registerPot(Ball.BLACK, Player.ONE);
        }
        for(int i=0; i<ballsCount-9; i++){
            match.getCurrentFrame().registerPot(Ball.RED, Player.TWO);
            match.getCurrentFrame().registerPot(Ball.BLACK, Player.TWO);
        }
        match.registerPot(Ball.YELLOW, Player.TWO);
        match.registerPot(Ball.GREEN, Player.TWO);
        match.registerPot(Ball.BROWN, Player.TWO);
        match.registerPot(Ball.BLUE, Player.TWO);
        match.registerPot(Ball.PINK, Player.TWO);
        match.registerMiss();
        // score is 72 : 68

        //then
        assertThat(match.getCurrentFrame().getFrame().isCompleted()).isFalse();
    }

    @Test
    public void shouldnt_end_frame_when_only_black_and_7_points_difference(){
        //given
        int ballsCount = 15;

        //when
        match.startNewFrame();
        for(int i=0; i<9; i++){
            match.getCurrentFrame().registerPot(Ball.RED, Player.ONE);
            match.getCurrentFrame().registerPot(Ball.BLACK, Player.ONE);
        }
        for(int i=0; i<ballsCount-9; i++){
            match.getCurrentFrame().registerPot(Ball.RED, Player.TWO);
            match.getCurrentFrame().registerPot(Ball.BLACK, Player.TWO);
        }
        match.registerPot(Ball.YELLOW, Player.TWO);

        match.registerFaul(6, Player.ONE, 0);
        match.registerFaul(5, Player.ONE, 0);

        match.registerPot(Ball.GREEN, Player.TWO);
        match.registerPot(Ball.BROWN, Player.TWO);
        match.registerPot(Ball.BLUE, Player.TWO);
        match.registerPot(Ball.PINK, Player.TWO);
        match.registerMiss();
        // score is 72 : 74

        //then
        assertThat(match.getCurrentFrame().getFrame().isCompleted()).isFalse();
    }

    @Test
    public void shouldnt_start_with_given_reds_on_table(){
        //given
        int ballsCount = 10;

        //when
        match.setReds(ballsCount);
        match.startNewFrame();
        match.getCurrentFrame().registerPot(Ball.RED, Player.ONE);

        //then
        assertThat(match.getCurrentFrame().getTable().getReds()).isEqualTo(ballsCount - 1);
    }

}
