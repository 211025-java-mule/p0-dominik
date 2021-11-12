// Fetched from app.quicktype.io website due to JSON's complexity

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.OffsetDateTime;

public class FoodElement {
    private String food_name;
    @JsonIgnore
    private Object brand_name;
    @JsonIgnore
    private long service_qty;
    @JsonIgnore
    private String serving_unit;
    private long serving_weight_grams;
    private long nf_calories;
    private long nf_total_fat;
    private long nf_saturated_fat;
    private long nf_cholesterol;
    private long nf_sodium;
    private long nf_total_carbohydrate;
    private long nf_dietary_fiber;
    private long nf_sugars;
    private long nf_protein;
    @JsonIgnore
    private Object nf_potassium;
    @JsonIgnore
    private Object nf_p;
    @JsonIgnore
    private FullNutrient[] full_nutrients;
    private String nix_brand_name;
    @JsonIgnore
    private String nix_brand_id;
    @JsonIgnore
    private String nix_item_name;
    @JsonIgnore
    private String nix_item_id;
    @JsonIgnore
    private Object upc;
    @JsonIgnore
    private OffsetDateTime consumed_at;
    @JsonIgnore
    private Metadata metadata;
    @JsonIgnore
    private long source;
    @JsonIgnore
    private long ndb_no;
    @JsonIgnore
    private Tags tags;
    @JsonIgnore
    private Object[] alt_measures;
    @JsonIgnore
    private Object lat;
    @JsonIgnore
    private Object lng;
    @JsonIgnore
    private long meal_type;
    @JsonIgnore
    private Photo photo;
    @JsonIgnore
    private Object sub_recipe;
    @JsonIgnore
    private Object class_code;

    public String getFood_name() {
        return food_name;
    }

    public long getServing_weight_grams() {
        return serving_weight_grams;
    }

    public long getNf_calories() {
        return nf_calories;
    }

    public long getNf_total_fat() {
        return nf_total_fat;
    }

    public long getNf_saturated_fat() {
        return nf_saturated_fat;
    }

    public long getNf_cholesterol() {
        return nf_cholesterol;
    }

    public long getNf_sodium() {
        return nf_sodium;
    }

    public long getNf_total_carbohydrate() {
        return nf_total_carbohydrate;
    }

    public long getNf_dietary_fiber() {
        return nf_dietary_fiber;
    }

    public long getNf_sugars() {
        return nf_sugars;
    }

    public long getNf_protein() {
        return nf_protein;
    }

    public Object getNf_potassium() {
        return nf_potassium;
    }

    @JsonIgnore
    private Object brick_code;
    @JsonIgnore
    private Object tag_id;

    @Override
    public String toString() {
        return food_name + ", " + serving_weight_grams +
                ", " + nf_calories +
                ", " + nf_total_fat +
                ", " + nf_saturated_fat +
                ", " + nf_cholesterol +
                ", " + nf_sodium +
                ", " + nf_total_carbohydrate +
                ", " + nf_dietary_fiber +
                ", " + nf_sugars +
                ", " + nf_protein;
    }

    public String displayValues() {
        return "Food name: " + food_name + "\n" +
                "Serving weight (g): " + serving_weight_grams + "\n" +
                "Calories (kcal): " + nf_calories + "\n" +
                "Fat (g): " + nf_total_fat + "\n" +
                "Saturated fat (g): " + nf_saturated_fat + "\n" +
                "Cholesterol (mg): " + nf_cholesterol + "\n" +
                "Sodium (mg): " + nf_sodium + "\n" +
                "Carbohydrate (g): " + nf_total_carbohydrate + "\n" +
                "Dietary fiber (g): " + nf_dietary_fiber + "\n" +
                "Sugars (g): " + nf_sugars + "\n" +
                "Protein (g): " + nf_protein + "\n";
    }
}