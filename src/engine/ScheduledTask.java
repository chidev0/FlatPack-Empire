package engine;

public class ScheduledTask {
    private int ticksUntilExecute;
    private final Runnable action;
    private boolean completed = false;

    public ScheduledTask(int ticksUntilExecute, Runnable action) {
        this.ticksUntilExecute = ticksUntilExecute;
        this.action = action;
    }

    public void updateTicksUntilExecute() {
        if (ticksUntilExecute == 0) return;
        ticksUntilExecute--;
    }

    public int getTicksUntilExecute()  {
        return ticksUntilExecute;
    }

    public void execute() {
        action.run();
        completed = true;
    }

    public boolean getStatus() {
        return completed;
    }

}
