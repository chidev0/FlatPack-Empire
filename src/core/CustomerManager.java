package core;

import engine.GameClock;
import engine.GameState;
import engine.Tickable;
import models.CheckoutLane;
import models.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomerManager implements Tickable {
    private GameState state;
    private InventoryManager manager;
    private CheckoutLane lane;
    Random randomizer = new Random();
    List<Customer> customerList = new ArrayList<>();
    public static int totalDayCustomers;

    public CustomerManager(GameState state, InventoryManager manager, CheckoutLane lane) {
        this.state = state;
        this.manager = manager;
        this.lane = lane;
    }

    // Customer spawn randomizer logic
    public void spawnCustomer() {
    if (randomizer.nextDouble() <= state.getCurrentCustomerSpawnRate()) {
        // Adds customer to list, spawning them in the store.
        customerList.add(new Customer(state, manager));
        totalDayCustomers++;
        // ToDo: Decouple print statement from CustomerManager.
        System.out.println("Looks like we got a customer.\nCustomers shopping: " + customerList.size());
     }
    }

    public void populateCustomersCart() {
        if (customerList.isEmpty()) return;
        int customerSize = customerList.size();
        for (int i = customerSize - 1; i >= 0; i-- ) {
            if (customerList.get(i).getShoppingCart().isFull()) {
                lane.queueCustomer(customerList.get(i));
                customerList.remove(i);
            } else {
                if (customerList.get(i).getTicksUntilNextItem() == 0) {
                    customerList.get(i).populateCart();
                    if (!customerList.get(i).getShoppingCart().isFull()) {
                        customerList.get(i).rerollPickupDelay();
                    }
                } else {
                    customerList.get(i).advanceShoppingProgress();
                }
            }
        }
    }

    @Override
    public void tick(GameState state) {
        spawnCustomer();
        populateCustomersCart();
    }
}
