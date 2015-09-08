package com.akotkowski.snooker;

import org.junit.Before;


public class AheadRemainingTest {

    Match match;

    @Before
    public void setup(){
        match = new MatchBuilder().frames(1).build();
    }


}
