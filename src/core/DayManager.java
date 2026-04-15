package core;

import engine.GameClock;
import engine.GameState;
import engine.GameStatus;
import engine.Tickable;
import ui.Printer;

public class DayManager implements Tickable {
    private GameClock clock;

    public DayManager(GameClock clock) {
        this.clock = clock;
    }

    public void tick(GameState state) {
        if (clock.isRollover()) {
            state.setSimulationStatus(GameStatus.ROLLOVER_DAY);
            handleDayEnd(state);
            Printer.pressEnterToContinue();
        }
    }

    public void handleDayEnd(GameState state) {
        System.out.println("Looks like you survived the day. Congrats, I hope you don't hate it here yet.");
        System.out.println("-- Day " + state.getCurrentDay() + "Complete --");
        System.out.println("Total Customers: " + CustomerManager.totalDayCustomers);
        System.out.println("Start of Day Revenue: " + state.getCurrentDayStartingBalance());
        System.out.println("End of Day Revenue: " + state.getCURRENT_BALANCE());
        state.setCurrentDayStartingBalance(state.getCURRENT_BALANCE());
        CustomerManager.totalDayCustomers = 0;
    }

    public void handleDayBegin(GameState state) {

    }
}
