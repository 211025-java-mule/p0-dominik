import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.*;

public class FoodHelper {
    private static Logger logger;
    private String X_APP_ID;
    private String X_APP_KEY;
    private final ObjectMapper mapper;

    public FoodHelper() {
        logger = LogManager.getLogger();
        X_APP_ID = "";
        X_APP_KEY = "";
        mapper = new ObjectMapper();
    }

    public static void main(String[] args) {
        FoodHelper foodHelper = new FoodHelper();
        foodHelper.getAPIKeys();
        while (true) {
            System.out.println("What food would you like to check nutrition values for?");
            foodHelper.fetchDataFromAPI(foodHelper.X_APP_ID, foodHelper.X_APP_KEY, null);
        }
    }

    public void getAPIKeys() {
        ClassLoader classLoader = FoodHelper.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("app_id.txt");
        X_APP_ID = readFromFile(inputStream);
        inputStream = classLoader.getResourceAsStream("app_key.txt");
        X_APP_KEY = readFromFile(inputStream);
        logger.debug("Gotten keys");
    }

    public static String readFromFile(InputStream filename) {
        StringBuilder resultStringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(filename));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        } catch (IOException | NullPointerException e) {
            logger.error("Cannot read f!\n");
        }
        return resultStringBuilder.toString();
    }

    public Food fetchDataFromAPI(String app_id, String app_key, String query) {
        if (query == null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                query = reader.readLine();
            } catch (IOException e) {
                logger.error("Cannot read query from command line!\n");
            }
        }
        String command = "curl -X POST \"https://trackapi.nutritionix.com/v2/natural/nutrients\" -H  " +
                "\"accept: application/json\" -H  \"x-app-id: " + app_id + "\" -H  " +
                "\"x-app-key:" + app_key + "\" -H  \"x-remote-user-id: 0\" -H  " +
                "\"Content-Type: application/json\" -d \"{  \\\"query\\\": \\\"" + query + "\\\"}";
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            logger.error("Cannot execute query!\n");
        }
        BufferedReader response = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String body = null;
        try {
            body = response.readLine();
        } catch (IOException e) {
            logger.error("Cannot read response!\n");
        }
        System.out.println(body);
        try {
            int exitValue = process.waitFor();
        } catch (InterruptedException e) {
            logger.error("Cannot exit response reader!\n");
        }
        Food output = new Food();
        try {
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.setVisibility(VisibilityChecker.Std.defaultInstance()
                    .withFieldVisibility(JsonAutoDetect.Visibility.ANY));
            output = mapper.readValue(body, Food.class);
        } catch (JsonProcessingException e) {
            logger.error("Cannot read response into Food Object!\n");
        }
        FoodElement[] foods = output.getFoods();
        try {
            for (int i = 0; i < foods.length; i++) {
                System.out.println(foods[i].toString());
            }
        } catch (NullPointerException e) {
            logger.error("Couldn't get a food from name provided!");
        }
        return output;
    }

    public void connectToDatabase(FoodElement foodElement) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/food",
                    "dominik", "123");
            PreparedStatement preparedStatement = connection.prepareStatement("insert into " +
                    "food(name, weightGrams, calories, fat, saturatedFat, cholesterol, sodium, carbohydrate," +
                    "dietaryFiber, sugars, protein, potassium) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, foodElement.getFood_name());
            preparedStatement.setLong(2, foodElement.getServing_weight_grams());
            preparedStatement.setLong(3, foodElement.getNf_calories());
            preparedStatement.setLong(4, foodElement.getNf_total_fat());
            preparedStatement.setLong(5, foodElement.getNf_saturated_fat());
            preparedStatement.setLong(6, foodElement.getNf_cholesterol());
            preparedStatement.setLong(7, foodElement.getNf_sodium());
            preparedStatement.setLong(8, foodElement.getNf_total_carbohydrate());
            preparedStatement.setLong(9, foodElement.getNf_dietary_fiber());
            preparedStatement.setLong(10, foodElement.getNf_sugars());
            preparedStatement.setLong(11, foodElement.getNf_protein());
            preparedStatement.setObject(12, foodElement.getNf_potassium());
        } catch (SQLException e) {
            logger.debug("Cannot connect to database");
        }
    }

    public String getX_APP_ID() {
        return X_APP_ID;
    }

    public String getX_APP_KEY() {
        return X_APP_KEY;
    }
}
