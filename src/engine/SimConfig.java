package engine;

import java.math.BigDecimal;
import core.InventoryManager;
import products.ProductCatalog;

public class SimConfig {
    public static final int STARTING_TRUCK_TIER = 1;
    public static final int MAX_TRUCK_TIER = 3;
    public static final int STARTING_CASHIER_LANES = 1;
    public static final int MAX_CASHIER_LANES = 5;
    public static final int SELF_CHECKOUT_LANES = 0;
    public static final int STARTING_CASHIER = 0;
    public static final int STARTING_CUSTOMER_TIER = 1;
    public static final double STARTING_CUSTOMER_SPAWN_RATE = 0.0010;
    public static final double EVENT_CHANCE = 0.35;
    public static final double SHRINK_CHANCE = 0.10;
    public static final int TICKS_PER_DAY = 600;
    public static final int SIMULATION_SPEED = 50; // Default Sim Speed, 1000ms (1s) per Tick.
    public static final BigDecimal STARTING_BALANCE = BigDecimal.valueOf(1500.00);
    private InventoryManager manager;

    public SimConfig(InventoryManager manager) {
        this.manager = manager;
    }

    public void initializeStore() {
        manager.addProduct(ProductCatalog.AlexDeskW, 30);
        manager.addProduct(ProductCatalog.AlexDeskB, 30);
        manager.addProduct(ProductCatalog.BillyBookcaseBr, 30);
        manager.addProduct(ProductCatalog.Swedish_Meatballs, 30);
    }



}
