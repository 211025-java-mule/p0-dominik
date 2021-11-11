import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertNotNull;


public class GetAPIAccessTest {
    private FoodHelper foodHelper;

    @Before
    public void setup() {
        foodHelper = new FoodHelper();
    }

    @Test
    public void getValues() {
        foodHelper.getAPIKeys();
        assertNotNull(foodHelper.getX_APP_ID());
        assertNotNull(foodHelper.getX_APP_KEY());
    }
}