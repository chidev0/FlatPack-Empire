package core;

import engine.GameState;
import logistics.DeliveryTruck;
import models.FoodItem;
import models.FurnitureItem;
import models.Product;
import models.UnloadManifest;
import products.MaterialType;
import products.ProductColor;
import products.ProductType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GameState Scottsville = new GameState();
        System.out.println("Welcome to chIKEA Pre-Alpha (v3.5)");
        InventoryManager manager = new InventoryManager();
        DamagesManager damageControl = new DamagesManager();
        DeliveryTruck Truck = new DeliveryTruck(manager, damageControl, Scottsville);
        System.out.println("Store Inventory created, creating products.");
        FurnitureItem Alex_WD = new FurnitureItem("Alex", ProductType.DESK, BigDecimal.valueOf(74.99), ProductColor.WHITE, "The pinnicle of 3000 years of advanced wood carving techniques.", MaterialType.PARTICLE_BOARD);
        FurnitureItem Alex_BD = new FurnitureItem("Alex", ProductType.DESK,BigDecimal.valueOf(64.99), ProductColor.BLACK, "The pinnicle of 3000 years of advanced wood carving techniques.", MaterialType.PARTICLE_BOARD);
        FurnitureItem Alex_WDR = new FurnitureItem("Alex", ProductType.DRESSER,BigDecimal.valueOf(54.99), ProductColor.WHITE, "The pinnicle of 3000 years of advanced wood carving techniques.", MaterialType.PARTICLE_BOARD);
        FurnitureItem Alex_BDR = new FurnitureItem("Alex", ProductType.DRESSER,BigDecimal.valueOf(57.99), ProductColor.BLACK, "The pinnicle of 3000 years of advanced wood carving techniques.", MaterialType.PARTICLE_BOARD);
        FoodItem Swedish_Meatballs = new FoodItem("Meatball", ProductType.HOT_FOOD, BigDecimal.valueOf(5.99), "Straight from the motherland",false, 30, 4, "Chicken");

        System.out.println("Adding products to inventory");
        manager.addProducts(Alex_WD, Alex_BD, Alex_WDR, Alex_BDR, Swedish_Meatballs);
        Truck.loadTruck(Swedish_Meatballs, Swedish_Meatballs, Swedish_Meatballs, Swedish_Meatballs, Alex_WD, Alex_WDR, Alex_BDR);
        // Sort Inventory
        manager.sortInventory();

        System.out.println("\n\n----------- chIKEA Store Inventory -----------\n");
        for (Product p : manager.getInventorySnapshot()) {
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
        System.out.println("[chIKEA Truck] Received a Truck at the dock, attempting unload now\n");
        try {
            UnloadManifest truckManifest = Truck.advance();
            if (!truckManifest.getDamageLog().isEmpty()) {
                for (Product p : truckManifest.getDamageLog()) {
                    System.out.println("Looks like " + p.getProduct() + " " + p.getType() + " didn't make it in one piece. Added to damages.");
                }
            }
            System.out.println("\n\n      [chIKEA Truck]      \n\nProducts added to inventory: " + truckManifest.getInventoryCount() + ".\nProducts damaged: " + truckManifest.getDamageCount() + "\nTotal: " +  truckManifest.getTotalProcessed());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } finally {
            manager.sortInventory();
            List<Product> inventory = manager.getInventorySnapshot();
            System.out.println("\n----------- chIKEA Store Inventory -----------\n");
            for (Product i : inventory) {
                System.out.println(i.toString());
            }
        }

    }
}
