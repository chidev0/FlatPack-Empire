package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import core.InventoryManager;
import engine.GameState;

public class Customer {

    private ArrayList<Product> shoppingCart;
    private Random cartRandomizer = new Random();
    private int customerTier;
    private InventoryManager manager;

    public Customer(GameState state, InventoryManager manager) {
        this.manager = manager;
        customerTier = state.getCurrentCustomerTier();
        if (customerTier == 1) {
            this.shoppingCart = new ArrayList<>(cartRandomizer.nextInt(0,10 ));
        } else if (customerTier == 2) {
            this.shoppingCart = new ArrayList<>(cartRandomizer.nextInt(5,15));
        }
    }

    public ArrayList<Product> getShoppingCart() {
        return shoppingCart;
    }

    public void populateCart() {
        Product p = manager.getInventorySnapshot().get(cartRandomizer.nextInt(0,manager.getInventorySnapshot().size()));
        shoppingCart.add(p);
    }
}
