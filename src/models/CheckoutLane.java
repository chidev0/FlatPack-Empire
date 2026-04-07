package models;

import core.InventoryManager;
import core.RevenueManager;
import engine.GameState;
import structures.ArrayQueue;

import java.util.ArrayList;
import java.util.List;

public class CheckoutLane {
    private List<ArrayQueue<Customer>> lanes = new ArrayList<>();
    ArrayQueue<Customer> laneOne;
    ArrayQueue<Customer> laneTwo;
    Customer currentCustomer;
    InventoryManager manager;
    RevenueManager accountant;
    TransactionReceipt laneOneTransaction;
    private boolean isTransactionCompleteL1;
    private boolean isTransactionCompleteL2;

    public CheckoutLane(GameState state, InventoryManager manager, RevenueManager accountant) {
        this.manager = manager;
        this.accountant = accountant;
        int CASHIER_LANES = state.getCurrentCheckoutLanes();
        this.laneOne = new ArrayQueue<>();
        lanes.add(laneOne);
        if (CASHIER_LANES == 2) {
            this.laneTwo = new ArrayQueue<>();
            lanes.add(laneTwo);
        }
    }

    public void queueCustomer(Customer customer) {
        if (lanes.size() == 1) {
            laneOne.enqueue(customer);
        } else if (lanes.size() == 2) {
            if (lanes.get(0).size() - lanes.get(1).size() < 0) {
                laneOne.enqueue(customer);
            } else {
                laneTwo.enqueue(customer);
            }
        }
    }

    public void processLane(ArrayQueue<Customer> lane){
        if (lane.isEmpty()) return;
        if (isTransactionComplete(lane)) {
            this.currentCustomer = lane.dequeue();
            this.laneOneTransaction = new TransactionReceipt();
            setTransactionCompletion(lane, false);
        } if (!currentCustomer.getShoppingCart().isEmpty()) {
            Product item = currentCustomer.getShoppingCart().getFirst();
            currentCustomer.getShoppingCart().removeFirst();
            manager.removeProduct(item);
            laneOneTransaction.addProduct(item);
        } else {
            accountant.addToBalance(laneOneTransaction.calculateTotal());
            setTransactionCompletion(lane, true);
        }
    }

    public void setTransactionCompletion(ArrayQueue<Customer> lane, boolean isComplete) {
        if (lane == laneOne) {
            this.isTransactionCompleteL1 = isComplete;
        } else if (lane == laneTwo) {
            this.isTransactionCompleteL2 = isComplete;
        }
    }

    public boolean isTransactionComplete(ArrayQueue<Customer> lane) {
        if (lane == laneOne) {
            return isTransactionCompleteL1;
        } else if (lane == laneTwo) {
            return isTransactionCompleteL2;
        }
        throw new RuntimeException("Illegal checkout lane provided.");
    }

}
