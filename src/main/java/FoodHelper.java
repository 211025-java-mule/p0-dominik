import sun.misc.IOUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/* TODOs
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
        foodHelper.connectToAPI();
    }

    private void connectToAPI() {
        URL url = null;
        try {
            url = new URL("https://api.nal.usda.gov/fdc/v1/foods/list?api_key=" + API_KEY);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream response = null;
        try {
            response = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String body = null;
        try {
            byte[] bytes = new byte[(int) response.available()];
            DataInputStream dataInputStream = new DataInputStream(response);
            dataInputStream.readFully(bytes);
            body = new String(bytes, StandardCharsets.UTF_8);
            System.out.println(body.length());
            System.out.println(body);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getAPIKey() {
        ClassLoader classLoader = FoodHelper.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("apikey.txt");
        API_KEY = readFromFile(inputStream);
        //System.out.println(API_KEY);
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

    public void getFoodByName(String name) {

    }
}
