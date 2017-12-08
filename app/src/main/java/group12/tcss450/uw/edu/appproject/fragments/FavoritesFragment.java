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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import group12.tcss450.uw.edu.appproject.R;
import group12.tcss450.uw.edu.appproject.activities.MainActivity;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavoritesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private ArrayAdapter<String> mArrayAdapter;
    private String[] mFavoritesUrls;
    private String[] mFavorites;
    private ListView mListView;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FavoritesFragment.
     */
    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFavoritesUrls = MainActivity.getAllFavorites();
        //mFavorites = getRecipeFromUrl();
        mArrayAdapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                R.layout.recipe_result, mFavoritesUrls);
    }

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
                Log.d(TAG, "onItemClick: ");
                //mListener.onFragmentInteraction(name);

                /*
                WebViewActivity webView = new WebViewActivity();
                webView.setUrl(name);
                Intent intent = new Intent(getActivity(), webView.getClass());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                */
            }
        });
        return v;
    }

    private String[] getRecipeFromUrl() {
        String[] recipes = new String[mFavoritesUrls.length];
        Log.d(TAG, "getRecipeFromUrl:");

        for (int i = 0; i < mFavoritesUrls.length; i++) {
            try {
                recipes[i] = getTitleFromUrl(mFavoritesUrls[i]);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
      return recipes;
    }

    private String getTitleFromUrl(String theUrl) throws Exception{
        URL url = new URL(theUrl);
        URLConnection urlConnection = url.openConnection();
        DataInputStream dis = new DataInputStream(urlConnection.getInputStream());
        String html = "", tmp = "";
        while ((tmp = dis.readUTF()) != null) {
            html += " " + tmp;
        }
        dis.close();

        html = html.replaceAll("\\s+", " ");
        Pattern p = Pattern.compile("<title>(.*?)</title>");
        Matcher m = p.matcher(html);
        while (m.find()) {
            Log.d("HALP", "getTitleFromUrl:" + m.group(1));
        }
        return "hi";
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
        void onFragmentInteraction(String theString);
    }
}
