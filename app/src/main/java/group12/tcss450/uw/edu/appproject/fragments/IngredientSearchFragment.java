package group12.tcss450.uw.edu.appproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import group12.tcss450.uw.edu.appproject.database.DBManager;
import group12.tcss450.uw.edu.appproject.R;
import group12.tcss450.uw.edu.appproject.util.SimilarityAdapter;

import static android.content.ContentValues.TAG;

/**
 * Fragment that allows a user to enter terms into a search bar.
 * Entered strings are saved and used to call the API.
 */
public class IngredientSearchFragment extends Fragment implements View.OnClickListener{
    /** Listener to send back user data. */
    private OnFragmentInteractionListener mListener;

    /** The list of ingredients enter. */
    private ArrayList<String> mIngredientList;

    /** Adapter for the list view. */
    private ArrayAdapter<String> mAdapter;

    /** The search bar where text is entered. */
    private AutoCompleteTextView mIngredientSearchBar;

    /** The list view to display ingredients. */
    private ListView mListView;

    /**
     * Required empty public constructor
     */
    public IngredientSearchFragment() { }

    /**
     * Sets up fragment behavior.
     * @param savedInstanceState Bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIngredientList = new ArrayList<>();
        mAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.recipe_ingredient, mIngredientList);
    }

    /**
     * Sets up fragment for displaying the fragment.
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return the created view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ingredient_search, container, false);

        if (mListView == null) {
            mListView = (ListView)v.findViewById(R.id.ingredientList);
        }
        mListView.setAdapter(mAdapter);

        Button b = v.findViewById(R.id.doneButton);
        b.setOnClickListener(this);

        b = v.findViewById(R.id.addButton);
        b.setOnClickListener(this);

        b = v.findViewById(R.id.clearButton);
        b.setOnClickListener(this);

        mIngredientSearchBar = (AutoCompleteTextView) v.findViewById(R.id.searchBar);
        String[] ingreds = new String[0];
        try {
            ingreds = DBManager.getIngredients();
        } catch (Exception e) {
            Log.d(TAG, "db error in search");
        }
        SimilarityAdapter adapter = new SimilarityAdapter(getContext());
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line, ingreds);*/
        mIngredientSearchBar.setAdapter(adapter);

        mIngredientSearchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                Log.d(TAG, "onKey: pressed");
                //When enter is pressed the current entered text is added to the ingredient list
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (i == KeyEvent.KEYCODE_ENTER) {
                        addIngredientToList();
                        return true;
                    }
                }

                return false;
            }
        });
        mIngredientSearchBar.setDropDownBackgroundResource(R.color.colorAutocomplete);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ingredient = mIngredientList.get(i);
                Log.d(TAG, "onItemClick: remove" + ingredient);
                removeItemFromIngredientList(ingredient);
            }
        });

        return v;
    }

    /**
     * Adds the current text in the search bar to a list of ingredients.
     */
    private void addIngredientToList() {
        String ingredient = mIngredientSearchBar.getText().toString().trim().toLowerCase();
        if (!mIngredientList.contains(ingredient) && !ingredient.equals("")) {
            mIngredientList.add(ingredient);
            mAdapter.notifyDataSetChanged();
        }
        mIngredientSearchBar.setText("");
    }

    /**
     * Clear all ingredients from the search list.
     */
    private void clearIngredientList() {
        mIngredientList.clear();
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Removes an item from the backend ingredient list and updates the front end ListView.
     * @param ingredientToRemove the ingredient to remove.
     */
    private void removeItemFromIngredientList(String ingredientToRemove) {
        if (mIngredientList.contains(ingredientToRemove)) {
            mIngredientList.remove(ingredientToRemove);
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Instantiation of variables when fragment is attached.
     * @param context Context.
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
     * Cleanup when fragment is detached.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Adds functionality to buttons.
     * @param view the view of the fragment.
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addButton:
                addIngredientToList();
                break;
            case R.id.doneButton:
                Log.d(TAG, "onClick: done, start search");
                mListener.onFragmentInteraction(mIngredientList);
                break;
            case R.id.clearButton:
                clearIngredientList();
                break;
        }
    }

    /**
     * Returns back a list of entered search ingredients..
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(ArrayList<String> theIngredientList);
    }

}