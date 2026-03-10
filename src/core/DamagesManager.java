package core;

import models.Product;

import java.util.ArrayList;
import java.util.List;

public class DamagesManager {

    // Initializes Damages Inventory via ArrayList, separating it from store inventory
    List<Product> damages = new ArrayList<>();


    // Method for adding a single Product to Store Inventory
    public String addProduct(Product p) {
        this.damages.add(p);
        p.setState("DAMAGED");
        return "[chIKEA Inventory]: Added 1x " + p.getProduct() + " " + p.getType() + " to damages.";
    }
}
