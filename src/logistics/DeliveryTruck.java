package logistics;

import core.InventoryManager;
import structures.ArrayStack;
import models.Product;
import core.InventoryManager.*;

public class DeliveryTruck {
    InventoryManager inventoryController;

    public DeliveryTruck(InventoryManager manager) {
        this.inventoryController = manager;
    }

    ArrayStack<Product> Truck = new ArrayStack<Product>(50);

    public void unloadTruck() {
        while (!Truck.isEmpty()) {
            Product productInTransit = Truck.pop();
            inventoryController.addProduct(productInTransit);
        }
    }

}
