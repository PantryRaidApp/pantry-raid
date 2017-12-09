package group12.tcss450.uw.edu.appproject.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import group12.tcss450.uw.edu.appproject.fragments.FavoritesFragment;
import group12.tcss450.uw.edu.appproject.fragments.IngredientSearchFragment;
import group12.tcss450.uw.edu.appproject.R;

import static android.content.ContentValues.TAG;

/**
 * Activity that gives a tabbed page , one for ingredient search and one for user favorites.
 */
public class TabbedPageActivity extends AppCompatActivity implements
        IngredientSearchFragment.OnFragmentInteractionListener,
        FavoritesFragment.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private IngredientSearchFragment ingredientSearchFragment;
    private FavoritesFragment favoritesFragment;

    /**
     * Creates instance of Activity
     * @param savedInstanceState saved state of activity (if any)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_page);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        ingredientSearchFragment = new IngredientSearchFragment();
        favoritesFragment = new FavoritesFragment();
    }

    /**
     * Creates menu when activity is created
     * @param menu a menu of items
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_page, menu);
        return true;
    }

    /**
     * When String is passed from FragmentInterationListener, creates instance of a WebViewActivity
     * to view Favorited recipe that had be clicked.
     * @param theString a string that represents the url of a favorited recipe
     */
    @Override
    public void onFragmentInteraction(String theString) {
        Bundle bundle = new Bundle();
        bundle.putString("url", theString);
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Takes in a list of ingredients that will be passed into DisplayRecipeResults
     * activity to be searched.
     * @param theIngredientList ArrayList containing strings representing ingredients that had been
     * searched for
     */
    @Override
    public void onFragmentInteraction(ArrayList<String> theIngredientList) {
        //List only send back from IngredientSearchFragment
        Log.d(TAG, "onFragmentInteraction: return from IngredientSearchFragment");
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("ingredientList", theIngredientList);

        Intent intent = new Intent(this, DisplayRecipeResultsActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    /**
     * Calls Super for onBackPressed.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * Handles actionbar items.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        /**
         * Creates View for Activity
         * @param inflater a Layout inflater
         * @param container a ViewGroup
         * @param savedInstanceState a bundle
         * @return rootView a view
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.main_page_activity_tab, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            ListViewCompat listViewCompat = rootView.findViewById(R.id.ingredientList);
            listViewCompat.setAdapter(new BaseAdapter() {
                List<String> items = new ArrayList<>();

                @Override
                public int getCount() {
                    return items.size();
                } //count of number of items

                @Override
                public Object getItem(int i) {
                    return null;
                }//item

                @Override
                public long getItemId(int i) {
                    return 0;
                }//item id

                @Override
                public View getView(int i, View view, ViewGroup viewGroup) {
                    return null;
                }//view
            });
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public final int NUMBER_OF_TABS = 2;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return ingredientSearchFragment;
                case 1:
                    return favoritesFragment;
            }
            return null;
        }

        /**
         * To get number of tabs to display
         * @return number of tabs
         */
        @Override
        public int getCount() {
            // Show 3 total pages.
            return NUMBER_OF_TABS;
        }

        /**
         * Gets the name of the tabs.
         * @param position int position of tab
         * @return name on tab or null depending on what is selected
         */
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Search Recipes";
                case 1:
                    return "Favorites";
            }
            return null;
        }
    }
}
