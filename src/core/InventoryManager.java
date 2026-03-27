package core;

import models.FoodItem;
import models.FurnitureItem;
import models.Product;
import models.TransitManifest;
import products.ProductState;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class InventoryManager {
    // Initializes Store Inventory via ArrayList
    List<Product> inventory = new ArrayList<>();


    // Method for adding a single Product to Store Inventory
    public TransitManifest addProduct(Product p) {
        TransitManifest logger = TransitManifest.createForMovement("INVENTORY_ADD");
        p.setState(ProductState.IN_INVENTORY);
        this.inventory.add(p);
        logger.logProduct(p);
        return logger;
    }

    // Method for adding duplicate Products to Store Inventory
    public TransitManifest addProduct(Product p, int Quantity) {
        TransitManifest logger = TransitManifest.createForMovement("INVENTORY_ADD");
        for (int i = 0; i < Quantity; i++) {
            p = p.copy();
            p.setState(ProductState.IN_INVENTORY);
            this.inventory.add(p);
            logger.logProduct(p);
        }
        return logger;
    }

    // Method for adding multiple Products to Store Inventory
    // TO DO: Stop bulk add from duplicating the same object reference

    public TransitManifest addProducts(Product... items) {
        TransitManifest logger = TransitManifest.createForMovement("INVENTORY_ADD");
        for (Product p : items) {
            p = p.copy();
            p.setState(ProductState.IN_INVENTORY);
            this.inventory.add(p);
            logger.logProduct(p);
        }
        return logger;
    }

    // Method for removing Products.

    public TransitManifest removeProduct(Product p) {
        if (this.inventory.contains(p)) {
            TransitManifest logger = TransitManifest.createForMovement("INVENTORY_REMOVE");
            this.inventory.remove(p);
            p.setState(ProductState.LIMBO);
            logger.logProduct(p);
            return logger;
        }
        throw new RuntimeException("Couldn't find any Product matching the one provided");
    }

    public TransitManifest removeProduct(String Sku) {
        if (Sku.length() < 7) {
            throw new RuntimeException("SKU Invalid: Must be 7 symbols long");
        }
        for (int i = 0; i < this.inventory.size(); i++){
            Product p = this.inventory.get(i);
            String skuString = p.getSku().toString();
            if (skuString.startsWith(Sku)) {
                TransitManifest logger = TransitManifest.createForMovement("INVENTORY_REMOVE");
                this.inventory.remove(p);
                p.setState(ProductState.LIMBO);
                logger.logProduct(p);
                return logger;
            }
        }
    throw new RuntimeException("Couldn't find any Products with that SKU");
    }



    // Method for finding Low Stock Items
    public List<Product> findLowStockItems(int threshold) {
        List<Product> lowInventory = new ArrayList<>();
        for ( Product p : inventory) {
            if (p.getStockLevel() < threshold) {
                lowInventory.add(p);
            }
        }
        return lowInventory;
    }

    // Method for finding items that fall within a price range.
    public List<Product> findProductsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        List<Product> temp_item_range = new ArrayList<>();
        for ( Product p: inventory) {
            // If price is more than the given minPrice and less than given maxPrice, add to List<Product>
            if(p.getPrice().compareTo(minPrice) > 0 && p.getPrice().compareTo(maxPrice) < 0) {
                temp_item_range.add(p);
            }
        }
        return temp_item_range;
    }

    // Method for sorting products in Inventory from lowest to highest price.
    public void sortInventory() {
        for(int i = 0; i < this.inventory.size(); i++) {
            int currentItem = i;
            for (int n = i + 1; n < this.inventory.size(); n++) {
               if (this.inventory.get(currentItem).compareTo(this.inventory.get(n)) > 0) {
                   currentItem = n;
               }
            }
            Product temp = this.inventory.get(i);
            this.inventory.set(i, this.inventory.get(currentItem));
            this.inventory.set(currentItem, temp);
        }
    }

    public List<Product> getInventorySnapshot() {
        List<Product> inventorySnapshot = new ArrayList<>();
        inventorySnapshot.addAll(inventory);
        return inventorySnapshot;
    }
}
