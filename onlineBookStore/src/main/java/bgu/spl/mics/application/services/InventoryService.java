package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
//imports in order to compile
import bgu.spl.mics.application.messages.CheckBookAvailabilityEvent;
import bgu.spl.mics.application.messages.TakeBookEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.OrderResult;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.Inventory;

import static bgu.spl.mics.application.BookStoreRunner.timeServiceCanStart;

/**
 * InventoryService is in charge of the book inventory and stock.
 * Holds a reference to the {@link Inventory} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */

public class InventoryService extends MicroService{
	private int currentTick;
	private int duration;
	public InventoryService(String name) {
		super(name);
	}

	@Override
	protected void initialize() {
		System.out.println( getName() + " started");

		subscribeEvent(CheckBookAvailabilityEvent.class, checkevent -> {
			int price = Inventory.getInstance().checkAvailabiltyAndGetPrice(checkevent.getBookTitle());
			complete(checkevent,price);
		});

		subscribeEvent(TakeBookEvent.class, takeifavailableevent -> {
			OrderResult o = Inventory.getInstance().take(takeifavailableevent.getBookTitle());
			complete(takeifavailableevent,o);
		});

		subscribeBroadcast(TickBroadcast.class, tickBroadcast -> {
			this.currentTick = tickBroadcast.getTick();
			this.duration = tickBroadcast.getDuration();
			if(tickBroadcast.getTick() == duration){
				this.terminate();
			}
		});
		timeServiceCanStart.countDown();
	}

}
