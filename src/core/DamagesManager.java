package core;

import models.Product;
import models.TransitManifest;
import products.ProductState;

import java.util.ArrayList;
import java.util.List;

public class DamagesManager {

    // Initializes Damages Inventory via ArrayList, separating it from store inventory
    List<Product> damages = new ArrayList<>();


    // Method for adding a single Product to Damages
    public TransitManifest addProduct(Product p) {
        TransitManifest damageManifest = TransitManifest.createForMovement("DAMAGE_ADD");
        this.damages.add(p);
        p.setState(ProductState.DAMAGED);
        damageManifest.logProduct(p);
        return damageManifest;
    }

    public int size() {
        return damages.size();
    }
}
