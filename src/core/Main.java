package core;

import engine.*;
import engine.commands.CommandQueue;
import logistics.DeliveryManager;
import logistics.DeliveryTruck;
import models.*;
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
        DeliveryManager truckManager = new DeliveryManager(manager, damageControl, Scottsville, clock, truck);
        RevenueManager revenueManager = new RevenueManager(Scottsville);
        CheckoutLane checkoutLane = new CheckoutLane(Scottsville, manager, revenueManager);
        CustomerManager customerManager = new CustomerManager(Scottsville, manager, checkoutLane);
        CommandQueue queue = new CommandQueue();
        CommandParser parser = new CommandParser(queue);
        InputListener input = new InputListener(parser);
        SimConfig simConfig  = new SimConfig(manager);
        DayManager dayManager = new DayManager(clock);

        System.out.println(ConsoleNarrator.WelcomeMessageLine1);
        System.out.println(ConsoleNarrator.WelcomeMessageLine2);

        GameEngine engine = new GameEngine(Scottsville, manager, damageControl, clock);
        engine.registerSystem(truck);
        engine.registerSystem(truckManager);
        engine.registerSystem(queue);
        engine.registerSystem(customerManager);
        engine.registerSystem(checkoutLane);
        engine.registerSystem(dayManager);

        simConfig.initializeStore();
        Thread commandHandler = new Thread(input);
        commandHandler.start();
        engine.advance();



    }
}
