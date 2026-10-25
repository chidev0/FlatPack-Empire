package engine.commands;

import engine.GameStatus;
import models.CheckoutLaneSnapshot;

import java.util.UUID;

public class CommandContext {
    private UUID SKU;
    private int laneId;
    private GameStatus status;
    private String displayLabel;
    private CheckoutLaneSnapshot checkoutLane;

    public CommandContext(UUID Sku) {
        this.SKU = Sku;
    }

    public CommandContext(int laneId) {
        this.laneId = laneId;
    }

    public CommandContext(GameStatus status) {
        this.status = status;
    }

    public CommandContext(String displayLabel) {
        this.displayLabel = displayLabel;
    }

    public CommandContext(CheckoutLaneSnapshot checkoutLane) { this.checkoutLane = checkoutLane; }

    public GameStatus getStatus() {
        return status;
    }

    public CheckoutLaneSnapshot getCheckoutLane() { return checkoutLane; }

    public int getLaneId() {
        return laneId;
    }

    public UUID getSKU() {
        return SKU;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }
}
