package com.example.anuj_nj.mytestapp;

/**
 * Created by Anuj_nj on 28-02-2018.
 */

public class FoodType {
    private String ImageUrl;
    private String FoodType;
    private String FoodTypeSearch;

    public FoodType(String imageUrl, String foodType, String foodTypeSearch) {
        ImageUrl = imageUrl;
        FoodType = foodType;
        FoodTypeSearch = foodTypeSearch;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getFoodType() {
        return FoodType;
    }

    public void setFoodType(String foodType) {
        FoodType = foodType;
    }

    public String getFoodTypeSearch() {
        return FoodTypeSearch;
    }

    public void setFoodTypeSearch(String foodTypeSearch) {
        FoodTypeSearch = foodTypeSearch;
    }
}
