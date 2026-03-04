package chIKEA;

public class Main {
    static void main(String[] args) {
        System.out.println("Welcome to chIKEA Pre-Alpha (v2.0)");
        InventoryManager manager = new InventoryManager();
        System.out.println("Store Inventory created, creating products.");
        Product Alex_WD = new Product("Alex", "Desk",74.99, "White");
        Product Alex_BD = new Product("Alex", "Desk",64.99, "Black");
        Product Alex_WDR = new Product("Alex", "Dresser",54.99, "White");
        Product Alex_BDR = new Product("Alex", "Dresser",57.99, "Black");
        Product Swedish_Meatballs = new Product("Meatball", "Food",5.99);

        System.out.println("Adding products to inventory");
        manager.addProduct(Alex_WD); manager.addProduct(Alex_BD); manager.addProduct(Alex_WDR); manager.addProduct(Alex_BDR); manager.addProduct(Swedish_Meatballs);

        // Sort Inventory
        manager.sortInventory();

        System.out.println("\n\n----------- chIKEA Store Inventory -----------\n");
        for (Product p : manager.inventory) {
            System.out.println(p.getProduct() + " " + p.getType() + ": $" + p.getPrice());
        }

    }
}
