package core;

import engine.GameClock;
import engine.GameEngine;
import engine.GameState;
import logistics.DeliveryManager;
import logistics.DeliveryTruck;
import models.FoodItem;
import models.FurnitureItem;
import models.Product;
import models.UnloadManifest;
import products.MaterialType;
import products.ProductColor;
import products.ProductType;
import ui.CommandParser;
import ui.ConsoleNarrator;
import ui.InputListener;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        GameState Scottsville = new GameState();
        InventoryManager manager = new InventoryManager();
        DamagesManager damageControl = new DamagesManager();
        GameClock clock = new GameClock(Scottsville);
        DeliveryTruck truck = new DeliveryTruck(manager, damageControl, Scottsville);
        DeliveryManager truckManager = new DeliveryManager(manager, damageControl, Scottsville, clock);
        CommandParser parser = new CommandParser();
        InputListener input = new InputListener(parser);

        System.out.println(ConsoleNarrator.WelcomeMessageLine1);
        System.out.println(ConsoleNarrator.WelcomeMessageLine2);

        GameEngine engine = new GameEngine(Scottsville, manager, damageControl);
        engine.registerSystem(truck);
        engine.registerSystem(truckManager);
        // TODO: engine.registerSystem(customerManager)
        // TODO: engine.registerSystem(checkout)
        // TODO: engine.registerSystem(revenueSystem)

        engine.advance();

        while (true) {
            if (!InputListener.isWaitingForResponse()) {
                input.nextInput();
            }
        }

    }
}
