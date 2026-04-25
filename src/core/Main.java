package core;

import engine.*;
import engine.commands.*;
import logistics.DeliveryManager;
import logistics.DeliveryTruck;
import ui.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GameState Scottsville = new GameState();
        RevenueManager revenueManager = new RevenueManager(Scottsville);
        InventoryManager manager = new InventoryManager();
        DamagesManager damageControl = new DamagesManager();
        TaskScheduler taskScheduler = new TaskScheduler();
        GameClock clock = new GameClock(Scottsville);
        EventBuffer eventBuffer = new EventBuffer();
        CheckoutManager checkoutManager = new CheckoutManager(Scottsville, manager, revenueManager, eventBuffer);
        UIState uiState = new UIState();
        CommandBootstrapper commandBootstrapper = new CommandBootstrapper(uiState);
        CommandRegistry registry = commandBootstrapper.createDefaultRegistry();
        CommandDispatcher commandDispatcher = new CommandDispatcher(uiState, registry, eventBuffer);
        CommandQueue queue = new CommandQueue(uiState, commandDispatcher, eventBuffer);
        CommandParser parser = new CommandParser(queue, eventBuffer, registry);
        InputListener input = new InputListener(parser);
        ScreenRenderer screenRenderer = new ScreenRenderer(Scottsville, uiState, manager, eventBuffer, damageControl, input, checkoutManager);
        DeliveryTruck truck = new DeliveryTruck(manager, damageControl, Scottsville);
        DeliveryManager truckManager = new DeliveryManager(manager, damageControl, Scottsville, clock, truck);
        CustomerManager customerManager = new CustomerManager(Scottsville, manager, checkoutManager, eventBuffer,taskScheduler);
        SimConfig simConfig  = new SimConfig(manager);
        DayManager dayManager = new DayManager(clock);
        ConsoleNarrator consoleNarrator = new ConsoleNarrator(Scottsville, manager, damageControl, screenRenderer);

        ConsoleNarrator.bootSequence();

        GameEngine engine = new GameEngine(Scottsville, manager, damageControl, clock);
        engine.registerSystem(truck);
        engine.registerSystem(truckManager);
        engine.registerSystem(queue);
        engine.registerSystem(customerManager);
        engine.registerSystem(dayManager);
        engine.registerSystem(consoleNarrator);
        engine.registerSystem(taskScheduler);
        engine.registerSystem(checkoutManager);

        simConfig.initializeStore();
        Thread commandHandler = new Thread(input);
        commandHandler.start();
        engine.advance();



    }
}
