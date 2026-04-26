package core;

import engine.GameClock;
import engine.GameState;
import engine.GameStatus;
import engine.Tickable;
import models.Day;
import ui.Printer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;

public class DayManager implements Tickable {
    private GameClock clock;
    private GameState state;
    private ArrayList<Day> dayList = new ArrayList<>();
    private Random randomizer = new Random();
    private Day currentDay;

    public DayManager(GameClock clock, GameState state) {
        this.state = state;
        this.clock = clock;
        currentDay = new Day(1);
        currentDay.setStartingBalance(state.getCURRENT_BALANCE());
        currentDay.setRevenueGoal(calculateRevenue());
    }

    public void tick(GameState state) {
        if (clock.isRollover()) handleDayEnd(state);
        if (clock.isNewDay()) handleDayBegin(state);
    }

    public void handleDayEnd(GameState state) {
        state.setSimulationStatus(GameStatus.ROLLOVER_DAY);
        currentDay.setRevenue(state.getCURRENT_BALANCE().subtract(currentDay.getStartingBalance()));
        dayList.add(currentDay);
        System.out.println("Looks like you survived the day. Congrats, I hope you don't hate it here yet.");
        System.out.println("-- Day " + state.getCurrentDay() + "Complete --");
        System.out.println("Total Customers: " + CustomerManager.totalDayCustomers);
        System.out.println("Daily Revenue " + currentDay.getRevenue());
        System.out.println("Revenue Goal" + currentDay.getRevenueGoal());
        if (currentDay.metRevenueGoal()) {
            System.out.println("Revenue goals have been met");
        }
        else {
            System.out.println("Revenue goals have not been met");
        }
        state.setCurrentDayStartingBalance(state.getCURRENT_BALANCE());
        CustomerManager.totalDayCustomers = 0;
        Printer.pause(10000);
        state.setSimulationStatus(GameStatus.RUNNING);
    }

    public void handleDayBegin(GameState state) {
        if (!dayList.isEmpty()) currentDay = new Day(state.getCurrentDay());
        currentDay.setRevenueGoal(calculateRevenue());
        currentDay.setStartingBalance(state.getCURRENT_BALANCE());
        System.out.println("New Day begun");
    }

    public BigDecimal calculateRevenue() {
        if (dayList.size() >= 2) {
        BigDecimal revenueAverage = calculateRevenueAverage();
        BigDecimal calculateInitial = dayList.getLast().getRevenue().multiply(BigDecimal.valueOf(randomizer.nextDouble(1.10,1.30)));
        return calculateInitial.add(revenueAverage).divide(BigDecimal.TWO, 2, RoundingMode.HALF_UP);
        } else if (dayList.size() == 1) {
            return dayList.getFirst().getRevenue().multiply(BigDecimal.valueOf(randomizer.nextDouble(1.05,1.20)));
        }
        return BigDecimal.valueOf(5000);
    }

    public BigDecimal calculateRevenueAverage() {
        BigDecimal revenueAverage = BigDecimal.ZERO;
        for (Day day : dayList) {
            revenueAverage = revenueAverage.add(day.getRevenue());
        }
        return revenueAverage.divide(BigDecimal.valueOf(dayList.size()),2 , RoundingMode.HALF_UP);
    }

    public Day getCurrentDay() {
        return currentDay;
    }

}
