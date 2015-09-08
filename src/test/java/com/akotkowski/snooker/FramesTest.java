package com.akotkowski.snooker;

import org.junit.Before;
import org.junit.Test;


public class FramesTest {

    Match match;

    @Before
    public void setup(){
        match = new MatchBuilder().frames(5).build();
    }

    @Test
    public void should_frame_be_retired() {

    }

    @Test
    public void should_match_end_after_correct_frame_amount() {

    }

    @Test
    public void should_end_frame_when_only_black(){

    }
    @Test
    public void shouldnt_end_frame_when_only_black(){

    }




}
