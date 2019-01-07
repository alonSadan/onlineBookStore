package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;

import java.io.Serializable;

public class ReturnVehicleEvent<T> implements Event<T> , Serializable {
    private DeliveryVehicle v;

    public ReturnVehicleEvent(DeliveryVehicle v) {
        this.v = v;
    }

    public void setV(DeliveryVehicle v) {
        this.v = v;
    }

    public DeliveryVehicle getV() {
        return v;
    }
}
