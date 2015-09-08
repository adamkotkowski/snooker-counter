package com.akotkowski.snooker;

import org.junit.Before;


public class TableTest {

    Match match;

    @Before
    public void setup() {
        match = new MatchBuilder().frames(1).build();
    }


}
