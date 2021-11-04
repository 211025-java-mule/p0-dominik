import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/* TODOs
figure out how to get certain items from API
connect to API
fetch data from API
output data
include server
add logging
add flags
 */
public class FoodHelper {
    private String API_KEY = "";
    public static void main(String[] args) {
        FoodHelper foodHelper = new FoodHelper();
        foodHelper.getAPIKey();
    }

    private void getAPIKey() {
        ClassLoader classLoader = FoodHelper.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("apikey.txt");
        API_KEY = readFromFile(inputStream);
        System.out.println(API_KEY);
    }

    private static String readFromFile(InputStream filename) {
        StringBuilder resultStringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(filename));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultStringBuilder.toString();
    }

    public void setAPI_KEY(String API_KEY) {
        this.API_KEY = API_KEY;
    }
}
