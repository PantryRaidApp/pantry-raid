package group12.tcss450.uw.edu.appproject.Activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import group12.tcss450.uw.edu.appproject.Database.DBManager;
import group12.tcss450.uw.edu.appproject.Database.Verifier;
import group12.tcss450.uw.edu.appproject.Fragments.VerifyEmailPasswordFragment;
import group12.tcss450.uw.edu.appproject.Fragments.ForgotPasswordFragment;
import group12.tcss450.uw.edu.appproject.Fragments.LoginFragment;
import group12.tcss450.uw.edu.appproject.Fragments.MainPageFragment;
import group12.tcss450.uw.edu.appproject.Fragments.RecipeViewFragment;
import group12.tcss450.uw.edu.appproject.Fragments.RegisterFragment;
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

        database = new DBManager();
        try {
            Object result = database.execute().get(); //so the async task can finish
            Log.d("TEST", Boolean.toString(database.validCredentials("eeeshe", "wow")));
            Log.d("TEST", Boolean.toString(database.validCredentials("eeessdfsdhe", "wow")));

        } catch (Exception e) {
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
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new RecipeViewFragment())
                    .addToBackStack(null)
                    .commit();

        } else if (theString.equals(getString(R.string.forgot_password_button)) ){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new LoginFragment())
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