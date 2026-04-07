package core;

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

    public CustomerManager(GameState state, InventoryManager manager, CheckoutLane lane) {
        this.state = state;
        this.manager = manager;
        this.lane = lane;
    }

    public void spawnCustomer() {
    if (randomizer.nextDouble() <= state.getCurrentCustomerSpawnRate()) {
        customerList.add(new Customer(state, manager));
     }
    }

    public void populateCustomersCart() {
        if (customerList.isEmpty()) return;
        for (Customer i : customerList) {
            if (!i.getShoppingCart().isEmpty()) {
                lane.queueCustomer(i);
                customerList.remove(i);
            } else {
                i.populateCart();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void tick(GameState state) {
        spawnCustomer();
    }

    public void run() {
        populateCustomersCart();
    }
}
