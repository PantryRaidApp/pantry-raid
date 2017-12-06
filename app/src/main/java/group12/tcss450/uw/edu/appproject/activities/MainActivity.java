package group12.tcss450.uw.edu.appproject.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

import group12.tcss450.uw.edu.appproject.database.DBManager;
import group12.tcss450.uw.edu.appproject.database.Verifier;
import group12.tcss450.uw.edu.appproject.fragments.VerifyEmailPasswordFragment;
import group12.tcss450.uw.edu.appproject.fragments.ForgotPasswordFragment;
import group12.tcss450.uw.edu.appproject.fragments.MainPageFragment;
import group12.tcss450.uw.edu.appproject.fragments.RegisterFragment;
import group12.tcss450.uw.edu.appproject.R;

/**
 * The main activity that handles all fragment transactions into the main functionality of the app.
 */
public class MainActivity extends AppCompatActivity implements
        MainPageFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener,
        VerifyEmailPasswordFragment.OnFragmentInteractionListener,
        ForgotPasswordFragment.OnFragmentInteractionListener {

    private AsyncTask<String, Integer, String> mTask;
    private DBManager database;
    String code;
    private static String user;
    private static String password;

    /**
     * Generates a 5-digit code and sends it to the given email address.
     * @param email The email address to send the code to.
     * @return The 5-digit code as a string, or null if there was an error.
     */
    public String generateCode(String email) {
        try {
            Verifier verifier = new Verifier();
            return verifier.execute(email).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getStringExtra("user") != null) {
            Log.d("BootUpReceiver", "We got it! "+getIntent().getStringExtra("user"));
            autoLogin(getIntent().getStringExtra("user"));
        }

        database = new DBManager();
        try {
            Log.d("TEST", Boolean.toString(database.validCredentials("eeeshe", "wow")));
            Log.d("TEST", Boolean.toString(database.validCredentials("eeessdfsdhe", "wow")));
            Log.d("TEST", Arrays.toString(database.getIngredients("mush")));
        } catch (Exception e) {
            Log.d("TEST!!", "asplode");
            e.printStackTrace();
        }

        if (savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, new MainPageFragment()).commit();
            }
        }
        
    }
    @Override
    public void onFragmentInteraction(String theString) {
        if (theString.equals(getString(R.string.register_button))){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (theString.equals(getString(R.string.skip_button))){
            clearBackStack();
            Intent intent = new Intent(this, TabbedPageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (theString.equals(getString(R.string.forgot_password_button)) ){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new MainPageFragment())
                    .commit();
        } else if (theString.startsWith("verify:")){
            try {
                theString = theString.split(":")[1];
                if (theString.equals(code)) {
                    if(password !=null) {
                        database.addNewUser(user, password);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, new MainPageFragment())
                                .commit();
                    }else{
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, new ForgotPasswordFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                } else {
                    Toast.makeText(this, "Invalid confirmation code. Please try again.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            if (theString.length() > 0) {
                code = generateCode(theString);
                user = theString;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new VerifyEmailPasswordFragment())
                        .addToBackStack(null)
                        .commit();
            }
        }

    }

    /**
     * Sets the user details, effectively setting the login state of the app.
     * Saves the user in shared preferences.
     * @param theUser the user email.
     * @param thePassword the user password.
     */
    public void setUserandPassword(String theUser, String thePassword) {
        user = theUser;
        password = thePassword;
        SharedPreferences sharedPreferences =
                getSharedPreferences(getString(R.string.SHARED_PREFS), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(getString(R.string.username_pref), theUser);
        editor.apply();
    }

    /**
     * Adds the favorite to the current user's list of favorites.
     * @param url The url to add to the favorites.
     * @return True if the add was successful, false otherwise.
     */
    public static boolean addFavorite(String url) {
        if (user == null)
            return false;
        try {
            Log.d("FAV", "ATTEMPTING TO add " + url + " to favorites of " + user + "!");
            return DBManager.addFavorite(getUserId(), url);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Checks if a recipe url has already been favorited.
     * @param url The url to check.
     * @return True if the url is already a favorite, false otherwise.
     */
    public static boolean isAFavorite(String url) {
        String[] favorites = getAllFavorites();
        int index = Arrays.binarySearch(favorites, url);

        for (String s: favorites) {
            if (s.equals(url)) {
                return true;
            }
        }
        return false;
    }

    public void autoLogin(String theUser) {
        user = theUser;
        clearBackStack();
        Intent intent = new Intent(this, TabbedPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    /**
     * Gets the user id of the current user logged in, -1 if there is an error or the user
     * isn't logged in.
     * @return The user id, -1 if error or the user isn't logged in.
     */
    public static int getUserId() {
        try {
            return DBManager.getUserId(user);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Returns a string array containing all the urls of the favorites of the
     * given user.
     * @return A string array with favorite URLs.
     */
    public static String[] getAllFavorites() {
        try {
            return DBManager.getFavorites(getUserId());
        } catch (Exception e) {
            return new String[0];
        }
    }
          
    public static String getUserName() {
        return user;
    }

    @Override
    public void onBackPressed() {
        if(mTask != null && mTask.getStatus() == AsyncTask.Status.RUNNING) {
            mTask.cancel(true);
        }
        else {
            super.onBackPressed();
        }
    }

    /**
     * Clears all fragments from the back stack when starting the TabbedPageActivity.
     */
    private void clearBackStack() {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}