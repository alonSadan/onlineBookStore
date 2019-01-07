package bgu.spl.mics;

import java.util.concurrent.TimeUnit;

/**
 * A Future object represents a promised result - an object that will
 * eventually be resolved to hold a result of some operation. The class allows
 * Retrieving the result once it is available.
 * <p>
 * Only private methods may be added to this class.
 * No public constructor is allowed except for the empty constructor.
 */
public class Future<T> {
    private T result;
    private boolean Done = false;
    private Object lock= new Object();
    /**
     * This should be the the only public constructor in this class.
     */
    public Future() {
    }

    /**
     * retrieves the result the Future object holds if it has been resolved.
     * This is a blocking method! It waits for the computation in case it has
     * not been completed.
     * <p>
     *
     * @return return the result of type T if(and only if) it is available, if not wait until it is available.
     * @pre none
     * @post none
     */
    public T get() {
        synchronized (lock){
            while (!Done) {
                try {
                    lock.wait();  //maybe wait without time//
                } catch (InterruptedException e) {
                    //    System.out.println("interrupted while waiting to be resolved");
                } //not sure it's the right message to print//
            }
            return this.result;
        }

    }

    /**
     * Resolves the result of this Future object.
     *
     * @pre future.get() == Blocked
     * @post future.get()==result;
     */
     public void resolve(T result) {
        synchronized (lock) {
            this.result = result;
            Done = true;
            lock.notifyAll();
        }
    }

    /**
     * @return true if(and only if) this object has been resolved, false otherwise
     */
    public boolean isDone() {
        return Done;
    }

    /**
     * retrieves the result the Future object holds if it has been resolved,
     * This method is non-blocking, it has a limited amount of time determined
     * by {@code timeout}
     * <p>
     *
     * @param timeout the maximal amount of time units to wait for the result.
     * @param unit    the {@link TimeUnit} time units to wait.
     * @return return the result of type T if it is available, if not,
     * wait for {@code timeout} TimeUnits {@code unit}. If time has
     * elapsed, return null.
     */
      public T get(long timeout, TimeUnit unit) {
        synchronized (lock) {
            while (!isDone()) {
                try {
                    lock.wait(unit.convert(timeout, unit));

                } catch (InterruptedException e) {
//                System.out.println("interrupted while waiting to be resolved ");

                    Thread.currentThread().interrupt();
                }
            }
            return result;
        }

    }


}
