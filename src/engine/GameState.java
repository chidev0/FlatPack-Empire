package engine;

public class GameState {
    private static int CURRENT_CHECKOUT_LANES;
    private static int CURRENT_TRUCK_TIER;
    private static int CURRENT_CUSTOMER_TIER;
    private static int CURRENT_DAY;
    private static int CURRENT_SIMULATION_SPEED;
    private static boolean PAUSE;


    // Setter for CURRENT_TRUCK_TIER
    public static void setCurrentTruckTier(int new_tier) {
        if (new_tier >= SimConfig.MAX_TRUCK_TIER) {
            throw new RuntimeException("Maximum Truck Tier Reached");
        }
        CURRENT_TRUCK_TIER = new_tier;
    }

    public static void setCurrentCheckoutLanes(int lanes) {
        if (lanes >= SimConfig.MAX_CASHIER_LANES) {
            throw new RuntimeException("Maximum Truck Tier Reached");
        }
        CURRENT_CHECKOUT_LANES = lanes;
    }

    public static int getCurrentCheckoutLanes() {
        return CURRENT_CHECKOUT_LANES;
    }

    public static int getCurrentCustomerTier() {
        return CURRENT_CUSTOMER_TIER;
    }

    public static int getCurrentTruckTier() {
        return CURRENT_TRUCK_TIER;
    }

    public static int getCurrentDay() {
        return CURRENT_DAY;
    }

    public static void setCurrentDay(int currentDay) {
        CURRENT_DAY = currentDay;
    }

    public static int getCurrentSimulationSpeed() {
        return CURRENT_SIMULATION_SPEED;
    }

    public static void setCurrentSimulationSpeed(int currentSimulationSpeed) {
        CURRENT_SIMULATION_SPEED = currentSimulationSpeed;
    }

    public static boolean isPAUSED() {
        return PAUSE;
    }

    public static void setPAUSE(boolean PAUSE) {
        GameState.PAUSE = PAUSE;
    }
}
