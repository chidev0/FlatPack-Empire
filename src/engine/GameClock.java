package engine;

public class GameClock {
    private int tick;
    private int day;
    private static final int TICKS_PER_DAY = 180;

    public void advance() {
        tick++;
        if (tick % TICKS_PER_DAY == 0) {
            day++;
        }
    }

    // Getter for Tick & Day
    public int getTick() {return tick;}
    public int getDay() { return day;}
}
