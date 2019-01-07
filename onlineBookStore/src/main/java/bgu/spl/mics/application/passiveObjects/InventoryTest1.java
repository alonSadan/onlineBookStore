package bgu.spl.mics.application.passiveObjects;

import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.*;

public class InventoryTest1 implements Serializable {
    private Inventory inventory;

    void setUp() {
        inventory = Inventory.getInstance();
    }



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

    /**
     * Retrieves the single instance of this class.
     */
    @Test
    static Inventory testgetInstance() {
        Inventory inventory1 = Inventory.getInstance();
        Inventory inventory2 = Inventory.getInstance();
        if(inventory1 != inventory2)
            fail();
        return inventory1;
    }





    /**
     * Initializes the store inventory. This method adds all the items given to the store
     * inventory.
     * <p>
     * @param inventory 	Data structure containing all data necessary for initialization
     * 						of the inventory.
     */
    @Test void testload(BookInventoryInfo[ ] inventory){
        assertTrue (inventory != null && inventory.length != 0);
    }



    /**
     * Attempts to take one book from the store.
     * <p>
     * @param book 		Name of the book to take from the store
     * @return 	an {@link Enum} with options NOT_IN_STOCK and SUCCESSFULLY_TAKEN.
     * 			The first should not change the state of the inventory while the
     * 			second should reduce by one the number of books of the desired type.
     */
    @Test
    OrderResult testtake (String book) {
        OrderResult o = inventory.take(book);

        assertTrue (o.name() == "NOT_IN_STOCK" || o.name() == "SUCCESSFULLY_TAKEN");
        if (o.name() == "NOT_IN_STOCK")
            assertTrue(inventory.checkAvailabiltyAndGetPrice(book) == -1);
        if (o.name() == "SUCCESSFULLY_TAKEN") {
            if (inventory.checkAvailabiltyAndGetPrice(book) == -1)
                assertTrue(inventory.take(book).name() == "NOT_IN_STOCK");
        }
        return o;
    }

    /**
     * Checks if a certain book is available in the inventory.
     * <p>
     * @param book 		Name of the book.
     * @return the price of the book if it is available, -1 otherwise.
     */
    @Test int testcheckAvailabiltyAndGetPrice(String book) {
        if (book == null) fail();
        int ans = inventory.checkAvailabiltyAndGetPrice(book);
        if(ans < -1) fail();
        if(ans >= 0){
            while(ans >= 0){ //make sure the book is gone
                OrderResult o = inventory.take(book);
                ans = inventory.checkAvailabiltyAndGetPrice(book);
            }
            if(ans != -1)
                fail();
        }
        return ans;
    }

    /**
     *
     * <p>
     * Prints to a file name @filename a serialized object HashMap<String,Integer> which is a Map of all the books in the inventory. The keys of the Map (type {@link String})
     * should be the titles of the books while the values (type {@link Integer}) should be
     * their respective available amount in the inventory.
     * This method is called by the main method in order to generate the output.
     */
    @Test void testprintInventoryToFile(String filename){
        //???
    }



    void tearDown() {
        inventory = null;
    }




}