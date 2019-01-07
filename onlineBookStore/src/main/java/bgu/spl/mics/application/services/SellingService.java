package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.messages.*;

import java.util.concurrent.ConcurrentLinkedDeque;

import static bgu.spl.mics.application.BookStoreRunner.timeServiceCanStart;

/**
 * Selling service in charge of taking orders from customers.
 * Holds a reference to the {@link MoneyRegister} singleton of the store.
 * Handles {@link BookOrderEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link Inventory}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class SellingService extends MicroService {
    private int currentTick = 0;

    public SellingService(String name) {
        super(name);

    }

    @Override
    protected void initialize() {

        subscribeEvent(BookOrderEvent.class, bookOrderEvent -> {
            Future<Integer> bookPriceFuture = sendEvent(new CheckBookAvailabilityEvent<>(this.getName(), bookOrderEvent.getNameOfBook()));


            if (bookPriceFuture != null) {    //maybe messageBuss returned null//
                if (!(bookPriceFuture.isDone() && bookPriceFuture.get() == null)) {   //if the unregister issue resolve null//
                    if (bookPriceFuture.get() != -1) {  // first check whether the Future is resolved, than if book is available(then it has a price)//

                    synchronized (bookOrderEvent.getCustomer()) {
                        if ((bookOrderEvent.getCustomer().getAvailableCreditAmount() >= bookPriceFuture.get())) {  // check if customer has enough money//
                            Future<OrderResult> orderResultFuture = sendEvent(new TakeBookEvent(bookOrderEvent.getNameOfBook()));  //try to take book//
                            if (orderResultFuture != null) {    //maybe messageBuss returned null than resolve null //


                                if (!(orderResultFuture.isDone() && orderResultFuture.get() == null)) {   //if for the unregister issue, resolve null//

                                    if (orderResultFuture.get() == orderResultFuture.get().SUCCESSFULLY_TAKEN) {  //book was taken else resolve null//
                                        if (MoneyRegister.getInstance().chargeCreditCard(bookOrderEvent.getCustomer(), bookPriceFuture.get())) { // if customer charging was successful//
                                            //return receipt//
                                            OrderReceipt receipt = new OrderReceipt(this.getName(), bookOrderEvent.getCustomer().getId(), bookOrderEvent.getNameOfBook(), currentTick, bookOrderEvent.getOrderTick());
                                            complete(bookOrderEvent, receipt);
                                            MoneyRegister.getInstance().file(receipt);
                                        } else { //charging the customer was unsuccessful//
                                            complete(bookOrderEvent, null);
                                        }
                                    } else {  //book was not taken from inventory//
                                        complete(bookOrderEvent, null);
                                    }


                                } else { // OrderResultFuture is done and it's result is null - unregister issue//
                                    complete(bookOrderEvent, null);   // if orderResultFuture result is null//
                                }


                            } else {   // messageBus returned null for the TakeBook event
                                System.out.println("messageBus returned null for the TakeBook event in " + this.getName());
                                complete(bookOrderEvent, null);
                            }   //if book is not in stock//

                        } else { //customer doesn't have enough money//
                            System.out.println("customer doesn't have enough money in " + this.getName());
                            complete(bookOrderEvent, null);
                        }
                    }
                    } else { //book is not in inventory
                        System.out.println(bookOrderEvent.getNameOfBook() + "is not in inventory in " + this.getName());
                        complete(bookOrderEvent, null);
                    }
                } else {// PriceFuture is done and it's result is null//
                    System.out.println("PriceFuture is done and it's result is null- unregister issue in " + this.getName());
                    complete(bookOrderEvent, null);
                }

            } else { //messageBus returned null for the checkBookAvailabilityEvent//
                System.out.println("messageBus returned null for the checkBookAvailabilityEvent in" + this.getName());
                complete(bookOrderEvent, null);

            }


        });
        subscribeBroadcast(TickBroadcast.class, tickBroadcast -> {

            this.currentTick = tickBroadcast.getTick();
            if (tickBroadcast.getTick() == tickBroadcast.getDuration()) {
                System.out.println(this.getName() + "is terminating");
                this.terminate();
                //  this.terminate();   ///put this in other events//
            }
        });

        timeServiceCanStart.countDown();
    }

}
