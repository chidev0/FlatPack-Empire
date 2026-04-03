package logistics;

import core.DamagesManager;
import core.InventoryManager;
import engine.GameState;
import engine.Tickable;
import exceptions.CapacityExceededException;
import exceptions.EmptyStructureException;
import exceptions.TruckNotAtDockException;
import products.ProductState;
import models.TransitManifest;
import models.UnloadManifest;
import structures.ArrayStack;
import models.Product;
import upgrades.UpgradeCatalog;

import java.util.Random;

public class DeliveryTruck implements Tickable {
    private ArrayStack<Product> truck;
    InventoryManager inventoryController;
    DamagesManager damageController;
    GameState state;
    int truckCapacity;
    double damageChance;
    int truckTier;
    UnloadManifest truckManifest;
    int inventoryCount = 0;
    boolean truckAtDock = false;

    public DeliveryTruck(InventoryManager manager, DamagesManager damageController, GameState state) {
        this.inventoryController = manager;
        this.damageController = damageController;
        this.state = state;
        this.truckManifest = new UnloadManifest();
        // Logic for determining features (Truck size, Damage chance) based on Tier (Default: 1)
        this.truckTier = state.getCurrentTruckTier();
        if (truckTier == 1) {
            truckCapacity = UpgradeCatalog.truckTierOne.getCapacity();
            damageChance = UpgradeCatalog.truckTierOne.getDamageChance();
        } else if (truckTier == 2) {
            truckCapacity = UpgradeCatalog.truckTierTwo.getCapacity();
            damageChance = UpgradeCatalog.truckTierTwo.getDamageChance();
        } else {
            throw new RuntimeException("Illegal Truck Tier");
        }
        this.truck = new ArrayStack<Product>(truckCapacity);
    }

    // Basic method for loading Truck
    public TransitManifest loadTruck(Product ...item) {
        TransitManifest loadManifest = TransitManifest.createForMovement("TRUCK_ADD");
        for (Product p : item) {
            if (this.truck.isFull()) {
                throw new CapacityExceededException(this.truck.size());
            }
            p = p.copy();
            this.truck.push(p);
            loadManifest.logProduct(p);
            p.setState(ProductState.ON_TRUCK);
        }
        return loadManifest;
    }

    // Method for unloading Truck into Inventory Array List
    public UnloadManifest advance() {
        if (truck.isEmpty()) {
            throw new EmptyStructureException();
        }
        if (truckAtDock) {
            Product productInTransit = truck.pop();
            // Check if Product is damaged, move to Damages if it is, otherwise move to Inventory.
            boolean checkDamage = isDamaged();
            if (checkDamage) {
                // TO DO: Update generic message.

                truckManifest.logItemDamaged(productInTransit);
                damageController.addProduct(productInTransit);
            } else {
                // TO DO: Implement pallet logic instead of direct inventory transfer
                inventoryController.addProduct(productInTransit);
                inventoryCount++;
            }

            truckManifest.setTotalProcessed(inventoryCount + truckManifest.getDamageLog().size());
            truckManifest.setInventoryCount(inventoryCount);
            return truckManifest;
        }
        throw new TruckNotAtDockException();
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
            truckCapacity = UpgradeCatalog.truckTierTwo.getCapacity();
            damageChance = UpgradeCatalog.truckTierTwo.getDamageChance();
            truck.upgradeCapacity(truckCapacity);
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

    public boolean isTruckAtDock() { return truckAtDock; }
    public void setTruckArrival(boolean truckAtDock) { this.truckAtDock = truckAtDock; }



    public void tick(GameState state) {
        try {
            advance();
        } catch (EmptyStructureException | TruckNotAtDockException _) {
        }
    }
}
