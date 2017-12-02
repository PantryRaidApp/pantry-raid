package group12.tcss450.uw.edu.appproject.Fragments;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import group12.tcss450.uw.edu.appproject.R;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class IngredientSearchFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;

    /**
     * The list of ingredients the user is searching for.
     */
    ArrayList<String> mIngredientList;

    /**
     * Adapter used for the ListView.
     */
    ArrayAdapter<String> mAdapter;

    /**
     * Search bar for entering ingredients.
     */
    EditText mIngredientSearchBar;

    /**
     * The ListView that holds the ingredients being searched for.
     */
    ListView mListView;

    public IngredientSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIngredientList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ingredient_search, container, false);

        mAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.recipe_ingredient, mIngredientList);

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

        mIngredientSearchBar = (EditText)v.findViewById(R.id.searchBar);

        //Enter works to add an ingredient to the list
        //ONLY WORKS FOR CERTAIN KEYBOARDS
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

        //Removes individually pressed ingredients
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
     * On button pressed.
     * @param uri
     */
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FavoritesFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addButton:
                addIngredientToList();
                break;
            case R.id.doneButton:
                //TODO send ingredients back to activity
                //So it goes to DisplayRecipesFragment
                break;
            case R.id.clearButton:
                clearIngredientList();
                break;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
