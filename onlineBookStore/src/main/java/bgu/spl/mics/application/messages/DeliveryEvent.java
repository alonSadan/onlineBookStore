package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.io.Serializable;

public class DeliveryEvent<T> implements Event<T> , Serializable {
    private String senderName;
    private int customerId;
    private String customerAddress;
    private int customerDistance;

    public DeliveryEvent(int id,String address, int customerDistance, String senderName){

        this.customerAddress = address;
        this.customerDistance = customerDistance;
        this.customerId = id;
        this.senderName = senderName;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public int getCustomerDistance() {
        return customerDistance;
    }

    public void setCustomerDistance(int customerDistance) {
        this.customerDistance = customerDistance;
    }

    public boolean isVehicleIsAvailable() {
        return vehicleIsAvailable;
    }

    public void setVehicleIsAvailable(boolean vehicleIsAvailable) {
        this.vehicleIsAvailable = vehicleIsAvailable;
    }

    private boolean delivered;
    private boolean vehicleIsAvailable;


    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }


    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }
}
