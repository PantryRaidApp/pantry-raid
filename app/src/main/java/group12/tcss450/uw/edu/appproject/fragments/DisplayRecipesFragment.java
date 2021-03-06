package group12.tcss450.uw.edu.appproject.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import group12.tcss450.uw.edu.appproject.api.ApiRecipe;
import group12.tcss450.uw.edu.appproject.api.ApiRecipeResponse;
import group12.tcss450.uw.edu.appproject.api.Response;
import group12.tcss450.uw.edu.appproject.R;

import static android.content.ContentValues.TAG;

/**
 * Fragment that displays the top 30 recipes that return from the entered ingredients.
 */
public class DisplayRecipesFragment extends Fragment{
    /** Listener to send back recipe data. */
    private OnFragmentInteractionListener mListener;

    /** The list of recipes returned from the API. */
    private ArrayList<ApiRecipe> mRecipeList;

    /** The adapter used for the recipes. */
    private ArrayAdapter<ApiRecipe> mRecipeAdapter;

    /** The ListView where the list of recipes is displayed. */
    private ListView mListView;

    /** API key used to connect to the API. */
    private String myApiKey;

    /** The string used to call the api for specific data. */
    private String myApiCall;

    /** The Gson builder used to create a Gson object. */
    private GsonBuilder myBuilder;

    /** Gson object used to parse Json from the API response. */
    private Gson myGson;

    /** String used to identify list parameter. */
    private static final String ARG_PARAM = "ingredientList";

    /** List of ingredients used to query API. */
    private ArrayList<String> mIngredientList;

    /**
     * Required empty public constructor.
     */
    public DisplayRecipesFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ingredientList list of ingredients as strings
     * @return A new instance of fragment DisplayRecipesFragment.
     */
    public static DisplayRecipesFragment newInstance(ArrayList<String> ingredientList) {
        Log.d(TAG, "newInstance:");
        DisplayRecipesFragment fragment = new DisplayRecipesFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM, ingredientList);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * onCreate method sets up all data for onCreateView.
     * The list of ingredients in saved here from the bundle.
     * All setup methods are called from here.
     * @param savedInstanceState the bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIngredientList = getArguments().getStringArrayList(ARG_PARAM);
            Log.d(TAG, "ingredientList" + mIngredientList.toString());
        }
        mRecipeList = new ArrayList<>();
        mRecipeAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.recipe_result, mRecipeList);

        myApiKey = getString(R.string.food2ForkKey);
        myApiCall = "http://food2fork.com/api/search?key=" + myApiKey + "&q=";
        myBuilder = new GsonBuilder();
        myGson = myBuilder.create();

        //Form the API get call
        createApiCallFromIngredients();

        //Create the async task to call the API
        AsyncTask<String, Void, String> task = new GetWebServiceTask();
        task.execute(myApiCall);

    }

    /**
     * Sets up the view for the page display.
     * @param inflater the LayoutInflater
     * @param container the ViewGroup
     * @param savedInstanceState the Bundle
     * @return the View created in the method.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_display_recipes, container, false);

        if (mListView == null) {
            mListView = (ListView)v.findViewById(R.id.recipe_list_view);
        }
        mListView.setAdapter(mRecipeAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = mRecipeList.get(i).getSourceUrl();
                Log.d(TAG, "onItemClick: a recipe was selected \n" + name);
                mListener.onFragmentInteraction(name);
            }
        });

        return v;
    }

    /**
     * Forms an API call based on the ingredients passed in from the bundle.
     */
    private void createApiCallFromIngredients() {
        //For every ingredient:
        //replace spaces with '%20' and add to APIcall
        for (String ingredient: mIngredientList) {
            ingredient = ingredient.replaceAll(" ", "%20");
            myApiCall += ingredient + ',';
        }

        if (myApiCall.endsWith(",")) {
            myApiCall = myApiCall.substring(0, myApiCall.length() - 1);
        }
    }

    /**
     * Instantiates the OnFragmentInteractionListener for ListView behavior.
     * @param context the Context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Handles cleanup when the fragment is detached.
     * Sets the onFragmentInteractionListener to null.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Passes back a URL of the selected recipe.
     *
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String theString);
    }

    /**
     * Handles the webservice methods for a GET call to the api.
     */
    private class GetWebServiceTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            String response = "";
            String url = strings[0];
            try {
                URL urlObject = new URL(url);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s;

                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                e.printStackTrace();
                response = "Unable to connect";
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
            }


            return response;
        }

        /**
         * Handles displaying the results of the API after the async task finished executing.
         * @param result the result of the async task.
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            Response<ApiRecipe> recipeResponse = myGson.fromJson(result, ApiRecipeResponse.class);
            Log.d(TAG, "recipeResponse:" + recipeResponse.getRecipes().toString());

            for (ApiRecipe recipe: recipeResponse.getRecipes()) {
                mRecipeList.add(recipe);
            }
            Log.d(TAG, "mRecipeList: " + mRecipeList.toString());
            mRecipeAdapter.notifyDataSetChanged();

            if (mRecipeList.size() <= 0) {
                TextView tv = getView().findViewById(R.id.displayRecipesErrorNoRecipes);
                tv.setVisibility(View.VISIBLE);
            }
        }
    }

}
