package com.akotkowski.snooker.model;

import com.akotkowski.snooker.Ball;

import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;
import java.util.List;

@XmlTransient
public interface FrameEventModel {

    long getShotTime();

    void setShotTime(long shotTime);

    public enum Type {
        BREAK("break"),
        FOUL("foul"),
        FRAME_SURRENDER("frame_surrender"),
        MATCH_SURRENDER("match_surrender");

        public String name;

        Type(String name) {
            this.name = name;
        }

    }

    Type getType();

    void setType(Type type);

    List<Ball> getBallsPotted();

    void setBallsPotted(List<Ball> ballsPotted);


    Player getPlayerScored();

    void setPlayerScored(Player playerScored);

    int getScore();

    void setScore(int score);

    int getBallsPottedCount();

    void setBallsPottedCount(int ballsPottedCount);

    boolean isOpen();

    void setOpen(boolean _open);

    int getFreeBallScore();

    void setFreeBallScore(int freeBall);

    Date getDate();

    void setDate(Date time);

    boolean isInColors();

    void setInColors(boolean inColors);

    void setFrame(FrameModel frame);

}
