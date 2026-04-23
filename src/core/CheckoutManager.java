package core;
import engine.GameState;
import models.CheckoutLane;

import java.util.ArrayList;

public class CheckoutManager {
    ArrayList<CheckoutLane> activeLanes = new ArrayList<>();
    private GameState state;
    private InventoryManager manager;
    private RevenueManager accountant;

    public void initializeCheckout() {
        CheckoutLane laneOne = new CheckoutLane(state, manager, accountant, 1);
        activeLanes.add(laneOne);
        if (state.getCurrentCheckoutLanes() > 1) {
            for (int i = 1; i < state.getCurrentCheckoutLanes(); i++) {
                activeLanes.add(new CheckoutLane(state, manager, accountant, i + 1));
            }
        }
    }

    public void processLanes() {
        for (CheckoutLane lane : activeLanes) {
            if (lane.isAwaitingCustomer()) lane.releaseCustomer();
            if (lane.isTransactionComplete()) {
                accountant.addToBalance(lane.getLaneTransaction().calculateTotal());
                lane.setCustomerAwaitStatus(true);
            } else {
                lane.processCheckoutProgress();
            }
        }
    }

    public CheckoutLane getLaneByNumber(int laneNumber) {
        for (CheckoutLane lane : activeLanes) {
            if (lane.getLaneNumber() == laneNumber) {
                return lane;
            }
        }
        throw new RuntimeException("Illegal lane number provided");
    }

    public void buildLaneSnapshot(int laneNumber) {
        try {
            CheckoutLane lane = getLaneByNumber(laneNumber);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }



}
