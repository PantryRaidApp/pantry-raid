package group12.tcss450.uw.edu.appproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import group12.tcss450.uw.edu.appproject.database.DBManager;
import group12.tcss450.uw.edu.appproject.activities.MainActivity;
import group12.tcss450.uw.edu.appproject.R;

/**
 * Provides functionality for a user to create an account.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener{
    /** Listener to send back user data. */
    private OnFragmentInteractionListener mListener;

    /** Input for email, password, and re-entered password. */
    private EditText email, password, passwordreenter;

    /** Database object used to edit user data. */
    private DBManager database;

    /** Display area for error messages. */
    private TextView errorMessage;

    /** Regex for well formed emails. */
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9.%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Required constructor.
     */
    public RegisterFragment() { }

    /**
     * Method to validate an email
     * @param emailStr the entered email at a string.
     * @return if the email is valid.
     */
    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    /**
     * Sets up fragment for displaying the fragment.
     * @param inflater LayoutInflater
     * @param container ViewGroup
     * @param savedInstanceState Bundle
     * @return the created view.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        Button b = v.findViewById(R.id.registerButton);
        b.setOnClickListener(this);
        email = v.findViewById(R.id.emailRegisterEditText);
        password = v.findViewById(R.id.passwordRegisterEditText);
        passwordreenter = v.findViewById(R.id.reenterPasswordRegisterEditText);
        errorMessage = v.findViewById(R.id.errorRegisterTextView);
        database = new DBManager();
        return v;
    }

    /**
     * Adds functionality to buttons.
     * @param view the view of the fragment.
     */
    @Override
    public void onClick(View view) {
        boolean exists = false;
        if (mListener != null) {
            try {
                exists = database.validCredentials((email.getText().toString()).trim().toLowerCase());

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!validate((email.getText().toString()).trim().toLowerCase())) {
                errorMessage.setText(R.string.invalid_email);
                errorMessage.setVisibility(TextView.VISIBLE);
            } else if (exists) {
                errorMessage.setText(R.string.user_exists);
                errorMessage.setVisibility(TextView.VISIBLE);
            } else {
                if (password.getText().toString().length() < 6) {
                    errorMessage.setText(R.string.password_length_error);
                    errorMessage.setVisibility(TextView.VISIBLE);
                } else if (!password.getText().toString().equals(passwordreenter.getText().toString())) {
                    errorMessage.setText(R.string.password_match_error);
                    errorMessage.setVisibility(TextView.VISIBLE);
                } else if (password.getText().toString().equals(passwordreenter.getText().toString())) {
                    ((MainActivity)getActivity()).setUserandPassword((email.getText().toString()).trim().toLowerCase(),
                           password.getText().toString());

                        mListener.onFragmentInteraction(email.getText().toString());
                        Log.d("name", (email.getText().toString()).trim().toLowerCase());
                }
            }
        }
    }

    /**
     * Instantiation of variables when fragment is attached.
     * @param context Context.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /**
     * Cleanup when fragment is detached.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Returns a string back to MainPageActivity.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String theString);
    }
}
