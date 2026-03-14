package models;

import java.util.ArrayList;
import java.util.List;

public class UnloadManifest {

    private int totalProcessed;
    private int damageCount;
    private int inventoryCount;
    private List<Product> damageLog = new ArrayList<>();

    public UnloadManifest(int totalProcessed, int damageCount, int inventoryCount) {
        this.totalProcessed = totalProcessed;
        this.damageCount = damageCount;
        this.inventoryCount = inventoryCount;
    }

    public UnloadManifest() {}

    public int getTotalProcessed() {
        return totalProcessed;
    }

    public int getDamageCount() {
        return damageCount;
    }

    public int getInventoryCount() {
        return inventoryCount;
    }

    public void setDamageCount(int damageCount) {
        this.damageCount = damageCount;
    }

    public void setInventoryCount(int inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public void setTotalProcessed(int totalProcessed) {
        this.totalProcessed = totalProcessed;
    }

    public void logItemDamaged(Product p) {
        System.out.println("Looks like " + p.getProduct() + " " + p.getType() + " didn't make it in one piece. Adding to damages.");
        damageLog.add(p);
    }

    public String toString() { return "\n\n      [chIKEA Truck]      \n\nProducts added to inventory: " + inventoryCount + ".\nProducts damaged: " + damageCount + "\nTotal: " +  (inventoryCount + damageCount); }
}
