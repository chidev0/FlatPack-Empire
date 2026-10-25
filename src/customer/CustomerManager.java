package customer;

import core.CheckoutManager;
import core.DayManager;
import core.InventoryManager;
import engine.GameClock;
import engine.GameState;
import engine.TaskScheduler;
import engine.Tickable;
import models.Customer;
import ui.EventBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomerManager implements Tickable {
    private GameState state;
    private InventoryManager manager;
    private CheckoutManager checkoutManager;
    private EventBuffer eventBuffer;
    Random randomizer = new Random();
    List<Customer> customerList = new ArrayList<>();
    public static int totalDayCustomers;
    private TaskScheduler taskScheduler;
    private DayManager dayManager;
    private GameClock clock;

    public CustomerManager(GameState state, InventoryManager manager, CheckoutManager checkoutManager, EventBuffer eventBuffer, TaskScheduler taskScheduler, DayManager dayManager, GameClock clock) {
        this.state = state;
        this.manager = manager;
        this.checkoutManager = checkoutManager;
        this.eventBuffer = eventBuffer;
        this.taskScheduler = taskScheduler;
        this.dayManager = dayManager;
        this.clock = clock;
    }

    // Customer spawn randomizer logic
    public void spawnCustomer() {
    if (randomizer.nextDouble() <= state.getCurrentCustomerSpawnRate()) {
        // Adds customer to list, spawning them in the store.
        customerList.add(new Customer(state, manager));
        dayManager.getCurrentDay().handleNewCustomer();
        totalDayCustomers++;
        // ToDo: Decouple print statement from CustomerManager.
        eventBuffer.enqueueEvent("Looks like we got a customer. Customers shopping: " + customerList.size());
     }
    }

    public void populateCustomersCart() {
        if (customerList.isEmpty()) return;
        int customerSize = customerList.size();
        for (int i = customerSize - 1; i >= 0; i-- ) {
            if (customerList.get(i).getShoppingCart().isFull()) {
                checkoutManager.queueCustomer(customerList.get(i));
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

public void newPopulateCart() {
        if (customerList.isEmpty()) return;
        int customerSize = customerList.size();
        for (int i = customerList.size() - 1; i >= 0; i--) {
            if (customerList.get(i).getShoppingCart().isFull()) {
                checkoutManager.queueCustomer(customerList.get(i));
                customerList.remove(i);
            } else {
                Customer customer = customerList.get(i);
                if (customer.getCustomerState() == CustomerState.AWAITING_ITEM_TASK) {
                    taskScheduler.scheduleTask(customer.rollPickupDelay() * 10, customer::populateCart);
                }
                customerList.get(i).advanceShoppingProgress();
            }
        }
}

    public void updateCurrentCustomerSize() {
        state.setCurrentCustomers(customerList.size());
    }

    public void handleDayEnd() {
        customerList.clear();
        taskScheduler.clearTasks();
    }


    @Override
    public void tick(GameState state) {
        spawnCustomer();
        newPopulateCart();
        updateCurrentCustomerSize();
        if (clock.isRollover()) handleDayEnd();
    }
}
