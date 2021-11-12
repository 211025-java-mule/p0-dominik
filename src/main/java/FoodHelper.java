import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class FoodHelper {
    private static Logger logger;
    private String X_APP_ID;
    private String X_APP_KEY;
    private final ObjectMapper mapper;

    /**
     * Class constructor. Start setup.
     */
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

    /**
     * Fetches API access connection info from files.
     */
    public void getAPIKeys() {
        ClassLoader classLoader = FoodHelper.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("app_id.txt");
        X_APP_ID = readFromFile(inputStream);
        inputStream = classLoader.getResourceAsStream("app_key.txt");
        X_APP_KEY = readFromFile(inputStream);
        logger.debug("Keys fetched correctly");
    }

    /**
     * Reads data from file, returns it as String object.
     * @param filename name of file to read from
     * @return file content converted into String
     */
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

    /**
     * Reads input from console, fetches data from API using provided necessary access values. Converts API output to
     * Food object. Uses CURL to fetch data.
     * @param app_id object created from console input
     * @param app_key Statement object created in connectToDatabase method
     * @param query input from console that is later used to fetch data from API
     * @return Food object created from console input
     */
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
//        System.out.println(body);
        try {
            process.waitFor();
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
        ArrayList<FoodElement> foods = new ArrayList<>();
        foods.addAll(output.getFoods());
        try {
            for (int i = 0; i < foods.size(); i++) {
                System.out.println(foods.get(i).displayValues());
            }
        } catch (NullPointerException e) {
            logger.error("Couldn't get a food from name provided!");
        }
        connectToDatabase(foods);
        return output;
    }

    /**
     * Connects to database setup in docker, creates food table in database (if doesn't exist already).
     * @param foodElements object array created from console input
     */
    public void connectToDatabase(ArrayList <FoodElement> foodElements) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "postgres");
            Statement statement = connection.createStatement();
            statement.execute("create table if not exists food(name varchar(100), weight_in_grams int, " +
                    "calories int, fat int, saturated_fat int, cholesterol int, sodium int, carbohydrate int," +
                    "dietary_fiber int, sugars int, protein int)");
            for(int i=0; i< foodElements.size(); i++) {
                addDataToDatabase(foodElements.get(i), statement);
            }
            /*ResultSet resultSet = statement.executeQuery("select * from food");
            while(resultSet.next()) {
                System.out.println("SQL: " + resultSet.getString("title"));
            }
            resultSet.close();*/
            statement.close();
            connection.close();
        } catch (SQLException e) {
            logger.error("Cannot connect to database");
        }
    }

    /**
     * Adds row to database. Row contains information on inputted food(s) from console.
     * @param foodElement object created from console input
     * @param statement Statement object created in connectToDatabase method
     */
    public void addDataToDatabase(FoodElement foodElement, Statement statement) {
        try {
            statement.execute("insert into food(name, weight_in_grams, calories, fat, saturated_fat, cholesterol, " +
                    "sodium, carbohydrate, dietary_fiber, sugars, protein) values ('" + foodElement.getFood_name() +
                    "', '" + foodElement.getServing_weight_grams() + "', '" + foodElement.getNf_calories() + "', '" +
                    foodElement.getNf_total_fat() + "', '" + foodElement.getNf_saturated_fat() + "', '" +
                    foodElement.getNf_cholesterol() + "', '" + foodElement.getNf_sodium() + "', '" +
                    foodElement.getNf_total_carbohydrate() + "', '" + foodElement.getNf_dietary_fiber() + "', '" +
                    foodElement.getNf_sugars() + "', '" + foodElement.getNf_protein() + "')");
        } catch (SQLException e) {
            logger.error("Cannot add object to table");
        }
    }

    /**
     * Returns x_app_id value required when connect to API.
     * @return  x_app_id value
     */
    public String getX_APP_ID() {
        return X_APP_ID;
    }

    /**
     * Returns x_app_key value required when connect to API.
     * @return  x_app_key value
     */
    public String getX_APP_KEY() {
        return X_APP_KEY;
    }
}
