package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.Customer;
import bgu.spl.mics.application.passiveObjects.OrderReceipt;
import bgu.spl.mics.application.passiveObjects.OrderResult;

import java.io.Serializable;

public class BookOrderEvent<T> implements Event<T> , Serializable {
    private String nameOfBook;
    private int orderId = 1;
    private OrderReceipt receipt = null;
    private String senderName;
    private int orderTick;
    private Customer customer;

public BookOrderEvent (String nameOfBook, Customer customer, String senderName, int orderTick){
    this.nameOfBook= nameOfBook;

    this.senderName = senderName;
    this.orderTick = orderTick;
    this.customer = customer;
}
    public void setReceipt(OrderReceipt receipt) {
        this.receipt = receipt;
    }

    public String getNameOfBook() {
        return nameOfBook;
    }

    public int getOrderId() {
        return orderId;
    }



    public OrderReceipt getReceipt() {
        return receipt;
    }

    public int getOrderTick() {
        return orderTick;
    }
    public Customer getCustomer(){
    return this.customer;
    }
}