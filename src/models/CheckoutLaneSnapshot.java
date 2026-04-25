package models;

import java.math.BigDecimal;

public class CheckoutLaneSnapshot {
    private int laneNumber;
    private int itemsProcessed;
    private int itemsRemaining;
    private int ticksUntilNextScan;
    private BigDecimal transactionTotal;
    private boolean isLaneProcessing;

    public CheckoutLaneSnapshot(int laneNumber, int itemsProcessed, int itemsRemaining, int ticksUntilNextScan, BigDecimal transactionTotal, boolean isLaneProcessing) {
        this.laneNumber = laneNumber;
        this.itemsRemaining = itemsRemaining;
        this.ticksUntilNextScan = ticksUntilNextScan;
        this.transactionTotal = transactionTotal;
        this.itemsProcessed = itemsProcessed;
        this.isLaneProcessing = isLaneProcessing;
    }

    public int getItemsProcessed() {
        return itemsProcessed;
    }

    public int getLaneNumber() {
        return laneNumber;
    }

    public int getTicksUntilNextScan() {
        return ticksUntilNextScan;
    }

    public int getItemsRemaining() {
        return itemsRemaining;
    }

    public BigDecimal getTransactionTotal() {
        return transactionTotal;
    }

    public boolean isLaneProcessing() {
        return isLaneProcessing;
    }



}
