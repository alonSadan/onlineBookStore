package bgu.spl.mics.application.passiveObjects;


import java.io.*;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Passive object representing the store finance management. 
 * It should hold a list of receipts issued by the store.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class MoneyRegister implements Serializable {
	private ConcurrentLinkedDeque<OrderReceipt> recipts;
	private int fortune;

	private static class MoneyRegisterHolder implements Serializable {
		private static MoneyRegister instance = new MoneyRegister();
	}
	private MoneyRegister() {
		recipts = new ConcurrentLinkedDeque<>();
		fortune = 0;
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MoneyRegister getInstance() {
		return MoneyRegisterHolder.instance;
	}
	
	/**
     * Saves an order receipt in the money register.
     * <p>   
     * @param r		The receipt to save in the money register.
     */
	public void file (OrderReceipt r) {
		recipts.add(r);
		fortune = fortune + r.getPrice();
	}
	
	/**
     * Retrieves the current total earnings of the store.  
     */
	public int getTotalEarnings() {
		return fortune;
	}
	
	/**
     * Charges the credit card of the customer a certain amount of money.
     * <p>
     * @param amount 	amount to charge
     */
	 public  boolean chargeCreditCard(Customer customer, int amount) {
		synchronized (customer) {
			if (customer.getAvailableCreditAmount() >= amount) {
				customer.setAvailableCreditAmount(customer.getAvailableCreditAmount() - amount);
				this.fortune= this.fortune + amount;
				return true;

			} else {
				return false;
			}
		}
	}
	
	/**
     * Prints to a file named @filename a serialized object List<OrderReceipt> which holds all the order receipts 
     * currently in the MoneyRegister
     * This method is called by the main method in order to generate the output.
     */
	public void printOrderReceipts(String filename) {
		LinkedList<OrderReceipt> reciptsToPrint= new LinkedList<>();
		for (OrderReceipt r:recipts) {
			reciptsToPrint.add(r);
		}
		FileOutputStream reciptsFile;
		ObjectOutputStream output;
		try {
			reciptsFile = new FileOutputStream(filename);
			try {
				output = new ObjectOutputStream(reciptsFile);
				output.writeObject(reciptsToPrint);
				output.close();
				reciptsFile.close();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
