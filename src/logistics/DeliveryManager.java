package logistics;

import core.DamagesManager;
import core.InventoryManager;
import engine.GameClock;
import engine.GameState;
import engine.Tickable;
import models.Product;
import structures.ArrayStack;
import upgrades.UpgradeCatalog;
import upgrades.UpgradeManager;

import java.util.Random;

public class DeliveryManager implements Tickable {
    Product[][] inboundManifest = new Product[7][];
    private InventoryManager manager;
    private DamagesManager damagesManager;
    private GameState state;
    private GameClock clock;
    private DeliveryTruck deliveryTruck;
    int arrivalTick;

    public DeliveryManager(InventoryManager manager, DamagesManager damagesManager, GameState state, GameClock clock, DeliveryTruck deliveryTruck) {
        this.manager = manager;
        this.damagesManager = damagesManager;
        this.state = state;
        this.clock = clock;
        this.deliveryTruck = deliveryTruck;
        arrivalTick = -1;
    }

    // Add Products to the next open position in the resolved arrival slot.
    public void scheduleInboundProduct(int arrivalSlot, Product loadIndex) {
        if (inboundManifest[arrivalSlot] == null && arrivalSlot <= 7) {
            inboundManifest[arrivalSlot] = new Product[UpgradeManager.getTruckCapacity()];
        }
        if (hasAvailableCargoSpace(inboundManifest, arrivalSlot)) {
        inboundManifest[arrivalSlot][findNextManifestSlot(inboundManifest, arrivalSlot)] = loadIndex;
        }
    }

    // Get arrival slot size.
    public int getScheduledManifestSize(Product[][] manifest, int arrivalSlot) {
        int scheduledSlotIndex = 0;
        boolean stopLoop = false;
        while (!stopLoop) {
            if (manifest[arrivalSlot][scheduledSlotIndex] != null) {
                scheduledSlotIndex++;
            }
            else stopLoop = true;
        }
        return scheduledSlotIndex;
    }

    // Method for determining if arrival slot is full
    public boolean hasAvailableCargoSpace(Product[][] manifest, int arrivalSlot) {
        return getScheduledManifestSize(inboundManifest, arrivalSlot) < UpgradeManager.getTruckCapacity();
    }

    // Method for determining next open slot for given arrival slot, (returns -1 if full)
    public int findNextManifestSlot(Product[][] manifest, int arrivalSlot) {
        if (!hasAvailableCargoSpace(manifest, arrivalSlot)) return -1;
        return getScheduledManifestSize(manifest, arrivalSlot);
    }

    // Method for determining inbound schedule slot based on expectedArrivalTick
    // To DO: Rewrite system to handle cases where Product is expected to arrive after day 7
    public int resolveArrivalSlot(int expectedArrivalTick) {
    if (expectedArrivalTick % 600 == 0) return expectedArrivalTick / 600;
    return (expectedArrivalTick / 600) + 1;
    }

    // Load cargo from the current arrival slot into the inbound truck.
    public DeliveryTruck dispatchScheduledTruck(){
        // If inbound schedule for current day is empty, throw Runtime Exception.
        if (getScheduledManifestSize(inboundManifest, 0) == 0) {
            throw new RuntimeException("No Cargo for Today!");
        }
        // Create inbound truck and load it with the scheduled slot cargo
        DeliveryTruck scheduledTruck = new DeliveryTruck(manager, damagesManager, state);
        for ( int i = 0; i < getScheduledManifestSize(inboundManifest, 0); i++) {
            scheduledTruck.loadTruck(inboundManifest[0][i]);
        }
        advanceInboundSchedule(inboundManifest);
        return scheduledTruck;
    }

    // Shift inbound schedule forward by one arrival slot
    public void advanceInboundSchedule(Product[][] manifest){
        // Iterate over each arrival slot in inbound manifest
        for (int i = 0; i < 7; i++) {
            Product[] temp = new Product[UpgradeManager.getTruckCapacity()];
            // Check if the array we are copying exists.
            if (inboundManifest[i + 1] == null) return;
            // For each arrival slot, copy Cargo contents to temp array
            for (int j = 0; j < getScheduledManifestSize(inboundManifest, i); j++) {
                if (inboundManifest[i+1][j] != null) {
                    temp[j] = inboundManifest[i+1][j];
                }
            }
            // Swap
            inboundManifest[i] = temp;
            inboundManifest[i + 1] = null;
        }
    }

    // Method for determining the tick in which inboundTruck arrives at dock based on truck tiers.
    public int resolveInboundArrival() {
        Random randomInt = new Random();
        if (UpgradeManager.getTruckTier() == 1) {
            return randomInt.nextInt(45, 150);
        } else if (UpgradeManager.getTruckTier() == 2) {
            return randomInt.nextInt(30, 95);
        }
        throw new RuntimeException("Illegal Truck Tier");
    }

    public void processScheduledArrival() {
        if (arrivalTick != -1) {
            if (clock.getCurrentTick() == arrivalTick) {
                    DeliveryTruck truck = dispatchScheduledTruck();
                    truck.setTruckArrival(true);
                    deliveryTruck.updateTruck(truck.getTruck());
                    arrivalTick = -1;
            }
        }

    }

    public void tick(GameState state) {
        if (clock.isNewDay()) {
            arrivalTick = resolveInboundArrival();
        }
        processScheduledArrival();
    }
}
