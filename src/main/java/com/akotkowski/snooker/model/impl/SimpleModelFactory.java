package com.akotkowski.snooker.model.impl;

import com.akotkowski.snooker.model.FrameEventModel;
import com.akotkowski.snooker.model.FrameModel;
import com.akotkowski.snooker.model.MatchModel;
import com.akotkowski.snooker.model.ModelFactory;

public class SimpleModelFactory implements ModelFactory {


    @Override
    public FrameModel createFrame() {
        return new SimpleFrameModel();
    }

    @Override
    public MatchModel createMatch() {
        return new SimpleMatchModel();
    }

    @Override
    public FrameEventModel createFrameEvent() {
        return new SimpleFrameEventModel();
    }

    @Override
    public void removeFrame(FrameModel frame) {
        // to be handled if necessary - ex. delete entity from DB
    }

    @Override
    public void removeMatch(MatchModel match) {
        // to be handled if necessary - ex. delete entity from DB
    }

    @Override
    public void removeFrameEvent(FrameEventModel frameEvent) {
        // to be handled if necessary - ex. delete entity from DB
    }
}
