package bgu.spl.mics.application.passiveObjects;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.*;
import java.io.Serializable.*;
/**
 * Passive data-object representing a customer of the store.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add fields and methods to this class as you see fit (including public methods).
 */
public class Customer implements Serializable {
    private String name;
    private int id;
    private String address;
    private List<OrderReceipt> customerReceiptList;
    private CopyOnWriteArrayList<OrderTick> orderSchedule;
    private int distance;
    private int availableCreditAmount;
    private int creditCardNumber;

    public Customer(String name, int id, String address, int distance, int availableCreditAmount, int creditCardNumber) {
        this.name = name;
        this.id = id;
        this.address = address;
        this.orderSchedule = new CopyOnWriteArrayList<>();
        this.customerReceiptList = new LinkedList<>();
        this.distance = distance;
        this.availableCreditAmount = availableCreditAmount;
        this.creditCardNumber = creditCardNumber;
    }
    public synchronized void setAvailableCreditAmount(int price){
        availableCreditAmount = price;
    }

    /**
     * Retrieves the name of the customer.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the ID of the customer.
     */
    public int getId() {
        return id;
    }

    /**
     * Retrieves the address of the customer.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Retrieves the distance of the customer from the store.
     */
    public int getDistance() {
        return distance;
    }


    /**
     * Retrieves a list of receipts for the purchases this customer has made.
     * <p>
     *
     * @return A list of receipts.
     */
    public List<OrderReceipt> getCustomerReceiptList() {
        return customerReceiptList;
    }

    /**
     * Retrieves the amount of money left on this customers credit card.
     * <p>
     *
     * @return Amount of money left.
     */
    public synchronized int getAvailableCreditAmount() {
        return availableCreditAmount;
    }

    /**
     * Retrieves this customers credit card serial number.
     */
    public int getCreditNumber() {
        return creditCardNumber;
    }

    public List<OrderTick> getOrderSchedule() {
        return orderSchedule;
    }

}
