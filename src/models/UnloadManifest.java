package models;

import java.util.ArrayList;
import java.util.List;

public class UnloadManifest {

    private int totalProcessed;
    private int inventoryCount;
    private List<Product> damageLog = new ArrayList<>();

    public UnloadManifest(int totalProcessed, int inventoryCount) {
        this.totalProcessed = totalProcessed;
        this.inventoryCount = inventoryCount;
    }

    public UnloadManifest() {}

    public int getTotalProcessed() {
        return totalProcessed;
    }

    public int getDamageCount() {
        return damageLog.size();
    }

    public int getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(int inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public void setTotalProcessed(int totalProcessed) {
        this.totalProcessed = totalProcessed;
    }

    public void logItemDamaged(Product p) {
        damageLog.add(p);
    }

    public List<Product> getDamageLog() {
        return damageLog;
    }

}
