package group12.tcss450.uw.edu.appproject.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

import group12.tcss450.uw.edu.appproject.Database.DBManager;
import group12.tcss450.uw.edu.appproject.Database.Verifier;
import group12.tcss450.uw.edu.appproject.Fragments.VerifyEmailPasswordFragment;
import group12.tcss450.uw.edu.appproject.Fragments.ForgotPasswordFragment;
import group12.tcss450.uw.edu.appproject.Fragments.LoginFragment;
import group12.tcss450.uw.edu.appproject.Fragments.MainPageFragment;
import group12.tcss450.uw.edu.appproject.Fragments.RegisterFragment;
import group12.tcss450.uw.edu.appproject.R;

/**
 * The main activity that handles all fragment transactions and activities.
 * This is the entry point into the app no matter if a user is already logged in.
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
            Intent intent = new Intent(this, TabbedPageActivity.class);
            startActivity(intent);

        } else if (theString.equals(getString(R.string.forgot_password_button)) ){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new ForgotPasswordFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (theString.startsWith("verify:")){
            try {
                theString = theString.split(":")[1];
                if (theString.equals(code)) {
                    if(password !=null) {
                        database.addNewUser(user, password);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, new LoginFragment())
                                .addToBackStack(null)
                                .commit();
                    }else{
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragmentContainer, new ForgotPasswordFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                } else
                    Log.d("TEST", "invalid code. print something later");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

            if (theString.length() > 0) {
                code = generateCode(theString);
                user= theString;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new VerifyEmailPasswordFragment())
                        .addToBackStack(null)
                        .commit();
            }
        }

    }

    /**
     * Called when a new user is registered.
     * Sets the email and password for that user.
     * @param theUser the user email.
     * @param thePassword the user password.
     */
    public static void setUserandPassword(String theUser, String thePassword){
        user = theUser;
        password = thePassword;
    }

    /**
     * Basic getter for user email.
     * @return the user's email.
     */
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
}