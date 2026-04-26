package core;
import engine.GameState;
import engine.Tickable;
import models.CheckoutLane;
import models.CheckoutLaneSnapshot;
import models.Customer;
import ui.ActiveViewContext;
import ui.EventBuffer;
import ui.ViewMode;

import java.util.ArrayList;

public class CheckoutManager implements Tickable {
    ArrayList<CheckoutLane> activeLanes = new ArrayList<>();
    private final GameState state;
    private final InventoryManager manager;
    private final RevenueManager accountant;
    private EventBuffer eventBuffer;
    private DayManager dayManager;

    public CheckoutManager(GameState state, InventoryManager manager, RevenueManager accountant, EventBuffer eventBuffer, DayManager dayManager) {
        this.state = state;
        this.manager = manager;
        this.accountant = accountant;
        this.eventBuffer = eventBuffer;
        this.dayManager = dayManager;
        initializeCheckout();
    }


    public void initializeCheckout() {
        CheckoutLane laneOne = new CheckoutLane(manager, accountant, 1, eventBuffer, state);
        activeLanes.add(laneOne);
        if (state.getCurrentCheckoutLanes() > 1) {
            for (int i = 1; i < state.getCurrentCheckoutLanes(); i++) {
                activeLanes.add(new CheckoutLane(manager, accountant, i + 1, eventBuffer, state));
            }
        }
    }

    public void processLanes() {
        for (CheckoutLane lane : activeLanes) {
            if (lane.isAwaitingCustomer() && !lane.isEmpty()) {
                lane.releaseCustomer();
                lane.setCustomerAwaitStatus(false);
                eventBuffer.enqueueEvent("Customer being processed in lane " + lane.getLaneNumber() + " (" + lane.getCurrentCustomer().getShoppingCart().size() + ")");
            }
            if (lane.isTransactionComplete()) {
                accountant.addToBalance(lane.getLaneTransaction().calculateTotal());
                lane.setCustomerAwaitStatus(true);
                lane.resetCurrentCustomer();
                lane.setCurrentlyProcessingCustomer(false);
                dayManager.getCurrentDay().setCustomersInStore(dayManager.getCurrentDay().getCustomersInStore() - 1);
                eventBuffer.enqueueEvent("Finished processing Customer in lane " + lane.getLaneNumber());
            } else {
                lane.processCheckoutProgress();
                //eventBuffer.enqueueEvent("[LANE " + lane.getLaneNumber() + "] " + lane.getCurrentProduct().getProduct() + " " + lane.getCurrentProduct().getType() + " (" + lane.getItemsProcessed() + "/" + lane.getItemsRemaining() + ") - Total: " + lane.getLaneTransaction().calculateTotal());
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

    public CheckoutLaneSnapshot buildLaneSnapshot(int laneNumber) {
        try {
            CheckoutLane lane = getLaneByNumber(laneNumber);
            return new CheckoutLaneSnapshot(laneNumber, lane.getItemsProcessed(), lane.getItemsRemaining(), lane.getTicksUntilNextItemProcessed(), lane.getLaneTransaction().calculateTotal(), !lane.isAwaitingCustomer());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public ActiveViewContext buildLaneOverview() {
        CheckoutLaneSnapshot[] laneSnapshotArray = new CheckoutLaneSnapshot[activeLanes.size()];
        for (int i = 0; i < activeLanes.size(); i++) {
            laneSnapshotArray[i] = buildLaneSnapshot(activeLanes.get(i).getLaneNumber());
        }
        return new ActiveViewContext(laneSnapshotArray, ViewMode.STORE_OVERVIEW);
    }

    public ActiveViewContext buildLaneOverview(int laneID) {
        try {
            return new ActiveViewContext(buildLaneSnapshot(laneID));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public void queueCustomer(Customer customer) {
    findShortestLane().queueCustomer(customer);
    }

    public CheckoutLane findShortestLane() {
        int i = -1;
        int laneNumber = 1;
        for (CheckoutLane lane : activeLanes) {
            if (i == -1) {
                i = lane.getLaneSize();
                laneNumber = lane.getLaneNumber();
                continue;
            }
            if (lane.getLaneSize() < i) {
                i = lane.getLaneSize();
                laneNumber = lane.getLaneNumber();
            }
        }
        return getLaneByNumber(laneNumber);
    }

    public boolean queuedCustomerStatus() {
        for (CheckoutLane lane : activeLanes) {
            if (!lane.isEmpty() || lane.currentlyProcessingCustomer()) {
                return true;
            }
        }
        return false;
    }

    public void tick(GameState state) {
        if (queuedCustomerStatus()) {
            processLanes();
        }
    }
}
