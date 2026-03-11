package core;

import logistics.DeliveryTruck;
import models.FoodItem;
import models.FurnitureItem;
import models.Product;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to chIKEA Pre-Alpha (v2.5)");
        InventoryManager manager = new InventoryManager();
        DamagesManager damageControl = new DamagesManager();
        DeliveryTruck Truck = new DeliveryTruck(manager, damageControl);
        System.out.println("Store Inventory created, creating products.");
        FurnitureItem Alex_WD = new FurnitureItem("Alex", "Desk",74.99, "White");
        FurnitureItem Alex_BD = new FurnitureItem("Alex", "Desk",64.99, "Black");
        FurnitureItem Alex_WDR = new FurnitureItem("Alex", "Dresser",54.99, "White");
        FurnitureItem Alex_BDR = new FurnitureItem("Alex", "Dresser",57.99, "Black");
        FoodItem Swedish_Meatballs = new FoodItem("Meatball", "Food", 5.99, false, 30, 4, "Chicken");

        System.out.println("Adding products to inventory");
        manager.addProducts(Alex_WD, Alex_BD, Alex_WDR, Alex_BDR, Swedish_Meatballs);
        Truck.loadTruck(Swedish_Meatballs, Swedish_Meatballs, Swedish_Meatballs, Swedish_Meatballs, Alex_WD, Alex_WDR, Alex_BDR);
        // Sort Inventory
        manager.sortInventory();

        System.out.println("\n\n----------- chIKEA Store Inventory -----------\n");
        for (Product p : manager.inventory) {
            System.out.println(p.toString());
        }
        System.out.println("\n\n");

        boolean verifyRemoval = false;

        while (!verifyRemoval) {
            try {
                Scanner input = new Scanner(System.in);
                System.out.println("Enter a SKU to remove: ");
                String SKU = input.nextLine();
                manager.removeProduct(SKU);
                verifyRemoval = true;
            } catch (RuntimeException e) {
                System.out.println("Ran into an error trying to remove the SKU. Please try again.");
                System.out.println(e.toString() + "\n");
            }
        }

        // v2.5 Testing
        System.out.println("[chIKEA Truck] Received a Truck at the dock, attempting unload now\n\n");
        try {
            System.out.println(Truck.unloadTruck());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } finally {
            manager.sortInventory();
            manager.displayInventory();
        }

    }
}
