package core;

import engine.GameState;

import java.math.BigDecimal;

public class RevenueManager {
    private GameState state;

    public RevenueManager(GameState state) {
        this.state = state;
    }

    public void addToBalance(BigDecimal bal) {
        state.setCURRENT_BALANCE(state.getCURRENT_BALANCE().add(bal));
    }
}
