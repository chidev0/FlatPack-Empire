package chIKEA;

public class Main {
    static void main(String[] args) {
        System.out.println("Welcome to chIKEA Pre-Alpha (v2.0)");
        InventoryManager manager = new InventoryManager();
        System.out.println("Store Inventory created, creating products.");
        FurnitureItem Alex_WD = new FurnitureItem("Alex", "Desk",74.99, "White");
        FurnitureItem Alex_BD = new FurnitureItem("Alex", "Desk",64.99, "Black");
        FurnitureItem Alex_WDR = new FurnitureItem("Alex", "Dresser",54.99, "White");
        FurnitureItem Alex_BDR = new FurnitureItem("Alex", "Dresser",57.99, "Black");
        FoodItem Swedish_Meatballs = new FoodItem("Meatball", "Food", 5.99, false, 30, 4, "Chicken");

        System.out.println("Adding products to inventory");
        manager.addProducts(Alex_WD, Alex_BD, Alex_WDR, Alex_BDR, Swedish_Meatballs);
        // Sort Inventory
        manager.sortInventory();

        System.out.println("\n\n----------- chIKEA Store Inventory -----------\n");
        for (Product p : manager.inventory) {
            System.out.println(p.toString());
        }

    }
}
