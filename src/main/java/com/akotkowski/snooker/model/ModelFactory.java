package com.akotkowski.snooker.model;

public interface ModelFactory {
    FrameModel createFrame();

    MatchModel createMatch();

    FrameEventModel createFrameEvent();

    void removeFrame(FrameModel frame);

    void removeFrameEvent(FrameEventModel frameEvent);
}
