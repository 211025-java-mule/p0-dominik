import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.assertNotNull;


public class ReturnValuesTest {
    private FoodHelper foodHelper;

    @Before
    public void setup() {
        foodHelper = new FoodHelper();
    }

    @Test
    public void getValues() {
        foodHelper.getAPIKeys();
        Food food = foodHelper.fetchDataFromAPI(foodHelper.getX_APP_ID(), foodHelper.getX_APP_KEY(), "1 cup of tea");
        assertNotNull(food);
    }
}