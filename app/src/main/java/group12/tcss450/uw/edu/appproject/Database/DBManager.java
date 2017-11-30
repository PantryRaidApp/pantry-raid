package group12.tcss450.uw.edu.appproject.Database;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Handles the connection to a database, and methods to query it.
 */
public class DBManager {

    /**
     * Returns whether or not the given credentials are valid.
     * Can be called with 1 or 2 arguments, which are username and password
     * respectively. Used to check if a username exists, or if a username/password
     * combo is valid.
     * Returns "found" is an entry is found, "not found" if not found, and "error"
     * if an error occurs.
     * @param strings The credentials to check - up to 2, username and password.
     * @throws ExecutionException If there is an error in execution.
     * @throws InterruptedException If there is an error in execution.
     * @return Whether or not the credentials exist in the database.
     */
    public boolean validCredentials(String... strings) throws ExecutionException, InterruptedException {
        AsyncTask<String, Void, String> task = new CredentialChecker();
        String response = task.execute(strings).get();
        return response.equals("found");
    }

    /**
     * Adds the user to the database. This does NOT check if the user already exists!
     * @param user The email of the user.
     * @param pass The password of the user.
     * @return True if the user was successfully added.
     */
    public boolean addNewUser(String user, String pass) throws ExecutionException, InterruptedException {
        AsyncTask<String, Void, String> task = new DBQuery();
        String response = task.execute(user, pass, "addnew").get();
        return response.equals("success");
    }
    /**
     * Deletes the user from the db.
     * @param user The email of the user.
     * @return True If the user was deleted.
     */
    public boolean editUser(String user, String newPass) throws ExecutionException, InterruptedException {
        AsyncTask<String, Void, String> task = new DBQuery();
        String response = task.execute(user, newPass, "pwupdater").get();
        return response.equals("success");
    }

    /**
     * Executes a 2-parameter query on the database.
     * Requires 3 arguments - 1 for the php file to use, and then the 2 parameters.
     * Argument order is arg1, arg2, php file.
     */
    private class DBQuery extends AsyncTask<String, Void, String> {

        /**
         * Adds the given set of credentials to the DB.
         * @param strings The credentials to check (2 required).
         * @return "success" if the add was successful.
         */
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length < 2) {
                throw new IllegalArgumentException("Need 2 arguments for DBQuery!");
            }
            String response = "";
            String args;
            HttpURLConnection urlConnection = null;
            String url = "http://cssgate.insttech.washington.edu/~stanhu/" + strings[2] + ".php";
            args = "?name=" + strings[0]+"&pass="+strings[1].replaceAll(" ", "%20");
            try {
                java.net.URL urlObject = new URL(url + args);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
                Log.d("CHECKER", response);
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
                Log.d("CHECKER", response);
                return response;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                Log.d("CHECKER", "Reached the end");
                if (response.length() > 0 && !response.contains("error")) {
                    Log.d("CHECKER", "success");
                    return "found";
                } else {
                    Log.d("CHECKER", "fail");
                    return "not found";
                }
            }
        }
    }

    /**
     * Checks whether the given credentials exist.
     * Can be called with 1 or 2 arguments, which are username and password
     * respectively. Used to check if a username exists, or if a username/password
     * combo is valid.
     * Returns "found" is an entry is found, "not found" if not found, and "error"
     * if an error occurs.
     */
    private class CredentialChecker extends AsyncTask<String, Void, String> {

        /**
         * Checks for the given credentials in the DB.
         * @param strings The credentials to check - up to 2, username and password.
         * @return "found" if they were found, false otherwise.
         */
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length == 0) {
                throw new IllegalArgumentException("No arguments given to CredentialChecker!");
            }
            String response = "";
            String args;
            HttpURLConnection urlConnection = null;
            String url = "http://cssgate.insttech.washington.edu/~stanhu/check.php";
            if (strings.length == 2)
                args = "?name=" + strings[0]+"&pass="+strings[1];
            else
                args = "?name=" + strings[0];
            try {
                java.net.URL urlObject = new URL(url + args);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
                Log.d("CHECKER", response);
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
                Log.d("CHECKER", response);
                return response;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                Log.d("CHECKER", "Reached the end");
                if (response.length() > 0 && !response.contains("Fail")) {
                    Log.d("CHECKER", "Found");
                    return "found";
                } else {
                    return "not found";
                }
            }
        }
    }
}
