package engine.commands;

import engine.GameStatus;

import java.util.UUID;

public class CommandContext {
    private UUID SKU;
    private int laneId;
    private GameStatus status;
    private String displayLabel;

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

    public GameStatus getStatus() {
        return status;
    }

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
