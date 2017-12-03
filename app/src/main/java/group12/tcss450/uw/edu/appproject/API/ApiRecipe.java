package group12.tcss450.uw.edu.appproject.API;

import com.google.gson.annotations.SerializedName;

/**
 * Recipe class used to hold all recipe data.
 * This class should only be filled using JSON from a Web API call.
 *
 * @author Zira Cook
 */
public class ApiRecipe {
    @SerializedName("publisher")
    private String publisher;

    @SerializedName("f2f_url")
    private String f2fUrl;

    @SerializedName("title")
    private String title;

    @SerializedName("source_url")
    private String sourceUrl;

    @SerializedName("recipe_id")
    private String recipeId;

    @SerializedName("image_url")
    private String imageUrl;

    @SerializedName("social_rank")
    private Double socialRank;

    @SerializedName("publisher_url")
    private String publisherUrl;

    /**
     * Gets the publisher.
     * @return the publisher of the recipe.
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Gets the url on food2fork.
     * @return the food2fork url of the recipe.
     */
    public String getF2fUrl() {
        return f2fUrl;
    }

    /**
     * Gets the title.
     * @return the title of the recipe.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the url.
     * @return the url of the recipe.
     */
    public String getSourceUrl() {
        return sourceUrl;
    }

    /**
     * Gets the recipe id.
     * @return the recipe id of the recipe.
     */
    public String getRecipeId() {
        return recipeId;
    }

    /**
     * Gets the image url.
     * @return the image url of the recipe.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Gets the food2fork social rank.
     * @return the social rank of the recipe.
     */
    public Double getSocialRank() {
        return socialRank;
    }

    /**
     * Gets the publisher url.
     * @return the publisher uel of the recipe.
     */
    public String getPublisherUrl() {
        return publisherUrl;
    }

    @Override
    public String toString() {
        String paddingForSecondLine = "        ";
        return title + "\n" + paddingForSecondLine + publisher;
    }
}
