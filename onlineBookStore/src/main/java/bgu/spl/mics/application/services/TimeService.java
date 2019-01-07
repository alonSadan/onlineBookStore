package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.messages.TickBroadcast;

import static bgu.spl.mics.application.BookStoreRunner.timeServiceCanStart;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast Broadcast}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService {
    private int tick = 1;
    private Object lock;
    private int millisPerTick;
    private int duration;
    Timer timer;

    public TimeService(int millisPerTick, int duration) {
        super("timeService");
        this.millisPerTick = millisPerTick;
        this.duration = duration;
        this.timer = new Timer();
    }

    @Override
    protected void initialize() {
        try {
            timeServiceCanStart.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
               TickBroadcast tickBroadcast = new TickBroadcast(tick, duration);
                if (tick >= duration){
                    timer.cancel();
                    timer.purge();

                }
                    if (tickBroadcast != null ) {
                        sendBroadcast(tickBroadcast);

                        System.out.println("current tick is "+tick);
                    }
                tick++;

            }
        };
        timer.scheduleAtFixedRate(task, 0, millisPerTick);

        this.terminate();



    }

}
