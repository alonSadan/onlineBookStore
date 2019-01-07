package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;

/**
 * Passive data-object representing a receipt that should
 * be sent to a customer after the completion of a BookOrderEvent.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class OrderReceipt implements Serializable {
    private int orderId = 1;
    private String seller; //the service that sold the book
    private int customerId;
    private String bookTitle;
    private int price;
    private int issuedAndProcessTick;
    private int orderTick;
    //constructor added by yotam, for now all responsibility is on the take method that gives the constructor everything

    public OrderReceipt( String seller, int customerId, String bookTitle, int issuedAndprocessTick, int orderTick) {
        this.orderId = orderId;
        this.seller = seller;
        this.customerId = customerId;
        this.bookTitle = bookTitle;
        this.issuedAndProcessTick = issuedAndprocessTick;
        this.orderTick = orderTick;
    }


    /**
     * Retrieves the orderId of this receipt.
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Retrieves the name of the selling service which handled the order.
     */
    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    /**
     * Retrieves the ID of the customer to which this receipt is issued to.
     * <p>
     *
     * @return the ID of the customer
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Retrieves the name of the book which was bought.
     */
    public String getBookTitle() {
        return bookTitle;
    }

    /**
     * Retrieves the price the customer paid for the book.
     */
    public int getPrice() {
        return price;
    }

    /**
     * Retrieves the tick in which this receipt was issued.
     */

    /**
     * Retrieves the tick in which the customer sent the purchase request.
     */
    public int getOrderTick() {
        return orderTick;
    }

    /**
     * Retrieves the tick in which the treating selling service started
     * processing the order.
     */

    //stters added by yotam, but maybe we dont need them
    public void setOrderid(int orderid) {
        this.orderId = orderid;
    }

    public void setCustomerid(int customerid) {
        this.customerId = customerid;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setOrderTick(int orderTick) {
        this.orderTick = orderTick;
    }

    public int getIssuedAndProcessTick() {
        return issuedAndProcessTick;
    }
}
