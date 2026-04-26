package models;

import java.math.BigDecimal;

public class Day {
    private int day;
    private int customersInStore;
    private int totalTraffic;
    private BigDecimal revenue;
    private BigDecimal losses;
    private BigDecimal revenueGoal;
    private BigDecimal startingBalance;

    public Day(int day) {
        this.day = day;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getCustomersInStore() {
        return customersInStore;
    }

    public void setCustomersInStore(int customersInStore) {
        this.customersInStore = customersInStore;
    }

    public int getTotalTraffic() {
        return totalTraffic;
    }

    public void setTotalTraffic(int totalTraffic) {
        this.totalTraffic = totalTraffic;
    }

    public BigDecimal getRevenue() {
        return revenue;
    }

    public void setRevenue(BigDecimal revenue) {
        this.revenue = revenue;
    }

    public BigDecimal getLosses() {
        return losses;
    }

    public void setLosses(BigDecimal losses) {
        this.losses = losses;
    }

    public BigDecimal getRevenueGoal() {
        return revenueGoal;
    }

    public void setRevenueGoal(BigDecimal revenueGoal) {
        this.revenueGoal = revenueGoal;
    }

    public BigDecimal getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(BigDecimal startingBalance) {
        this.startingBalance = startingBalance;
    }

    public void handleNewCustomer() {
        totalTraffic = totalTraffic + 1;
        customersInStore = customersInStore + 1;
    }

    public boolean metRevenueGoal() {
        return revenue.equals(revenueGoal);
    }
}

