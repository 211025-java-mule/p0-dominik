import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(
        {GetAPIAccessTest.class, ReturnValuesTest.class}
)
public class FoodHelperTestSuite {

}