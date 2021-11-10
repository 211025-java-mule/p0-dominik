import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;

/* TODOs
connect to API
fetch data from API
output data
include server
add logging
add flags
 */
public class FoodHelper {
    private String X_APP_ID = "";
    private String X_APP_KEY = "";
    private ObjectMapper mapper = new ObjectMapper();
    public static void main(String[] args) {
        FoodHelper foodHelper = new FoodHelper();
        foodHelper.getAPIKeys();
        foodHelper.connectToAPI();
    }

    private void connectToAPI() {
        /*BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String query = null;
        try {
            query = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        String command = "curl -X POST \"https://trackapi.nutritionix.com/v2/natural/nutrients\" -H  \"accept: application/json\" -H  \"x-app-id: 8d1bd058\" -H  \"x-app-key: 4b105532dbaf1546f254494b4dcf6a23\" -H  \"x-remote-user-id: 0\" -H  \"Content-Type: application/json\" -d \"{  \\\"query\\\": \\\"1 big mac\\\"}\"";
                /*"curl -X POST \"https://trackapi.nutritionix.com/v2/natural/nutrients\" -H  " +
                "\"accept: application/json\" -H  \"x-app-id: " + X_APP_ID + "\" -H  " +
                "\"x-app-key:" + X_APP_KEY + "\" -H  \"x-remote-user-id: 0\" -H  " +
                "\"Content-Type: application/json\" -d \"{  \\\"query\\\": \\\"" + query + "\\\"}";*/
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader response = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String body = null;
        while(true) {
            try {
                if (!((body = response.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(body);
        }
        try {
            int exitValue = process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void getAPIKeys() {
        ClassLoader classLoader = FoodHelper.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("app_key.txt");
        X_APP_KEY = readFromFile(inputStream);
        inputStream = classLoader.getResourceAsStream("app_id.txt");
        X_APP_ID = readFromFile(inputStream);
//        System.out.println(X_APP_KEY);
//        System.out.println(X_APP_ID);
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
}
