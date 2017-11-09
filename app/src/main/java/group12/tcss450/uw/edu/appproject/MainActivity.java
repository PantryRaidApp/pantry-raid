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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBManager();
        try {
            Object result = db.execute().get(); //so the async task can finish
            Log.d("TEST", Boolean.toString(db.validCredentials("eeeshe", "wow")));
            Log.d("TEST", Boolean.toString(db.validCredentials("eeessdfsdhe", "wow")));
            Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
            intent.setType("message/rfc822");

            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject of email");
            intent.putExtra(Intent.EXTRA_TEXT,    "Body of email");
            intent.setData(Uri.parse("hustanley@gmail.com")); // or just "mailto:" for blank
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
    }
}
