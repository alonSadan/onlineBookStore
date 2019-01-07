package bgu.spl.mics;

import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;


public class FutureTest1 implements Serializable {
    Future f = null;
    Object result = null;

    @Before
    public void setUp() {
        this.result = new Object();
        f=new Future();
    }

   public void tearDown() {
    }
    @Test
     public void testResolve() {


        assertTrue(f.get(30, TimeUnit.SECONDS) ==null);
        f.resolve(result);
        assertTrue(f.get(30,TimeUnit.SECONDS) == result);


        try{f.resolve(result);}
        catch (Exception e) {
            System.out.println("if you see this message it means that exception was being thrown and everithing is fine");}

    }

    @Test
   public void testGet() {

        //test blocking part//

        Thread t = new Thread(()->{
            f.get();
        });
        t.start();
        assertTrue(t.getState().toString().equals("BLOCKED") );
        t.interrupt();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        f.resolve(result);
        t = new Thread(()->{
            f.get();
        });
        t.start();
        assertTrue(t.getState().toString().equals("TERMINATED") );

        //tests return part//
        assertEquals(f.get(), result);

    }

    @Test
  public  void testIsDone() {
        assertTrue(! f.isDone());
        f.resolve(result);
        assertTrue(f.isDone());
    }

  public  void testGet1() {
        assertTrue(f.get(30, TimeUnit.SECONDS)==null);
        f.resolve(result);
        assertTrue(f.get(30, TimeUnit.SECONDS)==result);
    }

    @Test
    public void get() {
    }

    @Test
    public void resolve() {
    }

    @Test
    public void isDone() {
    }

    @Test
    public void get1() {
    }

}