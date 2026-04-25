package engine.commands.store;

import engine.commands.Command;
import engine.commands.CommandContext;
import exceptions.InvalidCommandArgsException;
import models.Product;
import products.ProductCatalog;
import ui.ActiveViewContext;
import ui.UIState;
import ui.ViewMode;

public class InfoCommand implements Command {
    private UIState uiState;

    public InfoCommand(UIState uiState) {
        this.uiState = uiState;
    }

    @Override
    public String name() {
        return "info";
    }

    @Override
    public String description() {
        return "";
    }

    @Override
    public String execute(CommandContext context, String[] args) {
        ActiveViewContext info = new ActiveViewContext(context.getSKU(), ViewMode.INVENTORY_VIEW);
        uiState.setActiveViewContext(info);
        return "";
    }

    @Override
    public CommandContext parseArgs(String[] args) throws InvalidCommandArgsException {
        if (args.length != 3) throw new InvalidCommandArgsException("Requires 2 Args");
        Product p = ProductCatalog.productLookup(args[1], args[2]);
        //TODO: Decouple print statement from Command class, output formatted product information, support color lookup.
        return new CommandContext(p.getSku());
    }
}
