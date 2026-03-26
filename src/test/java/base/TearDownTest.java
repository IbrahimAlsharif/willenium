package base;

import org.testng.annotations.Test;

public class TearDownTest
{
    @Test
    public void tearDown(){
        if (Setup.driver != null) {
            Setup.driver.quit();
        }
    }
    }
