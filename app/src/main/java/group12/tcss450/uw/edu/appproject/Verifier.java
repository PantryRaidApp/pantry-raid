package group12.tcss450.uw.edu.appproject;

import android.os.AsyncTask;
import android.util.Log;

import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Admin on 11/9/2017.
 */

public class Verifier extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... strings) {
        if (strings.length < 1) {
            throw new IllegalArgumentException("Not enough arguments given to Verifier!");
        }
        try {
            int code = (int)(Math.random() * 99999);
            URL url = new URL("http://cssgate.insttech.washington.edu/~stanhu/verify.php");
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            PrintStream stream = new PrintStream(connection.getOutputStream());
            stream.print("name=" + strings[0]);
            stream.print("&code=" + code);

            connection.getInputStream();
            stream.close();
            Log.d("TEST", "Code is "+code);
            return Integer.toString(code);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
