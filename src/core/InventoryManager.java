package core;

import models.FoodItem;
import models.FurnitureItem;
import models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InventoryManager {
    // Initializes Store Inventory via ArrayList
    List<Product> inventory = new ArrayList<>();


    // Method for adding a single Product to Store Inventory
    public void addProduct(Product p) {
        this.inventory.add(p);
        p.setState("IN_INVENTORY");
        System.out.println("chIKEA Inventory: Successfully added a " + p.getProduct() + " " + p.getType() + " to the database.");
    }

    // Method for adding duplicate Products to Store Inventory
    public void addProduct(Product p, int Quantity) {
        for (int i = 0; i < Quantity; i++) {
            p = rebuildProduct(p, p.getProductModel());
            p.setState("IN_INVENTORY");
            this.inventory.add(p);
        }
        System.out.println("chIKEA Inventory: Successfully added " + Quantity + " " +p.getProduct() + " " + p.getType() +"'s to the database.");
    }

    // Method for adding multiple Products to Store Inventory
    // TO DO: Stop bulk add from duplicating the same object reference

    public void addProducts(Product... items) {
        for (Product p : items) {
            p = rebuildProduct(p, p.getProductModel());
            p.setState("IN_INVENTORY");
            this.inventory.add(p);
            String skuString = p.getSku().toString();
            skuString = skuString.substring(0,7);
            System.out.println("chIKEA Inventory: Added [" + skuString + "] "  + p.getProduct() + " " + p.getType() +" to the database.");
        }
    }

    // Method for removing Products.

    public void removeProduct(Product p) {
        if (this.inventory.contains(p)) {
            this.inventory.remove(p);
            p.setState("LIMBO");
            System.out.println("[ chIKEA Inventory ] ~ Removed a " + p.getProduct() + " " + p.getType() + "from the database.");
            return;
        }
        throw new RuntimeException("Couldn't find any Product matching the one provided");
    }

    public void removeProduct(String Sku) {
        if (Sku.length() < 7) {
            throw new RuntimeException("SKU Invalid: Must be 7 symbols long");
        }
        for (int i = 0; i < this.inventory.size(); i++){
            Product p = this.inventory.get(i);
            String skuString = p.getSku().toString();
            if (skuString.startsWith(Sku)) {
                this.inventory.remove(p);
                p.setState("LIMBO");
                System.out.println("[ chIKEA Inventory ] ~ Removed [" + Sku + "] " + p.getProduct() + " " + p.getType() + " from the database." );
                return;
            }
        }
    throw new RuntimeException("Couldn't find any Products with that SKU");
    }

    public FurnitureItem rebuildFurniture(FurnitureItem p) {
            FurnitureItem tempProduct = new FurnitureItem(p.getProduct(), p.getType(), p.getPrice(), p.getColor());
            return tempProduct;
        }


    public FoodItem rebuildFood(FoodItem p) {

            FoodItem tempProduct = new FoodItem(p.getProduct(), p.getType(), p.getPrice());
            if (p.hasProtein()) {
                int proteinAmount = p.getProteinAmount();
                tempProduct.setProtein(proteinAmount);
            } if (p.veganStatus()) {
                tempProduct.setVegan(true);
            } if (p.hasBase()) {
                tempProduct.setBase(p.getBase());
            }
        return tempProduct;

    }
    
    public Product rebuildProduct(Object p, String model) {
        if (model.equals("Furniture")) {
            return rebuildFurniture((FurnitureItem) p);
        } else if (model.equals("Food")) {
            return rebuildFood((FoodItem) p);
        }
        throw new RuntimeException("Illegal Model Type");
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
        System.out.println("chIKEA Inventory: All items have been successfully sorted (low-high)");
    }
}
