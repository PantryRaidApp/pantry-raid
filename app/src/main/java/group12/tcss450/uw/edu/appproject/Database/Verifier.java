package group12.tcss450.uw.edu.appproject.Database;

import android.os.AsyncTask;
import android.util.Log;

import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Handles the sending of email codes for verification purposes.
 */
public class Verifier extends AsyncTask<String, Void, String> {
    /**
     * Sends an email with a random 5-digit code.
     * @param strings The first string is the email to send the code to.
     * @return The code that was sent to the user.
     */
    @Override
    protected String doInBackground(String... strings) {
        if (strings.length < 1) {
            throw new IllegalArgumentException("Not enough arguments given to Verifier!");
        }
        try {
            int code = (int)(Math.random() * 89999) + 10000;
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
