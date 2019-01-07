package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.io.Serializable;

public class TakeBookEvent<T> implements Event<T> , Serializable {
    private String bookTitle;

    public TakeBookEvent(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookTitle() {
        return bookTitle;
    }
}
