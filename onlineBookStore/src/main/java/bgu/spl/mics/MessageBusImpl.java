package bgu.spl.mics;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public  class  MessageBusImpl implements MessageBus {
    private ConcurrentHashMap<MicroService, LinkedBlockingDeque<Message>> queuesByMicroService;//<Microservice,Queue of Message>

    private ConcurrentHashMap<Class<? extends Message>, RobinQueue<MicroService>> robinByTypeOfMessage; //<Message,RobinQueue>
    //microserviceByMessage stores for each message all the microservices that are currently working on that message
    private ConcurrentHashMap<Message, MicroService> microserviceBymessage; //<message,microservice>

    private ConcurrentHashMap<Event, Future> resultsByEvents; // each event and his future\resolved future//

    //maybe add also broadCasts to this list//
    private static class MessageBusImplHolder {
        private static MessageBusImpl instance = new MessageBusImpl();
    }

    private MessageBusImpl() {

        this.queuesByMicroService = new ConcurrentHashMap<>();
        this.robinByTypeOfMessage = new ConcurrentHashMap<>();
        this.microserviceBymessage = new ConcurrentHashMap<>();
        this.resultsByEvents = new ConcurrentHashMap<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
     public static MessageBusImpl getInstance() {
        return MessageBusImplHolder.instance;
    }

    @Override
      public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {  //adds the event to the Robin of this type of message//

        if (robinByTypeOfMessage.containsKey(type)) {
            robinByTypeOfMessage.get(type).add(m);  //subscribes m to type//
        } else {    //create and subscribe//
            robinByTypeOfMessage.putIfAbsent(type, new RobinQueue<>());
            robinByTypeOfMessage.get(type).add(m);
        }
    }

    @Override
     public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {

        if (robinByTypeOfMessage.containsKey(type)) {
            robinByTypeOfMessage.get(type).add(m);
        } else {    //create and subscribe//

            robinByTypeOfMessage.putIfAbsent(type, new RobinQueue<>());
            robinByTypeOfMessage.get(type).add(m);
        }
    }

    @Override
     public <T> void complete(Event<T> e, T result) {
         if (e != null && resultsByEvents.get(e) != null) {
             resultsByEvents.get(e).resolve(result);   //finds the specific Future object connected to the event-e- and resolve it //
             queuesByMicroService.get(microserviceBymessage.get(e)).remove(e);
         }//removes the event from the microservise's Tasks queue//
    }

    @Override
     public void sendBroadcast(Broadcast b) {
        if (b != null) {
            for (MicroService m : robinByTypeOfMessage.get(b.getClass())) {
                queuesByMicroService.get(m).addLast(b);
            }
        }
    }


    @Override
    synchronized public <T> Future<T> sendEvent(Event<T> e) {
        if (robinByTypeOfMessage.containsKey(e.getClass()) && !robinByTypeOfMessage.get(e.getClass()).isEmpty()) {  //checks if someone subscribed for e//

            // the next two lines might have concurrency problems- the last micro service in the first line might not be the last one in the second line//
            synchronized ((robinByTypeOfMessage.get(e.getClass()).getFirst())) { //dont do circularDeque while adding to microserviceByMessage
                queuesByMicroService.get(robinByTypeOfMessage.get(e.getClass()).circularDequeue()).addLast(e); //add e to the microService's queue
                microserviceBymessage.put(e, robinByTypeOfMessage.get(e.getClass()).getLast()); //
            }

            resultsByEvents.put(e, new Future<T>());


            return resultsByEvents.get(e);
        }

        return null;
    }

    @Override
     public void register(MicroService m) {
        queuesByMicroService.putIfAbsent(m, new LinkedBlockingDeque<>());
    }

    @Override
     public void unregister(MicroService m) {

        for (Class<? extends Message> type : robinByTypeOfMessage.keySet()) {
            robinByTypeOfMessage.get(type).remove(m);   //returns True//// suppose to remove only if present//
        }
        for (Message message : microserviceBymessage.keySet()) {
            microserviceBymessage.remove(message, m);  // removes m only if its present//
        }
        for(Message message :queuesByMicroService.get(m)){
            if (message instanceof Event){
                this.resultsByEvents.get(message).resolve(null);
            }

        }
        queuesByMicroService.remove(m, queuesByMicroService.get(m)); //remove only if present//


    }

    @Override
     public Message awaitMessage(MicroService m) throws InterruptedException {
        if (!queuesByMicroService.containsKey(m)) {  //if m is not registered//
            throw new IllegalStateException();
        }
        return queuesByMicroService.get(m).take();                 //"take" waits for the element to become available//
    }


}
