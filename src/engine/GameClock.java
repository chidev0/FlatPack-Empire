package engine;

import java.util.Arrays;

public class GameClock {
    public GameState GameState;

    private int tick;
    private static final int TICKS_PER_DAY = SimConfig.TICKS_PER_DAY;
    private static final int SIMULATION_SPEED = SimConfig.SIMULATION_SPEED;
    private static final int[][] time = {
            {10,11,12,1,2,3,4,5,6,7,8},
            {0,60,120,180,240,300,360,420,480,540,600}
    };

    public GameClock(GameState GameState) {
        this.GameState = GameState;
    }


    public void advance() {
            tick++;
            if (tick % TICKS_PER_DAY == 0) {
                GameState.setCurrentDay(GameState.getCurrentDay() + 1);
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
        return tick % TICKS_PER_DAY >= 540;
    }

    // Method for evaluating start of day (First 30 minutes of business)
    public boolean startOfDay() {
        return tick % TICKS_PER_DAY <= 30;
    }

    // Getter for SIMULATION_SPEED
    public int getSimulationSpeed() {
        return GameState.getCurrentSimulationSpeed();
    }

    // Setter that sets SIMULATION_SPEED

    public void setSimulationSpeed(int SIMULATION_SPEED) {
        int[] APPROVED_SIM_SPEEDS = {1, 2, 4, 8}; // Array of Speed Modifiers (1x, 2x, 4x, 8x)
        if (Arrays.binarySearch(APPROVED_SIM_SPEEDS, SIMULATION_SPEED) >= 0) { // Searches sorted array for SIMULATION_SPEED input
            GameState.setCurrentSimulationSpeed(GameClock.SIMULATION_SPEED / SIMULATION_SPEED); // If int is found, update the speed by dividing default SPEED (100ms) by speed modifier.
            return;
        }
        throw new RuntimeException("Illegal Speed Modifier");
    }

    // Method for evaluating current time.
    public String getCurrentTime() {
        int targetTick = -1;
        int targetHour = -1;
        boolean isMorning = false;
        String timeType;
        for (int i = 0; i < time[0].length - 1; i++) { // Loop through each hour in the first row
            if (tick % TICKS_PER_DAY >= time[1][i] && tick % TICKS_PER_DAY < time[1][i + 1]) { // Check if current tick is greater than/equal to the tick associated with iterated hour AND less than tick associated with the hour in the column next to it.
                targetTick = time[1][i]; // If both conditions true, log the hour and the start tick associated with that hour
                targetHour = time[0][i];
                if (targetTick < 120) {
                    isMorning = true;
                }
            }
        }
        if (targetTick >= 0) { // Checks if result is found
            String minute = String.valueOf((tick % TICKS_PER_DAY) - targetTick); // Grab current minute by subtracting current game tick by hour start tick
            if (Integer.parseInt(minute) < 10) {
                minute = "0" + minute;
            }
            if (isMorning) {
                timeType = "AM";
            } else {
                timeType = "PM";
            }
            return targetHour + ":" + minute + " " + timeType;
        }
        throw new RuntimeException("Something went wrong pulling the time");
    }
}
