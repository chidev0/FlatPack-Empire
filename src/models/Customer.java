package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.InventoryManager;
import customer.CustomerState;
import engine.GameState;
import engine.SimRandom;
import products.ProductState;
import structures.ArrayStack;

public class Customer {

    private ArrayStack<Product> shoppingCart;
    private Random cartRandomizer;
    private int customerTier;
    private InventoryManager manager;
    private int ticksUntilNextItem;
    private boolean processing;
    public boolean placeholder = false;
    private int cartCapacity;
    private CustomerState customerState;

    public Customer(GameState state, InventoryManager manager, SimRandom simRandom) {
        this.manager = manager;
        this.cartRandomizer = simRandom.getShopping();
        customerTier = state.getCurrentCustomerTier();
        if (customerTier == 1) {
            this.cartCapacity = cartRandomizer.nextInt(1,5);
            this.ticksUntilNextItem = cartRandomizer.nextInt(14,30);
        } else if (customerTier == 2) {
            this.cartCapacity = cartRandomizer.nextInt(5,15);
            this.ticksUntilNextItem = cartRandomizer.nextInt(10,20);
        }
        this.shoppingCart = new ArrayStack<>(cartCapacity);
        customerState = CustomerState.IDLE;
    }

    public Customer(GameState state, InventoryManager manager, boolean placeholder) {
        this.manager = manager;
        customerTier = state.getCurrentCustomerTier();
        if (customerTier == 1) {
            this.shoppingCart = new ArrayStack<>(cartRandomizer.nextInt(1,5));
            this.ticksUntilNextItem = cartRandomizer.nextInt(14,30);
        } else if (customerTier == 2) {
            this.shoppingCart = new ArrayStack<>(cartRandomizer.nextInt(5,15));
            this.ticksUntilNextItem = cartRandomizer.nextInt(10,20);
        }
        this.placeholder = placeholder;
    }

    public ArrayStack<Product> getShoppingCart() {
        return shoppingCart;
    }

    // Method for adding items to customer cart over time.
    // Current limitations: No pattern to what will be in a customers shopping cart, fully random.
    public void populateCart() {
        // Grabs random item from Store inventory and adds it to cart.
        if (shoppingCart.isFull() || processing) return;
        Product p = new Product();
        while (p.getState() != ProductState.IN_INVENTORY) {
            p = manager.getInventorySnapshot().get(cartRandomizer.nextInt(0, manager.getInventorySnapshot().size()));
        }
        shoppingCart.push(p);
        System.out.println("Added an item to a customers cart");
        p.setState(ProductState.IN_CART);
    }

    public int getTicksUntilNextItem() {
        return ticksUntilNextItem;
    }

    public void rerollPickupDelay() {
        if (customerTier == 1) {
            this.ticksUntilNextItem = cartRandomizer.nextInt(80,150);
        } else if (customerTier == 2) {
            this.ticksUntilNextItem = cartRandomizer.nextInt(60,120);
        }
    }

    public int rollPickupDelay() {
        if (customerTier == 1) {
            int pickupDelayRoll = cartRandomizer.nextInt(80,150);
            this.ticksUntilNextItem = pickupDelayRoll * 10;
            customerState = CustomerState.SHOPPING;
            return pickupDelayRoll;
        } else if (customerTier == 2) {
            int pickupDelayRoll = cartRandomizer.nextInt(60,120);
            this.ticksUntilNextItem = pickupDelayRoll * 10;
            customerState = CustomerState.SHOPPING;
            return pickupDelayRoll;
        }
        throw new RuntimeException("Illegal customer tier");
    }

    public void advanceShoppingProgress() {
        ticksUntilNextItem--;
        if (ticksUntilNextItem <= 0) {
            customerState = CustomerState.AWAITING_ITEM_TASK;
        }
    }

    public void setProcessing(boolean processing) {
        this.processing = processing;
    }

    public void setCustomerState(CustomerState customerState) {
        this.customerState = customerState;
    }

    public CustomerState getCustomerState() {
        return customerState;
    }
}
