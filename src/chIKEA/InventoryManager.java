package chIKEA;

public class InventoryManager {
    Product[] inventory = new Product[15];
    public void addProduct(Product p) {
        this.inventory.add(p);
    }
}
