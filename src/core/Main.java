package core;

import customer.CustomerManager;
import engine.*;
import engine.commands.*;
import logistics.DeliveryManager;
import logistics.DeliveryTruck;
import ui.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GameState Scottsville = new GameState();
        SimRandom simRandom = new SimRandom(Scottsville);
        GameClock clock = new GameClock(Scottsville);
        MainMenuRenderer mainMenu = new MainMenuRenderer();
        UIState uiState = new UIState();
        EventBuffer eventBuffer = new EventBuffer();

        RevenueManager revenueManager = new RevenueManager(Scottsville);
        InventoryManager manager = new InventoryManager();
        DamagesManager damageControl = new DamagesManager();
        TaskScheduler taskScheduler = new TaskScheduler();
        DayManager dayManager = new DayManager(clock, Scottsville, simRandom);
        CheckoutManager checkoutManager = new CheckoutManager(Scottsville, manager, revenueManager, eventBuffer, dayManager, simRandom);
        CommandBootstrapper commandBootstrapper = new CommandBootstrapper(uiState, Scottsville, checkoutManager, eventBuffer);
        CommandRegistry registry = commandBootstrapper.createDefaultRegistry();
        CommandDispatcher commandDispatcher = new CommandDispatcher(uiState, registry, eventBuffer);
        CommandQueue queue = new CommandQueue(uiState, commandDispatcher, eventBuffer);
        CommandParser parser = new CommandParser(queue, eventBuffer, registry);
        InputListener input = new InputListener(parser, uiState);
        ScreenRenderer screenRenderer = new ScreenRenderer(Scottsville, uiState, manager, eventBuffer, damageControl, input, checkoutManager, dayManager, taskScheduler);
        GameEngine engine = new GameEngine(Scottsville, manager, damageControl, clock, simRandom);
        ApplicationRuntime runtime = new ApplicationRuntime(engine, queue, screenRenderer, Scottsville, mainMenu, input);
        DeliveryTruck truck = new DeliveryTruck(manager, damageControl, Scottsville, simRandom);
        DeliveryManager truckManager = new DeliveryManager(manager, damageControl, Scottsville, clock, truck, simRandom);
        CustomerManager customerManager = new CustomerManager(Scottsville, manager, checkoutManager, eventBuffer,taskScheduler, dayManager, clock, simRandom);
        SimConfig simConfig  = new SimConfig(manager);
        ConsoleNarrator consoleNarrator = new ConsoleNarrator(Scottsville, manager, damageControl, screenRenderer);

        ConsoleNarrator.bootSequence();

        engine.registerSystem(dayManager);
        engine.registerSystem(truck);
        engine.registerSystem(truckManager);
        engine.registerSystem(customerManager);
        engine.registerSystem(taskScheduler);
        engine.registerSystem(checkoutManager);


        simConfig.initializeStore();
        Thread commandHandler = new Thread(input);
        commandHandler.start();
        runtime.run();



    }
}
