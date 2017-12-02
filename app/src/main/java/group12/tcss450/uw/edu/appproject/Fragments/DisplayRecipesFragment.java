package group12.tcss450.uw.edu.appproject.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import group12.tcss450.uw.edu.appproject.API.ApiRecipe;
import group12.tcss450.uw.edu.appproject.API.ApiRecipeResponse;
import group12.tcss450.uw.edu.appproject.API.Response;
import group12.tcss450.uw.edu.appproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DisplayRecipesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DisplayRecipesFragment extends Fragment{
    //Strings used to store data for calling the API
    private String myApiKey;
    private String myApiCall;

    //GSON objects for parsing JSON returned by API call
    private GsonBuilder myBuilder;
    private Gson myGson;

    //String used to identify list parameter
    private static final String ARG_PARAM = "ingredientList";
    //List of ingredients used to query API
    private ArrayList<String> mIngredientList;


    public DisplayRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param ingredientList list of ingredients as strings
     * @return A new instance of fragment DisplayRecipesFragment.
     */
    public static DisplayRecipesFragment newInstance(ArrayList<String> ingredientList) {
        DisplayRecipesFragment fragment = new DisplayRecipesFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM, ingredientList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIngredientList = getArguments().getStringArrayList(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_display_recipes, container, false);
        myApiKey = getString(R.string.food2ForkKey);
        myApiCall = "http://food2fork.com/api/search?key=" + myApiKey + "&q=";
        myBuilder = new GsonBuilder();
        myBuilder.setPrettyPrinting();
        myGson = myBuilder.create();
        return v;
    }

    //http://food2fork.com/api/search?key=e60347278aefc1439128a6281dab7812&q=shredded%20chicken,onion,green%20pepper

    /**
     * Handles the webservice methods for a GET call to the api.
     */
    private class GetWebServiceTask extends AsyncTask<String, Void, String> {
        private final String SERVICE = "_get.php";


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

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }
            Response<ApiRecipe> recipeResponse = myGson.fromJson(result, ApiRecipeResponse.class);
            String recipeString = recipeResponse.getRecipes().toString();
            //recipeString = recipeString.replaceAll(",", "\n");
        }
    }

}
