package engine;

import java.util.Arrays;

public class GameClock {
    private int tick;
    private static final int TICKS_PER_DAY = SimConfig.TICKS_PER_DAY;
    private static final int SIMULATION_SPEED = SimConfig.SIMULATION_SPEED;
    private static final int[][] time = {
            {10,11,12,1,2,3,4,5,6,7},
            {0,60,120,180,240,300,360,420,480,540,600}
    };


    public void advance() {
        if (!GameState.isPAUSED()) {
            tick++;
            if (tick % TICKS_PER_DAY == 0) {
                GameState.setCurrentDay(GameState.getCurrentDay() + 1);
            }
        }
    }

    // Getter for Tick & Day
    public int getTick() {
        return tick;
    }

    public boolean isNewDay() {
        return tick % TICKS_PER_DAY == 0;
    }

    // Method For Evaluating EOD (Last hour of business)
    public boolean endOfDay() {
        return tick % TICKS_PER_DAY <= 60;
    }

    // Method for evaluating start of day (First 30 minutes of business)
    public boolean startOfDay() {
        return tick % TICKS_PER_DAY >= 570;
    }

    // Getter for SIMULATION_SPEED
    public int getSimulationSpeed() {
        return SIMULATION_SPEED;
    }

    // Setter that sets SIMULATION_SPEED

    public static void setSimulationSpeed(int SIMULATION_SPEED) {
        int[] APPROVED_SIM_SPEEDS = {1, 2, 4, 8}; // Array of Speed Modifiers (1x, 2x, 4x, 8x)
        if (Arrays.binarySearch(APPROVED_SIM_SPEEDS, SIMULATION_SPEED) > 0) { // Searches sorted array for SIMULATION_SPEED input
            GameState.setCurrentSimulationSpeed(GameClock.SIMULATION_SPEED / SIMULATION_SPEED); // If int is found, update the speed by dividing default SPEED (100ms) by speed modifier.
            return;
        }
        throw new RuntimeException("Illegal Speed Modifier");
    }

    // Method for evaluating current time.
    public String getCurrentTime() {
        int targetTick = -1;
        int targetHour = -1;
    for (int i = 0; i < time.length; i++) {
        if (tick >= time[i][0] && tick < time[i + 1][0]) {
            targetTick = time[i][0];
            targetHour = i;
        }
    }
    if (targetTick > 0) {
    int minute = tick - targetTick;
    if (minute == 0) {
        minute = 00;
    }
    return targetHour + ":" + minute;
    }
    throw new RuntimeException("Something went wrong pulling the time");
    }
}
