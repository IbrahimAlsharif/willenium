package base;

import org.testng.annotations.Test;

public class TearDownTest
{
    @Test
    public void tearDown(){
        Setup.driver.quit();
    }
    }
