package base;

import org.testng.annotations.Test;

public class TearDownTest
{
    @Test
    public void tearDown(){
        if (Setup.hasActiveUiSession()) {
            Setup.driver.quit();
        }
        Setup.driver = null;
        Setup.wait = null;
    }
    }
