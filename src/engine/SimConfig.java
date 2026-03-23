package engine;

public class SimConfig {
    public static final int STARTING_TRUCK_TIER = 1;
    public static int CURRENT_TRUCK_TIER = 1;
    public static final int MAX_TRUCK_TIER = 3;
    public static final int STARTING_CASHIER_LANES = 1;
    public static int CURRENT_CASHIER_LANES = 1;
    public static final int MAX_CASHIER_LANES = 5;
    public static final int SELF_CHECKOUT_LANES = 0;
    public static final int STARTING_CASHIER = 0;
    public static final int STARTING_CUSTOMER_TIER = 1;
    public static int CURRENT_CUSTOMER_TIER = 1;
    public static final double CUSTOMER_SPAWN_RATE = 0.16;
    public static final double EVENT_CHANCE = 0.35;
    public static final double SHRINK_CHANCE = 0.10;
    public static final int TICKS_PER_DAY = 180;

    // Setter for CURRENT_TRUCK_TIER
    public static void  setCurrentTruckTier(int new_tier) {
        if (new_tier >= SimConfig.MAX_TRUCK_TIER) {
            throw new RuntimeException("Maximum Truck Tier Reached");
        }
        CURRENT_TRUCK_TIER = new_tier;
    }

    public static void setCurrentCheckoutLanes(int lanes) {
        if (lanes >= SimConfig.MAX_CASHIER_LANES) {
            throw new RuntimeException("Maximum Truck Tier Reached");
        }
        CURRENT_CASHIER_LANES = lanes;
    }



}
