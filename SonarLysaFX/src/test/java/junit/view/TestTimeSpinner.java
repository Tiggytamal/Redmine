package junit.view;

import java.time.LocalTime;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.saxsys.javafx.test.JfxRunner;
import view.TimeSpinner;

@RunWith(JfxRunner.class)
public class TestTimeSpinner
{
    private TimeSpinner ts;
    
    @Before
    public void init()
    {
        ts = new TimeSpinner();
    }
    @Test
    public void timmeSpinner()
    {
        ts = new TimeSpinner(LocalTime.of(12, 12));
    }
    
    @Test
    public void getMode()
    {
        ts.getMode();
    }
}
