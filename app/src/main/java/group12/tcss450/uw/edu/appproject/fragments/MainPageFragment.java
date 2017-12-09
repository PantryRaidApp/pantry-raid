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

import group12.tcss450.uw.edu.appproject.activities.MainActivity;
import group12.tcss450.uw.edu.appproject.database.DBManager;
import group12.tcss450.uw.edu.appproject.R;

/**
 * The main fragment that allows the user to pick between basic starting options:
 * Login, register, display recipes from the API.
 */
public class MainPageFragment extends Fragment implements View.OnClickListener{
    /** Listener to send back user data. */
    private OnFragmentInteractionListener mListener;

    /** Components for entering email and password. */
    private EditText email, password;

    /** Database object used to check for saved user data. */
    private DBManager database;

    /** A check for whether or not a user already exists. */
    private boolean userExistsInDatabase;

    /** A display area for error text. */
    private TextView errorText;

    /**
     * Required constructor.
     */
    public MainPageFragment() { }

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
        database = new DBManager();
        View v = inflater.inflate(R.layout.fragment_main_page, container, false);
        Button b = v.findViewById(R.id.loginButton);
        b.setOnClickListener(this);
        b = v.findViewById(R.id.registerFragmentButton);
        b.setOnClickListener(this);
        b = v.findViewById(R.id.forgotPasswordButton);
        b.setOnClickListener(this);
        b = v.findViewById(R.id.skipButton);
        b.setOnClickListener(this);

        email = v.findViewById(R.id.emailLoginEditText);
        password = v.findViewById(R.id.passwordLoginEditText);
        errorText = v.findViewById(R.id.errorLoginTextView);

        return v;
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
     * Adds functionality to buttons.
     * @param view the view of the fragment.
     */
    @Override
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.loginButton:
                    try{
                        userExistsInDatabase = database.validCredentials((email.getText().toString())
                                        .trim().toLowerCase(),
                                password.getText().toString());
                        Log.d("TEST", Boolean.toString(userExistsInDatabase));

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    if (userExistsInDatabase) {
                        mListener.onFragmentInteraction(getString(R.string.skip_button));
                        ((MainActivity) getActivity()).setUserandPassword((email.getText().toString())
                                .trim().toLowerCase(), password.getText().toString());
                    } else {
                        errorText.setText(R.string.invalid_username_password);
                        errorText.setVisibility(TextView.VISIBLE);
                    }
                    break;
                case R.id.forgotPasswordButton:
                    try{
                        userExistsInDatabase = database.validCredentials((email.getText().toString())
                                .trim().toLowerCase());
                        Log.d("TEST", Boolean.toString(userExistsInDatabase));

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    if (userExistsInDatabase)
                        mListener.onFragmentInteraction((email.getText().toString())
                                .trim().toLowerCase());
                    else {
                        errorText.setText(R.string.forgot_password_no_email);
                        errorText.setVisibility(TextView.VISIBLE);
                    }
                    break;
                case R.id.registerFragmentButton:
                    mListener.onFragmentInteraction(getString(R.string.register_button));
                    break;
                case R.id.skipButton:
                    mListener.onFragmentInteraction(getString(R.string.skip_button));
                    break;
            }
        }
    }

    /**
     * Returns a string back to MainPageActivity.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String theString);
    }
}
