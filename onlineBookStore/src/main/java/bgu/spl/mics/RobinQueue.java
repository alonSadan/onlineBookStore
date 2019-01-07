package bgu.spl.mics;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.Iterator;

//maybe chang to Blocking queue if necessary//
public class RobinQueue<T> implements Iterable<T> {    //added by Yotam and Alon

    private ConcurrentLinkedDeque <T> q;
    public RobinQueue(){
        this.q = new ConcurrentLinkedDeque<T>();
    }
    synchronized public T circularDequeue() {    //moves the head to the tail of the queue//
        if (q.peek() != null) {
            q.add(q.poll());
            return q.getLast();
        }
        return null;
    }
     public T peek(){
        return q.peek();
    }
     public T poll(){
        return this.q.poll();
    }
     void add(T element){
         this.q.add(element);
    }
     T getLast(){
        return this.q.getLast();
    }
    public T getFirst(){
        return q.getFirst();
    }
     void remove(){
        this.q.remove();
    }
     boolean isEmpty(){
        return this.q.isEmpty();
    }

    public Iterator<T> iterator() {
        return this.q.iterator();
    }
    public Iterator<T> descendingIterator(){
        return this.q.descendingIterator();
    }
    public  void remove(T element){
        this.q.remove(element);
    }

    public  void forEach(Consumer<?super T> action){
        this.q.forEach(action);
    }
    public  Spliterator<T> spliterator(){
        return  this.q.spliterator();
    }
}
