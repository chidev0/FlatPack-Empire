package engine.commands;

import engine.GameClock;
import exceptions.InvalidCommandException;
import models.Product;
import products.ProductCatalog;
import ui.InputListener;

public class CommandDispatcher {

    public void execute(String[] command) {
    if (command[0].equals("info")) {
        if (command.length != 3) {
            throw new InvalidCommandException("Invalid Arguments");
        }
        try {
            System.out.println("Running info");
            info(command[1], command[2]);
        } catch (InvalidCommandException e) {
            System.out.print("Incorrect Usage - info [ProductName] [ProductType]");
        }
        InputListener.setProcessingCommand(false);
    } else if (command[0].equals("time")) {
        System.out.println(time());
    }
    }

    public void info(String productName, String productType) {
        try {
            Product p = ProductCatalog.productLookup(productName, productType);
            //TODO: Decouple print statement from Command class, output formatted product information, support color lookup.
            System.out.println(p.getProduct() + " " + p.getType() + " - $" + p.getPrice() + ": " + p.getDescription());
        } catch (InvalidCommandException e) {
            throw new InvalidCommandException("Missing Arguments");
        }
    }

    public String time() {
        return GameClock.getCurrentTime();
    }
}
