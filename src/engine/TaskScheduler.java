package engine;

import java.util.ArrayList;
import java.util.List;

public class TaskScheduler implements Tickable {
    private List<ScheduledTask> scheduledTasks = new ArrayList<>();

    public ScheduledTask scheduleTask(int ticksUntilExecute, Runnable action) {
        ScheduledTask newTask = new ScheduledTask(ticksUntilExecute, action);
        scheduledTasks.add(newTask);
        return newTask;
    }

    public void tick(GameState state) {
        for (int i = scheduledTasks.size() - 1; i>= 0; i--) {
            ScheduledTask task = scheduledTasks.get(i);
            task.updateTicksUntilExecute();
            if (task.getTicksUntilExecute() == 0) {
                task.execute();
                scheduledTasks.remove(task);
            }

        }
    }




}
