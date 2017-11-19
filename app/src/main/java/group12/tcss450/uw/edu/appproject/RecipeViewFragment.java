package group12.tcss450.uw.edu.appproject;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 * Provides functionality to display the top 30 recipes from food2fork.
 */
public class RecipeViewFragment extends Fragment implements View.OnClickListener{
    private String myApiKey;
    private String myApiCall;

    private Button myButton;
    private TextView myTextView;

    private GsonBuilder myBuilder;
    private Gson myGson;

    public RecipeViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_display_top_recipes, container, false);

        myApiKey = getString(R.string.food2ForkKey);
        myApiCall = "http://food2fork.com/api/search?key=" + myApiKey + "&sort=t";

        myButton = v.findViewById(R.id.callButton);
        myButton.setOnClickListener(this);
        myTextView = v.findViewById(R.id.apiReturnTextView);

        myBuilder = new GsonBuilder();
        myBuilder.setPrettyPrinting();
        myGson = myBuilder.create();

        return v;
    }

    @Override
    public void onClick(View view) {
        AsyncTask<String, Void, String> task = new GetWebServiceTask();
        task.execute(myApiCall);
    }

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
            myTextView.setText(recipeString);
        }
    }
}
