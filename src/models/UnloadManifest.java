package models;

public class UnloadManifest {

    private int totalProcessed;
    private int damageCount;
    private int inventoryCount;

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

    public String toString() { return "\n\n      [chIKEA Truck]      \n\nProducts added to inventory: " + inventoryCount + ".\nProducts damaged: " + damageCount + "\nTotal: " +  (inventoryCount + damageCount); }
}
