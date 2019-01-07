package bgu.spl.mics.application.services;

import bgu.spl.mics.Future;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.ReturnVehicleEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.messages.getVehicleEvent;
import bgu.spl.mics.application.passiveObjects.DeliveryVehicle;
//imports in order to compile
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.Inventory;
import java.util.concurrent.ConcurrentLinkedDeque;

import static bgu.spl.mics.application.BookStoreRunner.timeServiceCanStart;


/**
 * ResourceService is in charge of the store resources - the delivery vehicles.
 * Holds a reference to the {@link ResourcesHolder} singleton of the store.
 * This class may not hold references for objects which it is not responsible for:
 * {@link MoneyRegister}, {@link Inventory}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class ResourceService extends MicroService {

    public ResourceService(String name) {
        super(name);
    }

    @Override
    protected void initialize() {

        System.out.println(  getName() + " started");
        subscribeEvent(getVehicleEvent.class, getVehicleEvent -> {
           complete(getVehicleEvent,ResourcesHolder.getInstance().acquireVehicle());

        });
        subscribeEvent(ReturnVehicleEvent.class, returnVehicleEvent -> {
            ResourcesHolder.getInstance().releaseVehicle(returnVehicleEvent.getV());
        });
        subscribeBroadcast(TickBroadcast.class, tickBroadcast->{
            if(tickBroadcast.getDuration() == tickBroadcast.getTick()){
                this.terminate();
            }

        });
        timeServiceCanStart.countDown();

    }
}
