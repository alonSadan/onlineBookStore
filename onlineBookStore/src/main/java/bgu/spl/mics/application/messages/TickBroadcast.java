package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

import java.io.Serializable;

public class TickBroadcast implements Broadcast, Serializable {
    private int tick;
    private int duration;

    public TickBroadcast(int tick ,int duration){
        this.tick = tick ;
        this.duration = duration;
    }

    public int getTick() {
        return tick;
    }

    public int getDuration() {
        return duration;
    }
}
