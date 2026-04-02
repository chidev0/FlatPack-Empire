package models;

import java.util.ArrayList;
import java.util.Random;
import engine.GameState;

public class Customer {

    private ArrayList<Product> shoppingCart;
    private Random cartRandomizer = new Random();
    private int customerTier;

    public Customer(GameState state) {
        customerTier = state.getCurrentCustomerTier();
        if (customerTier == 1) {
            this.shoppingCart = new ArrayList<>(cartRandomizer.nextInt(0,3 ));
        }
    }

}
