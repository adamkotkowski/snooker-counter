package com.akotkowski.snooker;

import com.akotkowski.snooker.model.*;
import com.akotkowski.snooker.model.impl.SimpleFrameEventModel;
import com.akotkowski.snooker.model.impl.SimpleFrameModel;
import com.akotkowski.snooker.model.impl.SimpleMatchModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class ModelFactoryTest {

    @Mock
    ModelFactory modelFactory;

    Match match;

    @Test
    public void should_create_match_with_model_factory(){
        //given
        MatchModel matchModel = new SimpleMatchModel();
        Mockito.when(modelFactory.createMatch()).thenReturn(matchModel);


        //when
        match = new MatchBuilder().frames(3).modelFactory(modelFactory).build();


        //then
        assertThat(match.getMatch()).isSameAs(matchModel);
    }

    @Test
    public void should_create_frame_with_model_factory(){
        //given
        MatchModel matchModel = new SimpleMatchModel();
        FrameModel frameModel = new SimpleFrameModel();
        Mockito.when(modelFactory.createMatch()).thenReturn(matchModel);
        Mockito.when(modelFactory.createFrame()).thenReturn(frameModel);
        match = new MatchBuilder().frames(3).modelFactory(modelFactory).build();

        //when
        match.startNewFrame();

        //then
        assertThat(match.getCurrentFrame().getFrame()).isSameAs(frameModel);
    }

    @Test
    public void should_create_frame_event_with_model_factory(){
        MatchModel matchModel = new SimpleMatchModel();
        FrameModel frameModel = new SimpleFrameModel();
        FrameEventModel frameEventModel = new SimpleFrameEventModel();

        Mockito.when(modelFactory.createMatch()).thenReturn(matchModel);
        Mockito.when(modelFactory.createFrame()).thenReturn(frameModel);
        Mockito.when(modelFactory.createFrameEvent()).thenReturn(frameEventModel);
        match = new MatchBuilder().frames(3).modelFactory(modelFactory).build();

        //when
        match.startNewFrame();
        match.registerPot(Ball.RED, Player.ONE);

        //then
        assertThat(match.getCurrentFrame().getlastEvent()).isSameAs(frameEventModel);
    }

    @Test
    public void should_remove_frame_event(){
        MatchModel matchModel = new SimpleMatchModel();
        FrameModel frameModel = new SimpleFrameModel();
        frameModel.setWinner(Player.ONE);
        FrameEventModel frameEventModel = new SimpleFrameEventModel();

        Mockito.when(modelFactory.createMatch()).thenReturn(matchModel);
        Mockito.when(modelFactory.createFrame()).thenReturn(frameModel);
        Mockito.when(modelFactory.createFrameEvent()).thenReturn(frameEventModel);
        match = new MatchBuilder().frames(3).modelFactory(modelFactory).build();

        match.startNewFrame();
        match.registerPot(Ball.RED, Player.ONE);

        //when
        match.undo();

        //then
        Mockito.verify(modelFactory, Mockito.times(1)).removeFrameEvent(frameEventModel);
    }

    @Test
    public void should_remove_frame(){
        MatchModel matchModel = new SimpleMatchModel();
        FrameModel frameModel = new SimpleFrameModel();
        FrameEventModel frameEventModel = new SimpleFrameEventModel();

        Mockito.when(modelFactory.createMatch()).thenReturn(matchModel);
        Mockito.when(modelFactory.createFrame()).thenReturn(new SimpleFrameModel());
        Mockito.when(modelFactory.createFrameEvent()).thenReturn(frameEventModel);
        match = new MatchBuilder().frames(3).modelFactory(modelFactory).build();

        match.startNewFrame();
        match.surrenderFrame(Player.ONE);

        Mockito.when(modelFactory.createFrame()).thenReturn(frameModel);
        match.startNewFrame();

        //when
        match.undo();

        //then
        Mockito.verify(modelFactory, Mockito.times(1)).removeFrame(frameModel);
    }

}
