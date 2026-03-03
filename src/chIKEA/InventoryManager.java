package chIKEA;

import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    // Initializes Store Inventory via ArrayList
    List<Product> inventory = new ArrayList<>();

    // Method for adding a single Product to Store Inventory
    public void addProduct(Product p) {
        this.inventory.add(p);
        System.out.println("chIKEA Inventory: Successfully added a " + p.getProduct() + " " + p.getType() + " to the database.");
    }

    // Method for adding multiple Products to Store Inventory
    public void addProduct(Product p, int Quantity) {
        for (int i = 0; i < Quantity; i++) {
            this.inventory.add(p);
        }
        System.out.println("chIKEA Inventory: Successfully added " + Quantity + p.getProduct() + p.getType() +"'s to the database.");
    }

    // Method for finding Low Stock Items
    public void findLowStockItems(int threshold) {
        for ( Product p : inventory) {
            if (p.getStockLevel() < threshold) {
                System.out.print("chIKEA Inventory Watchdog: " + p.getProduct() + " " + p.getType() + "is below the set threshold. ");
                System.out.println("Available Inventory: " + p.getStockLevel());
            }
        }
    }

    // Method for finding items that fall within a price range.
    public List<Product> findProductsInPriceRange(double minPrice, double maxPrice) {
        List<Product> temp_item_range = new ArrayList<>();
        for ( Product p: inventory) {
            if(p.getPrice() >= minPrice && p.getPrice() <= maxPrice) {
                temp_item_range.add(p);
            }
        }
        return temp_item_range;
    }
}
