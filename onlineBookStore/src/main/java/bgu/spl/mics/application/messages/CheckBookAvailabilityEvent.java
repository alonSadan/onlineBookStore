package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.*;

import java.io.Serializable;

public class CheckBookAvailabilityEvent<T> implements Event<T>, Serializable {

    private String senderName;
    private OrderResult orderResult;
    private String bookTitle;


    public CheckBookAvailabilityEvent(String senderName ,String bookTitle){
        this.senderName = senderName;
        this.bookTitle = bookTitle;
    }

    public OrderResult getOrderResult() {
        return orderResult;
    }

    public void setOrderResult(OrderResult orderResult) {
        this.orderResult = orderResult;

    }


    public String getBookTitle() {
        return bookTitle;
    }
}
