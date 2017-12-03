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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import static android.content.ContentValues.TAG;

/**
 * Handles the connection to a database, and methods to query it.
 */
public class DBManager {

    //the names of php scripts to execute various functions.

    /**
     * The script that verifies whether or not a set of credentials is valid.
     */
    private static final String VERIFY = "check";

    /**
     * The script that adds a set of credentials to the database.
     */
    private static final String ADD = "addnew";

    /**
     * The script that changes the password of a given user to a new value.
     */
    private static final String CHANGE = "pwupdater";

    /**
     * The script that gets ingredients that match a query.
     */
    private static final String INGRED_SEARCH_ALL = "getallingredient";

    /**
     * The script that gets ingredients that match a query.
     */
    private static final String INGRED_SEARCH = "getingredient";

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
        AsyncTask<String, Void, String> task = new DBQuery();
        String response;
        if (strings.length == 1)
            response = task.execute(strings[0], VERIFY).get();
        else if (strings.length == 2)
            response = task.execute(strings[0], strings[1], VERIFY).get();
        else
            return false; //invalid input
        Log.d(TAG, "validCredentials: success");
        return response.equals("success");
    }

    /**
     * Adds the user to the database. This does NOT check if the user already exists!
     * @param user The email of the user.
     * @param pass The password of the user.
     * @return True if the user was successfully added.
     */
    public boolean addNewUser(String user, String pass) throws ExecutionException, InterruptedException {
        AsyncTask<String, Void, String> task = new DBQuery();
        String response = task.execute(user, pass, ADD).get();
        Log.d(TAG, "addNewUser: success");
        return response.equals("success");
    }
    /**
     * Edits a user in the db, changing their password to the given value.
     * @param user The email of the user.
     * @param newPass The proposed new password.
     * @return True If the user's password was changed successfully.
     */
    public boolean editUser(String user, String newPass) throws ExecutionException, InterruptedException {
        AsyncTask<String, Void, String> task = new DBQuery();
        String response = task.execute(user, newPass, CHANGE).get();
        Log.d(TAG, "editUser: success");
        return response.equals("success");
    }

    /**
     * Returns an array of all ingredients in the database.
     * @return A list of the ingredients.
     */
    public static String[] getIngredients() throws ExecutionException, InterruptedException {
        AsyncTask<String, Void, String> task = new DBGet();

        String[] response = task.execute(INGRED_SEARCH_ALL).get().split("#");
        return response;
    }

    /**
     * Returns an array of ingredients that match the given query.
     * @param query The search term.
     * @return A list of the ingredients.
     */
    public static String[] getIngredients(String query) throws ExecutionException, InterruptedException {
        AsyncTask<String, Void, String> task = new DBGet();
        query = query.toLowerCase().trim();

        String[] response = task.execute(query, INGRED_SEARCH).get().split("#");
        return response;
    }

    /**
     * Executes a query on the database.
     * Requires 2+ arguments - 1 for the php file to use, and then 1+ parameters.
     * Argument order is args1... argsN, php file.
     */
    private class DBQuery extends AsyncTask<String, Void, String> {

        /**
         * Executes the query on the db.
         * @param strings The credentials to check (2 required).
         * @return Varies, depending on the php script sent.
         */
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length < 2) {
                throw new IllegalArgumentException("Need 2+ arguments for DBQuery!");
            }
            String response = "";
            String args = "";
            HttpURLConnection urlConnection = null;
            String url = "http://cssgate.insttech.washington.edu/~stanhu/" + strings[strings.length - 1] + ".php";
            if (strings.length == 2)
                args = "?name=" + strings[0];
            else if (strings.length == 3)
                args = "?name=" + strings[0]+"&pass="+strings[1].replaceAll(" ", "%20");
            Log.d("QUERY", url + args);
            try {
                java.net.URL urlObject = new URL(url + args);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
                Log.d("CHECKER", response);
                return response;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                if (strings[strings.length - 1].equals(VERIFY)) {
                   return response.toLowerCase();
                }
                if (response.length() > 0 && !response.contains("error")) {
                    Log.d("CHECKER", "success");
                    return "success";
                } else {
                    Log.d("CHECKER", "fail");
                    return "fail";
                }
            }
        }
    }

    /**
     * Executes a query on the database, returning the result.
     * Requires 1+ arguments - 1 for the php file to use, and then 0+ parameters.
     * Argument order is args1... argsN, php file.
     */
    private static class DBGet extends AsyncTask<String, Void, String> {

        /**
         * Executes the query on the db.
         * @param strings The parameters.
         * @return Varies, depending on the php script sent.
         */
        @Override
        protected String doInBackground(String... strings) {
            String response = "";
            String args = "";
            HttpURLConnection urlConnection = null;
            String url = "http://cssgate.insttech.washington.edu/~stanhu/" + strings[strings.length - 1] + ".php";
            if (strings.length == 2)
                args = "?name=" + strings[0];
            else if (strings.length == 3)
                args = "?name=" + strings[0]+"&pass="+strings[1].replaceAll(" ", "%20");
            Log.d("QUERY", url + args);
            try {
                java.net.URL urlObject = new URL(url + args);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                InputStream content = urlConnection.getInputStream();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {
                response = "Unable to connect, Reason: "
                        + e.getMessage();
                Log.d("CHECKER", response);
                return response;
            } finally {
                if (urlConnection != null)
                    urlConnection.disconnect();
                return response;
            }
        }
    }
}
