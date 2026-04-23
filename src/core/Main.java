package core;

import engine.*;
import engine.commands.CommandQueue;
import logistics.DeliveryManager;
import logistics.DeliveryTruck;
import models.*;
import org.jline.utils.ShutdownHooks;
import products.MaterialType;
import products.ProductColor;
import products.ProductType;
import ui.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        GameState Scottsville = new GameState();
        InventoryManager manager = new InventoryManager();
        DamagesManager damageControl = new DamagesManager();
        TaskScheduler taskScheduler = new TaskScheduler();
        GameClock clock = new GameClock(Scottsville);
        UIState uiState = new UIState();
        EventBuffer eventBuffer = new EventBuffer();
        CommandQueue queue = new CommandQueue(uiState);
        CommandParser parser = new CommandParser(queue);
        InputListener input = new InputListener(parser);
        ScreenRenderer screenRenderer = new ScreenRenderer(Scottsville, uiState, manager, eventBuffer, damageControl, input);
        DeliveryTruck truck = new DeliveryTruck(manager, damageControl, Scottsville);
        DeliveryManager truckManager = new DeliveryManager(manager, damageControl, Scottsville, clock, truck);
        RevenueManager revenueManager = new RevenueManager(Scottsville);
        CheckoutLane checkoutLane = new CheckoutLane(Scottsville, manager, revenueManager);
        CustomerManager customerManager = new CustomerManager(Scottsville, manager, checkoutLane, eventBuffer,taskScheduler);
        SimConfig simConfig  = new SimConfig(manager);
        DayManager dayManager = new DayManager(clock);
        ConsoleNarrator consoleNarrator = new ConsoleNarrator(Scottsville, manager, damageControl, screenRenderer);

        ConsoleNarrator.bootSequence();

        GameEngine engine = new GameEngine(Scottsville, manager, damageControl, clock);
        engine.registerSystem(truck);
        engine.registerSystem(truckManager);
        engine.registerSystem(queue);
        engine.registerSystem(customerManager);
        engine.registerSystem(checkoutLane);
        engine.registerSystem(dayManager);
        engine.registerSystem(consoleNarrator);
        engine.registerSystem(taskScheduler);

        simConfig.initializeStore();
        Thread commandHandler = new Thread(input);
        commandHandler.start();
        engine.advance();



    }
}
