package group12.tcss450.uw.edu.appproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import group12.tcss450.uw.edu.appproject.R;
import group12.tcss450.uw.edu.appproject.activities.MainActivity;

import static android.content.ContentValues.TAG;

/**
 * The fragment that displays the saved user favorites.
 * Used in TabbedPageActivity.
 */
public class FavoritesFragment extends Fragment{
    /** The listener used to send back url data. */
    private OnFragmentInteractionListener mListener;

    /** The adapter used to display the recipe urls in the ListView. */
    private ArrayAdapter<String> mArrayAdapter;

    /** The list of favorites displayed. */
    private String[] mFavoritesUrls;

    private ArrayList<String> mFavorites;

    /** List view where the favorites are displayed. */
    private ListView mListView;

    /**
     * Required empty public constructor
     */
    public FavoritesFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FavoritesFragment.
     */
    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    /**
     * Sets up data and adapter for the fragment
     * @param savedInstanceState the bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavoritesUrls = MainActivity.getAllFavorites();
        mFavorites = new ArrayList<>(Arrays.asList(mFavoritesUrls));

        mArrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                R.layout.recipe_result, mFavorites);
    }

    /**
     * Sets up the view and its components for the fragment.
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return the View created from the method.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);
        if (mListView == null) {
            mListView = (ListView)v.findViewById(R.id.favorites_list_view);
        }
        mListView.setAdapter(mArrayAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = mArrayAdapter.getItem(i);
                Log.d(TAG, "onItemClick: " + name);
                mListener.onFragmentInteraction(name);
            }
        });
        return v;
    }

    /**
     * Method to instantiate the OnFragmentInteractionListener when the fragment is attached.
     * @param context Context.
     */
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

    /**
     * Cleanup for when the fragment is detached.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        Log.d("FavoritesFragment", "onResume:" + Arrays.toString(mFavoritesUrls));
        super.onResume();
        mFavoritesUrls = MainActivity.getAllFavorites();

        for (String s: mFavoritesUrls) {
            if (!mFavorites.contains(s)) {
                mFavorites.add(s);
            }
        }

        if (mFavoritesUrls.length != mFavorites.size()) {
            Iterator itr = mFavorites.iterator();
            while (itr.hasNext()) {
                String s = (String) itr.next();
                if (!Arrays.asList(mFavoritesUrls).contains(s)) {
                    itr.remove();
                }
            }
        }


        Log.d("FavoritesFragment", "new list data" + Arrays.toString(mFavoritesUrls));
        mArrayAdapter.notifyDataSetChanged();
    }
    /**
     * Sends back the url of the selected favorites to open a WebView.
     *
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String theString);
    }

}
