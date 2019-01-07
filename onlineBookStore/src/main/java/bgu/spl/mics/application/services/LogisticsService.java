package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
//imports in order to compile
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.Inventory;

import java.util.concurrent.LinkedBlockingDeque;

import static bgu.spl.mics.application.BookStoreRunner.timeServiceCanStart;

/**
 * Logistic service in charge of delivering books that have been purchased to customers.
 * Handles {@link DeliveryEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LogisticsService extends MicroService {

    private int tick = 1;
    private int duration;

    public LogisticsService(String name) {
        super(name);
    }

    @Override
    protected void initialize() {

        //maybe its good but the warnings annoy me
        subscribeEvent(DeliveryEvent.class, deliverEvent -> {
            Future<Future<DeliveryVehicle>> futureVehicle = sendEvent(new getVehicleEvent(this.getName()));
            if (futureVehicle != null) {
                if(!(futureVehicle.isDone() && futureVehicle.get() == null) ) {  //for the-unregister-situation//
                  //if the messageBus will return Null//
                    DeliveryVehicle v = futureVehicle.get().get();

                    v.deliver(deliverEvent.getCustomerAddress(), deliverEvent.getCustomerDistance());
                    sendEvent(new ReturnVehicleEvent(v));
                }
            }
//           no call for complete() method because webAPI service is not waiting for the delivery to be complete//
        });
        subscribeBroadcast(TickBroadcast.class,tickBroadcast->{
        this.tick = tickBroadcast.getTick();
        this.duration = tickBroadcast.getDuration();
            if(tickBroadcast.getTick() == duration){
                this.terminate();
            }
        });

        timeServiceCanStart.countDown();

    }


}
