package group12.tcss450.uw.edu.appproject;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private DBManager db;

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
            generateCode("hustanley@gmail.com");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (savedInstanceState == null) {
            if (findViewById(R.id.fragmentContainer) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainer, new DisplayTopRecipesFragment()).commit();
            }
        }
        
    }
}
