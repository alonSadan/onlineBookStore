package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.io.Serializable;

public class getVehicleEvent<T> implements Event<T> , Serializable {

    private String senderName;

    public getVehicleEvent(String senderName){
        this.senderName = senderName;
    }


    public String getSenderName() {
        return senderName;
    }
}
