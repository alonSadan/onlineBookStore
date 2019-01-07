package bgu.spl.mics.application;

import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.*;
/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class BookStoreRunner {
    public  static  CountDownLatch timeServiceCanStart;
    public static void main(String[] args) {
        HashMap<Integer, Customer> hashCustomers = new HashMap<>();
        ArrayList<Thread> threadlist = new ArrayList<>();
        String s1 = args[1];
       String s2 = args[2];
        String s3 = args[3];
       String s4 = args[4];


        JsonParser parser = new JsonParser();
        JsonObject o = null;
        try {
            o = parser.parse(new FileReader(args[0])).getAsJsonObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }//initial resources looks redundant to me but thats how it is in the Json example
        JsonArray initialResources = o.getAsJsonArray("initialResources");
        JsonArray vehicles = initialResources.get(0).getAsJsonObject().get("vehicles").getAsJsonArray();
        DeliveryVehicle[] cars = new DeliveryVehicle[vehicles.size()];
        for (int i = 0; i < vehicles.size(); i++) {
            JsonObject car = vehicles.get(i).getAsJsonObject();
            int license = car.get("license").getAsInt();
            int speed = car.get("speed").getAsInt();
            cars[i] = new DeliveryVehicle(license, speed);
        }
        ResourcesHolder.getInstance().load(cars);

        JsonArray inventory = o.getAsJsonArray("initialInventory");
        BookInventoryInfo[] books = new BookInventoryInfo[inventory.size()];
        for (int i = 0; i < inventory.size(); i++) {
            JsonObject book = inventory.get(i).getAsJsonObject();
            String title = book.get("bookTitle").getAsString();
            int amount = book.get("amount").getAsInt();
            int price = book.get("price").getAsInt();
            books[i] = new BookInventoryInfo(title, amount, price);
        }
        Inventory.getInstance().load(books);

        JsonObject services = o.getAsJsonObject("services");
        JsonObject time = services.getAsJsonObject("time");
        int milsPerTick = time.get("speed").getAsInt();
        int duration = time.get("duration").getAsInt();
        int ss = services.get("selling").getAsInt();
        int is = services.get("inventoryService").getAsInt();
        int l = services.get("logistics").getAsInt();
        int rs = services.get("resourcesService").getAsInt();
        int numberOfCustomers = services.get("customers").getAsJsonArray().size();


        //there was no constructor for SellingService and InventoryService so for now they get constructed with a string
        for (int i = 0; i < ss; i++) {
            SellingService s = new SellingService("SellingService" + i);
            Thread tmp = new Thread(s);
            threadlist.add(tmp);
        }
        for (int j = 0; j < is; j++) {
            InventoryService i = new InventoryService("InventoryService" + j);
            Thread tmp = new Thread(i);
            threadlist.add(tmp);
        }
        for (int i = 0; i < l; i++) {
            LogisticsService ls = new LogisticsService("LogisticsService"+ i);
            Thread tmp = new Thread(ls);
            threadlist.add(tmp);
        }
        for (int i = 0; i < rs; i++) {
            ResourceService r = new ResourceService("ResourceService" + i);
            Thread tmp = new Thread(r);
            threadlist.add(tmp);
        }
        //create customers and api services

        JsonArray customers = services.getAsJsonArray("customers");
        for (int i = 0; i < customers.size(); i++) {
            JsonObject customer = customers.get(i).getAsJsonObject();
            int id = customer.get("id").getAsInt();
            String name = customer.get("name").getAsString();
            String address = customer.get("address").getAsString();
            int distance = customer.get("distance").getAsInt();
            JsonObject creditCard = customer.get("creditCard").getAsJsonObject();
            int number = creditCard.get("number").getAsInt();
            int amount = creditCard.get("amount").getAsInt();
            Customer c = new Customer(name, id, address, distance, amount, number);
            JsonArray orderSchedule = customer.getAsJsonArray("orderSchedule");
            for (int j = 0; j < orderSchedule.size(); j++) {
                JsonObject ortick = orderSchedule.get(j).getAsJsonObject();
                String title = ortick.get("bookTitle").getAsString();
                int tick = ortick.get("tick").getAsInt();
                OrderTick ordertick = new OrderTick(title, tick);
                c.getOrderSchedule().add(ordertick);
                hashCustomers.put(c.getId(), c);
            }
            APIService api = new APIService(c, "APIService" + i);
            Thread tmp = new Thread(api);
            threadlist.add(tmp);
        }
        //
        timeServiceCanStart = new CountDownLatch(threadlist.size());
        TimeService ts = new TimeService(milsPerTick,duration);
        Thread tst = new Thread(ts);
        threadlist.add(tst);

        for (Thread t : threadlist) {
                t.start();
        }
        for (Thread t:threadlist ) {

            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //customer hashmap is args[1]
        FileOutputStream customersFile = null;
        ObjectOutputStream output = null;
        try {
            customersFile = new FileOutputStream(s1);
            try {

                output = new ObjectOutputStream(customersFile);


                output.writeObject(hashCustomers);
                output.close();
                customersFile.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Inventory.getInstance().printInventoryToFile(s2);
        MoneyRegister.getInstance().printOrderReceipts(s3);
        //print moneyregister to args[4]

        FileOutputStream moneyRegisterFile = null;
        ObjectOutputStream output1 = null;
        try {
            moneyRegisterFile = new FileOutputStream(s4);

        try {
            output1 = new ObjectOutputStream(moneyRegisterFile);
            output1.writeObject(MoneyRegister.getInstance());
            output1.close();
            moneyRegisterFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

