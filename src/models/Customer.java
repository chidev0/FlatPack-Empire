package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.InventoryManager;
import engine.GameState;
import products.ProductState;
import structures.ArrayStack;

public class Customer {

    private ArrayStack<Product> shoppingCart;
    private Random cartRandomizer = new Random();
    private int customerTier;
    private InventoryManager manager;
    private int ticksUntilNextItem;

    public Customer(GameState state, InventoryManager manager) {
        this.manager = manager;
        customerTier = state.getCurrentCustomerTier();
        if (customerTier == 1) {
            this.shoppingCart = new ArrayStack<>(cartRandomizer.nextInt(1,5));
            this.ticksUntilNextItem = cartRandomizer.nextInt(14,30);
        } else if (customerTier == 2) {
            this.shoppingCart = new ArrayStack<>(cartRandomizer.nextInt(5,15));
            this.ticksUntilNextItem = cartRandomizer.nextInt(10,20);
        }
    }

    public ArrayStack<Product> getShoppingCart() {
        return shoppingCart;
    }

    // Method for adding items to customer cart over time.
    // Current limitations: No pattern to what will be in a customers shopping cart, fully random.
    public void populateCart() {
        // Grabs random item from Store inventory and adds it to cart.
        Product p = new Product();
        while (p.getState() != ProductState.IN_INVENTORY) {
            p = manager.getInventorySnapshot().get(cartRandomizer.nextInt(0, manager.getInventorySnapshot().size()));
        }
        shoppingCart.push(p);
        p.setState(ProductState.IN_CART);
    }

    public int getTicksUntilNextItem() {
        return ticksUntilNextItem;
    }

    public void rerollPickupDelay() {
        if (customerTier == 1) {
            this.ticksUntilNextItem = cartRandomizer.nextInt(8,15);
        } else if (customerTier == 2) {
            this.ticksUntilNextItem = cartRandomizer.nextInt(6,12);
        }
    }

    public void advanceShoppingProgress() {
        ticksUntilNextItem--;
    }
}
