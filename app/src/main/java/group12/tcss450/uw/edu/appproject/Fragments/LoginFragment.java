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
 * Provides functionality that allows users to login with a preexisting account.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private EditText login, password;
    private DBManager db;
    private boolean exists;
    private TextView mErrorText;

    /**
     * A required empty constructor.
     */
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        db = new DBManager();
        Button b = v.findViewById(R.id.LoginPageButton);
        b.setOnClickListener(this);
        b = v.findViewById(R.id.forgotPWord);
        b.setOnClickListener(this);
        login = v.findViewById(R.id.uName);
        password = v.findViewById(R.id.pWord);
        mErrorText = v.findViewById(R.id.errorText);

        return v;
    }
    @Override
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.LoginPageButton:
                    try{
                        exists = db.validCredentials((login.getText().toString())
                                        .trim().toLowerCase(),
                                password.getText().toString());
                        Log.d("TEST", Boolean.toString(exists));

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    if (exists) {
                     mListener.onFragmentInteraction(getString(R.string.skip_button));
                    } else {
                        mErrorText.setText(R.string.invalid_username_password);
                        mErrorText.setVisibility(TextView.VISIBLE);
                        //mListener.onFragmentInteraction(getString(R.string.login_button));
                    }
                break;
                case R.id.forgotPWord:
                    try{
                        exists = db.validCredentials((login.getText().toString())
                                .trim().toLowerCase());
                        Log.d("TEST", Boolean.toString(exists));

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    if (exists)
                        mListener.onFragmentInteraction((login.getText().toString())
                                .trim().toLowerCase());
                    else {
                        mErrorText.setText(R.string.forgot_password_no_email);
                        mErrorText.setVisibility(TextView.VISIBLE);
                    }
                    break;
            }
        }
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

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String theString);
    }
}