package logistics;

import core.DamagesManager;
import core.InventoryManager;
import structures.ArrayStack;
import models.Product;
import java.util.Random;

public class DeliveryTruck {
    private ArrayStack<Product> Truck;
    int truckTier = 1;
    int truckCapacity;
    InventoryManager inventoryController;
    DamagesManager damageController;
    double damageChance;

    public DeliveryTruck(InventoryManager manager, DamagesManager damageController) {
        this.inventoryController = manager;
        this.damageController = damageController;
        // Logic for determining features (Truck size, Damage chance) based on Tier (Default: 1)
        if (truckTier == 1) {
            truckCapacity = 50;
            damageChance = 15;
        } else if (truckTier == 2) {
            truckCapacity = 100;
            damageChance = 7.5;
        } else {
            throw new RuntimeException("Illegal Truck Tier");
        }
        this.Truck = new ArrayStack<Product>(truckCapacity);
    }

    // Basic method for loading Truck
    public void loadTruck(Product ...item) {
        for (Product p : item) {
            if (this.Truck.isFull()) {
                System.out.println("[chIKEA Truck]: Truck has reached maximum capacity, consider upgrading to add more items");
                return;
            }
            p = inventoryController.rebuildProduct(p, p.getProductModel());
            this.Truck.push(p);
            p.setState("ON_TRUCK");
            System.out.println("[chIKEA Truck]: Added " + p.getProduct() + " " + p.getType() + " to the Truck." );
        }
    }

    // Method for unloading Truck into Inventory Array List
    public String unloadTruck() {
        int damageCount = 0;
        int inventoryCount = 0;
        if (Truck.isEmpty()) {
            return "Truck arrived empty. Not sure if this was intentional";
        }
        while (!Truck.isEmpty()) {
            Product productInTransit = Truck.pop();
            // Set Product ownership to ON_TRUCK
            productInTransit.setState("ON_TRUCK");
            // Check if Product is damaged, move to Damages if it is, otherwise move to Inventory.
            boolean checkDamage = isDamaged();
            if (checkDamage) {
                // TO DO: Update generic message.
                System.out.println("Looks like " + productInTransit.getProduct() + " " + productInTransit.getType() + " didn't make it in one piece. Adding to damages.");
                damageController.addProduct(productInTransit);
                damageCount++;
            } else {
                inventoryController.addProduct(productInTransit);
                inventoryCount++;
            }
        }
        return "\n\n      [chIKEA Truck]      \n\nProducts added to inventory: " + inventoryCount + ".\nProducts damaged: " + damageCount + "\nTotal: " +  (inventoryCount + damageCount);
    }

    // Method for Upgrading Truck Capacity
    public String upgradeTruck() {
        if (truckTier == 2) {
            return "You are at the maximum Truck Tier (WIP)";
        }
        // To DO: Create 2D array mapping Truck Tiers to maximum Truck capacity. Will help once we add more tiers.
        // To DO: Deduct cost from storeBalance once Economy is created.
        if (truckTier == 1) {
            truckTier++;
            truckCapacity = 100;
            damageChance = 7.5;
            Truck.upgradeCapacity(truckCapacity);
            return "[ chIKEA Logistics ] Tier Upgrade: You have upgraded your Truck tier.\n New Box Capacity: 100";
        }
        throw new RuntimeException("Expected a truckTier of 1 but received " + truckTier);
    }

    // Method for calculating damage chance based on tier modifiers.
    public boolean isDamaged() {
        Random damageOdds = new Random();
        double damageRoll = damageOdds.nextDouble(0, 100);
        if (truckTier < 1 || truckTier > 2) {
            throw new RuntimeException("Illegal Truck Tier");
        }
        return damageRoll <= damageChance;
    }



}
