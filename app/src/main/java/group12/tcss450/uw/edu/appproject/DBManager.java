package group12.tcss450.uw.edu.appproject;

import android.os.AsyncTask;
import android.util.Log;

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
public class DBManager extends AsyncTask<String, Void, String> {

    private static final Properties properties;

    private static final String URL = "jdbc:mariadb://cssgate.insttech.washington.edu/stanhu";

    private static final String DB = "stanhu";

    private static final String USER = "stanhu";

    private static final String PASS = "BuvCuet";

    static {
        properties = new Properties();
        properties.put("user", USER);
        properties.put("password", PASS);
    }

    public static Connection connection = null;

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
        AsyncTask<String, Void, String> task = new DBAdder();
        String response = task.execute(user, pass).get();
        return response.equals("success");
    }

    /**
     * Returns a list of users.
     * @return A list of users.
     */
    public static synchronized String getAllUsers() {
        String query = "SELECT * FROM `User`";
        Statement statement = null;
        final StringBuilder sb = new StringBuilder();
        sb.append("username | pw\n");
        try {
            statement = connection.createStatement();
            ResultSet results = statement.executeQuery(query);
            while (results.next()) {
                String user = results.getString(1);
                String pw = results.getString(2);
                sb.append(user);
                sb.append(" ");
                sb.append(pw);
                sb.append("\n");
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e);
            return ""; //error
        }
        return sb.toString();
    }

    /**
     * Connects to the database.
     * @param strings The arguments, which are irrelevant to this method.
     * @return "success" if the database was successfully connected to.
     */
    @Override
    protected String doInBackground(String... strings) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(URL, properties);
            System.out.println("Successfully connected to the database.");
            return "success";
        } catch (Exception e) {
            System.out.println("Error connecting to the database: "+ e);
            return "failure";
        }
    }
    /**
     * Adds a set of credentials to the DB.
     */
    private class DBAdder extends AsyncTask<String, Void, String> {

        /**
         * Adds the given set of credentials to the DB.
         * @param strings The credentials to check (2 required).
         * @return "success" if the add was successful.
         */
        @Override
        protected String doInBackground(String... strings) {
            if (strings.length < 2) {
                throw new IllegalArgumentException("Need 2 arguments for DBAdder!");
            }
            String query = "INSERT INTO `User` VALUES (?, ?)";
            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, strings[0]);
                statement.setString(2, strings[1]);
                ResultSet results = statement.executeQuery();
                return "success";
            } catch (SQLException e) {
                System.out.println("Login error: " + e);
                return "fail"; //error
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
            String query = "SELECT * FROM `User` WHERE username = ?";
            PreparedStatement statement;
            try {
                if (strings.length >= 2) {
                    query = ("SELECT * FROM `User` WHERE username = ? AND pwd = ?");
                    statement = connection.prepareStatement(query);
                    statement.setString(1, strings[0]);
                    statement.setString(2, strings[1]);
                } else {
                    statement = connection.prepareStatement(query);
                    statement.setString(1, strings[0]);
                }
                ResultSet results = statement.executeQuery();
                while (results.next()) {
                    return "found";
                }
                return "not found";
            } catch (SQLException e) {
                System.out.println("Login error: " + e);
                return "error"; //error
            }
        }
    }
}
