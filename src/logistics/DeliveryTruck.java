package logistics;

import core.InventoryManager;
import structures.ArrayStack;
import models.Product;
import core.InventoryManager.*;

public class DeliveryTruck {
    ArrayStack<Product> Truck = new ArrayStack<Product>(50);

    public void unloadTruck() {
        for (int i = 0; i < Truck.size(); i++) {
            System.out.println("WIP");
        }
    }

}
