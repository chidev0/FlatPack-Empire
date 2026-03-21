package logistics;

import core.DamagesManager;
import core.InventoryManager;
import exceptions.CapacityExceededException;
import exceptions.EmptyStructureException;
import models.ProductState;
import models.TransitManifest;
import models.UnloadManifest;
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
    public TransitManifest loadTruck(Product ...item) {
        TransitManifest loadManifest = TransitManifest.createForMovement("TRUCK_ADD");
        for (Product p : item) {
            if (this.Truck.isFull()) {
                throw new CapacityExceededException(this.Truck.size());
            }
            p = inventoryController.rebuildProduct(p, p.getProductModel());
            this.Truck.push(p);
            loadManifest.logProduct(p);
            p.setState(ProductState.ON_TRUCK);
        }
        return loadManifest;
    }

    // Method for unloading Truck into Inventory Array List
    public UnloadManifest unloadTruck() {
        int inventoryCount = 0;
        if (Truck.isEmpty()) {
            throw new EmptyStructureException();
        }
        UnloadManifest truckManifest = new UnloadManifest();
        while (!Truck.isEmpty()) {
            Product productInTransit = Truck.pop();
            // Check if Product is damaged, move to Damages if it is, otherwise move to Inventory.
            boolean checkDamage = isDamaged();
            if (checkDamage) {
                // TO DO: Update generic message.

                truckManifest.logItemDamaged(productInTransit);
                damageController.addProduct(productInTransit);
            } else {
                inventoryController.addProduct(productInTransit);
                inventoryCount++;
            }
        }
        truckManifest.setTotalProcessed(inventoryCount + truckManifest.getDamageLog().size());
        truckManifest.setInventoryCount(inventoryCount);
        return truckManifest;
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
