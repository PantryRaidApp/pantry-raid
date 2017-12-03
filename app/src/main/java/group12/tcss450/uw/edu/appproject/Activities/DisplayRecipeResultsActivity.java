package group12.tcss450.uw.edu.appproject.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import group12.tcss450.uw.edu.appproject.Fragments.DisplayRecipesFragment;
import group12.tcss450.uw.edu.appproject.R;

import static android.content.ContentValues.TAG;

public class DisplayRecipeResultsActivity extends AppCompatActivity implements
        DisplayRecipesFragment.OnFragmentInteractionListener {

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

    @Override
    public void onFragmentInteraction(String theString) {
        Bundle bundle = new Bundle();
        bundle.putString("url", theString);

    }
}
