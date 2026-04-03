package engine.commands;

import exceptions.InvalidCommandException;
import models.Product;
import products.ProductCatalog;
import products.ProductType;

public class Command {

    public void execute(String[] command) {
    if (command[0].equals("info")) {
        if (command.length != 3) {
            throw new InvalidCommandException("Invalid Arguments");
        }
        info(command[1], command[2]);
    }
    }

    public void info(String productName, String productType) {
        try {
            Product p = ProductCatalog.productLookup(productName, productType);
            //TODO: Decouple print statement from Command class, output formatted product information, support color lookup.
            System.out.println(p.getProduct() + " " + p.getType() + " - $" + p.getPrice() + ": " + p.getDescription());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
