package group12.tcss450.uw.edu.appproject;


import com.google.gson.annotations.SerializedName;

import java.util.List;

class Response<T> {
    @SerializedName("count")
    private int count;

    @SerializedName("recipes")
    private List<T> recipes;

    /**
     * Gets the count.
     * @return the count.
     */
    public int getCount() {
        return count;
    }

    /**
     * Gets a list of recipes.
     * @return recipes from the response.
     */
    public List<T> getRecipes() {
        return recipes;
    }

}
