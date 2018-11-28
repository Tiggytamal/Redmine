package control.task;

import javafx.concurrent.Task;

public class CalculStatsTask extends Task<Boolean>
{

    @Override
    protected Boolean call() throws Exception
    {
        return calculerStats();
    }

    private boolean calculerStats()
    {
        return true;
    }

}
