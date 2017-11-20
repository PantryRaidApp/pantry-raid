package group12.tcss450.uw.edu.appproject.Fragments;

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

import group12.tcss450.uw.edu.appproject.Database.DBManager;
import group12.tcss450.uw.edu.appproject.R;

/**
 * A simple {@link Fragment} subclass.
 * The main fragment that allows the user to pick between basic starting options:
 * Login, register, display recipes from the API.
 */
public class MainPageFragment extends Fragment implements View.OnClickListener{
    private OnFragmentInteractionListener mListener;
    private EditText email, password;
    private DBManager database;
    private boolean userExistsInDatabase;
    private TextView errorText;

    /**
     * Required constructor.
     */
    public MainPageFragment() {
        // Required empty public constructor
    }


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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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
                case R.id.registerFragmentButton:
                    mListener.onFragmentInteraction(getString(R.string.register_button));
                    break;
                case R.id.skipButton:
                    mListener.onFragmentInteraction(getString(R.string.skip_button));
                    break;
            }
        }
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String theString);
    }
}
