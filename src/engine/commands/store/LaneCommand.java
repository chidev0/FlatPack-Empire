package engine.commands.store;

import engine.GameState;
import engine.commands.Command;
import engine.commands.CommandContext;
import exceptions.InvalidCommandArgsException;
import jdk.jfr.Event;
import ui.ActiveViewContext;
import ui.EventBuffer;
import ui.UIState;
import core.CheckoutManager;
import ui.ViewMode;

public class LaneCommand implements Command {
    private UIState uiState;
    private GameState gameState;
    private CheckoutManager checkoutManager;
    private EventBuffer eventBuffer;

    public LaneCommand(UIState uiState, GameState state, CheckoutManager checkoutManager, EventBuffer eventBuffer) {
        this.uiState = uiState;
        this.gameState = state;
        this.checkoutManager = checkoutManager;
        this.eventBuffer = eventBuffer;
    }

    @Override
    public String name() {return "lane";}

    @Override
    public String description() {
        return "View information for Checkout Lane";
    }

    @Override
    public String execute(CommandContext context, String[] args) {
        ActiveViewContext lane = new ActiveViewContext(context.getCheckoutLane());
        uiState.setActiveViewContext(lane);
        return "";
    }

    public CommandContext parseArgs(String[] args) throws InvalidCommandArgsException {
        if (args.length != 2) throw new InvalidCommandArgsException("Was expecting one argument (Lane ID), received " + args.length + "arguments");
        eventBuffer.enqueueEvent(String.valueOf(args.length) + args[0] + args[1]);
        // if (args.length != 2) throw new InvalidCommandArgsException("Was expecting one argument (Lane ID), received " + args.length + "arguments");
        if (Integer.parseInt(args[1]) > gameState.getCurrentCheckoutLanes()) throw new InvalidCommandArgsException("Invalid lane ID. Lanes Unlocked: " + gameState.getCurrentCheckoutLanes());
        if (Integer.parseInt(args[1]) <= 0) throw new InvalidCommandArgsException("Invalid lane ID. Must be greater than 0.");
        return new CommandContext(checkoutManager.buildLaneSnapshot(Integer.parseInt(args[1])));
    }
}
