package models;

import core.InventoryManager;
import core.RevenueManager;
import engine.GameState;
import engine.Tickable;
import products.ProductModel;
import products.ProductState;
import structures.ArrayQueue;

import java.lang.classfile.attribute.CodeAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CheckoutLane implements Tickable {
    private List<ArrayQueue<Customer>> lanes = new ArrayList<>();
    ArrayQueue<Customer> laneOne;
    ArrayQueue<Customer> laneTwo;
    Customer currentCustomerL1;
    Customer currentCustomerL2;
    int cartSizeL1;
    int cartSizeL2;
    InventoryManager manager;
    RevenueManager accountant;
    TransactionReceipt laneOneTransaction;
    TransactionReceipt laneTwoTransaction;
    private boolean isTransactionCompleteL1 = true;
    private boolean isTransactionCompleteL2 = true;

    // New lane model
    private boolean awaitingCustomer;
    private ArrayQueue<Customer> lane;
    private boolean selfCheckout;
    private boolean isTransactionComplete = false;
    private int cartSize;
    private TransactionReceipt laneTransaction;
    private int ticksUntilNextItemProcessed;
    private Customer currentCustomer;
    private Random checkoutRandomizer = new Random();
    private int laneNumber;

    public CheckoutLane(GameState state, InventoryManager manager, RevenueManager accountant, int laneNumber) {
        this.manager = manager;
        this.accountant = accountant;
        int CASHIER_LANES = state.getCurrentCheckoutLanes();
        this.laneOne = new ArrayQueue<>();
        this.lane = new ArrayQueue<>();
        lanes.add(laneOne);
        this.laneNumber = laneNumber;
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

    public void queueCustomerN(Customer customer) {
        lane.enqueue(customer);
    }

    // Logic for tick-based checkout lane processing
    // Current limitations: Not built to scale, only support two lanes for now.
    public void processLane(ArrayQueue<Customer> lane) {
        if (lane.isEmpty()) return;
        if (isTransactionComplete(lane)) {
            if (lane == laneOne) {
                this.currentCustomerL1 = lane.dequeue();
                setCartSize(currentCustomerL1, currentCustomerL1.getShoppingCart().size());
                this.laneOneTransaction = new TransactionReceipt();
            } else if (lane == laneTwo) {
                this.currentCustomerL2 = lane.dequeue();
                setCartSize(currentCustomerL2, currentCustomerL2.getShoppingCart().size());
                this.laneTwoTransaction = new TransactionReceipt();
            }
            setTransactionCompletion(lane, false);
        }
        if (lane == laneOne) {
            if (!currentCustomerL1.getShoppingCart().isEmpty()) {
                Product item = currentCustomerL1.getShoppingCart().pop();
                System.out.println("[chIKEA Checkout] Customer in Progress - Lane 1");
                System.out.print("Progress: " + (cartSizeL1 - currentCustomerL1.getShoppingCart().size()) + "/" + cartSizeL1 + " items - ");
                System.out.print(item.getProduct() + " " + item.getType().getUiLabel() + " ");
                if (item.getProductModel() == ProductModel.FURNITURE) System.out.print(item.getColor());
                System.out.println("\nCustomers in queue: " + lane.size());
                manager.removeProduct(item);
                laneOneTransaction.addProduct(item);

            } else if (currentCustomerL1.getShoppingCart().isEmpty()){
                System.out.println("[chIKEA Checkout] Customer Processed. Transaction total: $" + laneOneTransaction.calculateTotal());
                accountant.addToBalance(laneOneTransaction.calculateTotal());
                setTransactionCompletion(lane, true);
            }
        } else if (lane == laneTwo) {
            if (!currentCustomerL2.getShoppingCart().isEmpty()) {
                Product item = currentCustomerL2.getShoppingCart().pop();
                System.out.println("[chIKEA Checkout] Customer in Progress - Lane " + lane);
                System.out.println("Progress: " + (cartSizeL2 - currentCustomerL2.getShoppingCart().size()) + "/" + cartSizeL2 + " items");
                manager.removeProduct(item);
                laneTwoTransaction.addProduct(item);
                item.setState(ProductState.SOLD);
            } else if (currentCustomerL2.getShoppingCart().isEmpty()){
                accountant.addToBalance(laneTwoTransaction.calculateTotal());
                setTransactionCompletion(lane, true);
            }
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

    public int getCartSize(Customer customer) {
        return customer.getShoppingCart().size();
    }

    public void setCartSize(Customer customer, int size) {
        if (customer == currentCustomerL1) {
            cartSizeL1 = size;
        } else if (customer == currentCustomerL2) {
            cartSizeL2 = size;
        }
    }

    //New methods

    public boolean isAwaitingCustomer() {
        return awaitingCustomer;
    }

    public void setCustomerAwaitStatus(boolean awaitingCustomer) {
        this.awaitingCustomer = awaitingCustomer;
    }

    public boolean isTransactionComplete() {
        return isTransactionComplete;
    }

    public void setTransactionComplete(boolean isTransactionComplete) {
        this.isTransactionComplete = isTransactionComplete;
    }

    public void releaseCustomer() {
        currentCustomer = lane.dequeue();
        cartSize = currentCustomer.getShoppingCart().size();
        this.laneTransaction = new TransactionReceipt();
        isTransactionComplete = false;
    }

    public void processCheckoutProgress() {
        ticksUntilNextItemProcessed--;
        if (ticksUntilNextItemProcessed == 0) {
            Product item = currentCustomer.getShoppingCart().pop();
            manager.removeProduct(item);
            laneTransaction.addProduct(item);
            item.setState(ProductState.SOLD);
            if (currentCustomer.getShoppingCart().isEmpty()) {
                isTransactionComplete = true;
            } else {
                resetItemProcessingDelay();
            }
        }
    }

    public void resetItemProcessingDelay() {
        ticksUntilNextItemProcessed = checkoutRandomizer.nextInt(10,50);
    }

    public TransactionReceipt getLaneTransaction() {
        return laneTransaction;
    }

    public int getLaneNumber() {
        return laneNumber;
    }

    public void tick(GameState state) {
        processLane(laneOne);
        if (state.getCurrentCheckoutLanes() == 2) {
            processLane(laneTwo);
        }
    }
}
