package group12.tcss450.uw.edu.appproject.API;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Used to hold data from the API.
 * @param <T> generic array type contained by the overall json.
 */
public class Response<T> {
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
