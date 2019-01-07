package bgu.spl.mics.application.passiveObjects;


import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Passive data-object representing the store inventory.
 * It holds a collection of {@link BookInventoryInfo} for all the
 * books in the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory implements Serializable {
    private ArrayList<BookInventoryInfo> inventory;
    private static class InventoryHolder {
        private static Inventory instance = new Inventory();
    }
    private Inventory() {
        inventory = new ArrayList<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static Inventory getInstance() {
        return InventoryHolder.instance;
    }

    /**
     * Initializes the store inventory. This method adds all the items given to the store
     * inventory.
     * <p>
     *
     * @param inventory Data structure containing all data necessary for initialization
     *                  of the inventory.
     */
    public void load(BookInventoryInfo[] inventory) {
        if(this.inventory.size() == 0)
        this.inventory.addAll(Arrays.asList(inventory));
        else{
            this.inventory.clear();
            this.inventory.addAll(Arrays.asList(inventory));
        }
    }

    /**
     * Attempts to take one book from the store.
     * <p>
     *
     * @param book Name of the book to take from the store
     * @return an {@link Enum} with options NOT_IN_STOCK and SUCCESSFULLY_TAKEN.
     * The first should not change the state of the inventory while the
     * second should reduce by one the number of books of the desired type.
     */
    public OrderResult take(String book) {

            for (BookInventoryInfo b : inventory) {

                synchronized (b) {
                if (b.getBookTitle().equals(book)) {

                    b.setAmount(b.getAmount() - 1);

                    OrderResult o = OrderResult.SUCCESSFULLY_TAKEN;
                    return o;
                }
            }
        }
            OrderResult o = OrderResult.NOT_IN_STOCK;
            return o;

    }


    /**
     * Checks if a certain book is available in the inventory.
     * <p>
     *
     * @param book Name of the book.
     * @return the price of the book if it is available, -1 otherwise.
     */
    public int checkAvailabiltyAndGetPrice(String book) {

            for (BookInventoryInfo b : inventory) {

                if (b.getBookTitle().equals(book)) {
                    synchronized (b) {
                    return b.getPrice();
                }
            }

        }
        return -1;
    }
    /**
     * <p>
     * Prints to a file name @filename a serialized object HashMap<String,Integer> which is a Map of all the books in the inventory. The keys of the Map (type {@link String})
     * should be the titles of the books while the values (type {@link Integer}) should be
     * their respective available amount in the inventory.
     * This method is called by the main method in order to generate the output.
     */
    public void printInventoryToFile(String filename) {
        HashMap<String,Integer> hm = new HashMap<>();
        for (BookInventoryInfo book:inventory) {
            hm.put(book.getBookTitle(),book.getAmount());
        }
        FileOutputStream customersFile = null;
        ObjectOutputStream output = null;
        try {
            customersFile = new FileOutputStream(filename);
            try {
                output = new ObjectOutputStream(customersFile);
                output.writeObject(hm);
                output.close();
                customersFile.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
