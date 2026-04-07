package engine;

import java.math.BigDecimal;

public class GameState {
    private GameStatus GAME_STATUS = GameStatus.RUNNING;

    private BigDecimal CURRENT_BALANCE = SimConfig.STARTING_BALANCE;
    private int CURRENT_CHECKOUT_LANES = SimConfig.STARTING_CASHIER_LANES;
    private int CURRENT_TRUCK_TIER = SimConfig.STARTING_TRUCK_TIER;
    private int CURRENT_CUSTOMER_TIER = SimConfig.STARTING_CUSTOMER_TIER;
    private int CURRENT_DAY;
    private int CURRENT_SIMULATION_SPEED = SimConfig.SIMULATION_SPEED;
    private double CURRENT_CUSTOMER_SPAWN_RATE = SimConfig.STARTING_CUSTOMER_SPAWN_RATE;
    private boolean PAUSE;


    // Setter for CURRENT_TRUCK_TIER
    public void setCurrentTruckTier(int new_tier) {
        if (new_tier > SimConfig.MAX_TRUCK_TIER) {
            throw new RuntimeException("Maximum Truck Tier Reached");
        }
        CURRENT_TRUCK_TIER = new_tier;
    }

    public void setCurrentCheckoutLanes(int lanes) {
        if (lanes > SimConfig.MAX_CASHIER_LANES) {
            throw new RuntimeException("Maximum Cashier Lanes Reached");
        }
        CURRENT_CHECKOUT_LANES = lanes;
    }

    public int getCurrentCheckoutLanes() {
        return CURRENT_CHECKOUT_LANES;
    }

    public int getCurrentCustomerTier() {
        return CURRENT_CUSTOMER_TIER;
    }

    public int getCurrentTruckTier() {
        return CURRENT_TRUCK_TIER;
    }

    public int getCurrentDay() {
        return CURRENT_DAY;
    }

    public void setCurrentDay(int currentDay) {
        CURRENT_DAY = currentDay;
    }

    public int getCurrentSimulationSpeed() {
        return CURRENT_SIMULATION_SPEED;
    }

    public void setCurrentSimulationSpeed(int currentSimulationSpeed) {
        CURRENT_SIMULATION_SPEED = currentSimulationSpeed;
    }

    public GameStatus getSimulationStatus() {
        return GAME_STATUS;
    }

    public void pauseSimulation() {
        GAME_STATUS = GameStatus.PAUSED;
    }

    public BigDecimal getCURRENT_BALANCE() {
        return CURRENT_BALANCE;
    }

    public void setCURRENT_BALANCE(BigDecimal CURRENT_BALANCE) {
        this.CURRENT_BALANCE = CURRENT_BALANCE;
    }

    public double getCurrentCustomerSpawnRate() {
        return CURRENT_CUSTOMER_SPAWN_RATE;
    }

    public void setCurrentCustomerSpawnRate(double CURRENT_CUSTOMER_SPAWN_RATE) {
        this.CURRENT_CUSTOMER_SPAWN_RATE = CURRENT_CUSTOMER_SPAWN_RATE;
    }
}
