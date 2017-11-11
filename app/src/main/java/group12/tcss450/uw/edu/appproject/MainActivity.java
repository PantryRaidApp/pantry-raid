package group12.tcss450.uw.edu.appproject;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * The main activity that handles all fragment transactions into the main functionality of the app.
 */
public class MainActivity extends AppCompatActivity implements
        chooseFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener,
        VerifyEmailPassword.OnFragmentInteractionListener,
        ForgotPasswordFragment.OnFragmentInteractionListener {
    private AsyncTask<String, Integer, String> mTask;
    private DBManager db;
    String code;
    private static String user;
    private static String password;

    private Verifier verifier;

    /**
     * Generates a 5-digit code and sends it to the given email address.
     * @param email The email address to send the code to.
     * @return The 5-digit code as a string, or null if there was an error.
     */
    public String generateCode(String email) {
        try {
            verifier = new Verifier();
            String result = verifier.execute(email).get();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBManager();
        try {
            Object result = db.execute().get(); //so the async task can finish
            Log.d("TEST", Boolean.toString(db.validCredentials("eeeshe", "wow")));
            Log.d("TEST", Boolean.toString(db.validCredentials("eeessdfsdhe", "wow")));

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, new chooseFragment()).commit();
            }
        }
        
    }
    @Override
    public void onFragmentInteraction(String theString) {
        if (theString.equals(getString(R.string.login_button))){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new LoginFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (theString.equals(getString(R.string.register_button))){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (theString.equals(getString(R.string.search_button))){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new DisplayTopRecipesFragment())
                    .addToBackStack(null)
                    .commit();

        } else if (theString.equals(getString(R.string.forgot_link)) ){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new LoginFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (theString.startsWith("verify:")){
            try {
                theString = theString.split(":")[1];
                if (theString.equals(code)) {
                    if(password !=null) {
                        db.addNewUser(user, password);
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
                        .replace(R.id.fragmentContainer, new VerifyEmailPassword())
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