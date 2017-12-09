package group12.tcss450.uw.edu.appproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import group12.tcss450.uw.edu.appproject.fragments.DisplayRecipesFragment;
import group12.tcss450.uw.edu.appproject.R;

import static android.content.ContentValues.TAG;

/**
 * Activity that displays recipes that have been searched for by the user.
 */
public class DisplayRecipeResultsActivity extends AppCompatActivity implements
        DisplayRecipesFragment.OnFragmentInteractionListener {

    /**
     * Method that creates new DisplayRecipeResultsActivity. Takes in a bundle of saved
     * instance state in order to previous search results (if any).
     *@param savedInstanceState bundle of saved instance used to create Activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = this.getIntent().getExtras();

        if (savedInstanceState == null) {
            Log.d(TAG, "savedInstanceState == null");
            if (findViewById(R.id.recipeFragmentContainer) != null) {
                Log.d(TAG, "recipeFragmentContainer != null");
                DisplayRecipesFragment displayRecipesFragment = new DisplayRecipesFragment();
                displayRecipesFragment.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.recipeFragmentContainer, displayRecipesFragment).commit();
            }
        }

    }

    /**
     * Listens for interaction from recipes displayed and sends url information to create a
     * WebViewActivity to display the clicked recipe.
     * @param theString
     */
    @Override
    public void onFragmentInteraction(String theString) {
        Bundle bundle = new Bundle();
        bundle.putString("url", theString);
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
