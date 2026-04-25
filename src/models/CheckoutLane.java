package models;

import core.InventoryManager;
import core.RevenueManager;
import engine.GameState;
import engine.Tickable;
import products.ProductColor;
import products.ProductModel;
import products.ProductState;
import products.ProductType;
import structures.ArrayQueue;
import ui.EventBuffer;

import java.lang.classfile.attribute.CodeAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CheckoutLane {
    InventoryManager manager;
    RevenueManager accountant;
    private GameState state;

    // New lane model
    private boolean awaitingCustomer = true;
    private ArrayQueue<Customer> lane;
    private boolean selfCheckout;
    private boolean isTransactionComplete = false;
    private int cartSize;
    private TransactionReceipt laneTransaction;
    private Customer currentCustomer;
    private Random checkoutRandomizer = new Random();
    private int ticksUntilNextItemProcessed;
    private int laneNumber;
    private Product currentProduct;
    private EventBuffer eventBuffer;
    private boolean currentlyProcessingCustomer;

    public CheckoutLane(InventoryManager manager, RevenueManager accountant, int laneNumber, EventBuffer eventBuffer, GameState state) {
        this.manager = manager;
        this.accountant = accountant;
        this.lane = new ArrayQueue<>();
        this.laneNumber = laneNumber;
        this.eventBuffer = eventBuffer;
        this.state = state;
    }


    public void queueCustomer(Customer customer) {
        lane.enqueue(customer);
    }


    public int getCartSize(Customer customer) {
        return customer.getShoppingCart().size();
    }

    public void setCartSize(int size) {
        cartSize = size;
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
        resetItemProcessingDelay();
        this.currentlyProcessingCustomer = true;
    }

    public void processCheckoutProgress() {
        ticksUntilNextItemProcessed = ticksUntilNextItemProcessed - 1;
        if (ticksUntilNextItemProcessed <= 0) {
            Product item = currentCustomer.getShoppingCart().pop();
            eventBuffer.enqueueEvent("Lane " + getLaneNumber() + " - Processing " + item.getProduct() + " " + item.getType() + "  (" + getItemsProcessed() + "/" + getItemsRemaining() + ")");
            currentProduct = item;
            manager.removeProduct(item);
            laneTransaction.addProduct(item);
            item.setState(ProductState.SOLD);
            if (currentCustomer.getShoppingCart().isEmpty()) {
                isTransactionComplete = true;
                eventBuffer.enqueueEvent("Transaction has completed");
            } else {
                resetItemProcessingDelay();
            }
        }
    }

    public void resetItemProcessingDelay() {
        ticksUntilNextItemProcessed = checkoutRandomizer.nextInt(10, 50);
    }

    public TransactionReceipt getLaneTransaction() {
        return laneTransaction;
    }

    public int getLaneNumber() {
        return laneNumber;
    }

    public Customer getCurrentCustomer() {
        return currentCustomer;
    }

    public int getItemsProcessed() {
        return cartSize - currentCustomer.getShoppingCart().size();
    }

    public int getItemsRemaining() {
        return cartSize;
    }

    public int getTicksUntilNextItemProcessed() {
        return ticksUntilNextItemProcessed;
    }

    public int getLaneSize() {
        return lane.size();
    }

    public boolean isEmpty() {
        return lane.isEmpty();
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public void resetCurrentCustomer() {
        currentCustomer = null;
    }

    public boolean currentlyProcessingCustomer() {
        return currentlyProcessingCustomer;
    }

    public void setCurrentlyProcessingCustomer(boolean currentlyProcessingCustomer) {
        this.currentlyProcessingCustomer = currentlyProcessingCustomer;
    }
}
