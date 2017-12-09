package group12.tcss450.uw.edu.appproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import group12.tcss450.uw.edu.appproject.database.DBManager;
import group12.tcss450.uw.edu.appproject.activities.MainActivity;
import group12.tcss450.uw.edu.appproject.R;

/**
 * Functionality for when a user forgets their password.
 */
public class ForgotPasswordFragment extends Fragment implements View.OnClickListener{
    /** Listener to send back user data. */
    private OnFragmentInteractionListener mListener;

    /** Database object used to check for and edit users. */
    private DBManager db;

    /** The password field. */
    private EditText pword;

    /** The re-entered password field. */
    private EditText rePword;

    /** Display area for error messages. */
    private TextView mErrorText;
    
    /**
    * Required constructor.
    */
    public ForgotPasswordFragment() { }

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
        View v = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        Button b = (Button) v.findViewById(R.id.changePButton);
        b.setOnClickListener(this);
        db = new DBManager();
        pword = v.findViewById(R.id.newPWText);
        rePword = v.findViewById(R.id.reNewPW);
        mErrorText = v.findViewById(R.id.errorTextView);
        return v;
    }

    /**
     * Adds functionality to buttons.
     * @param view the view of the fragment.
     */
    @Override
    public void onClick(View view) {
        if (mListener != null) {
            if(pword.getText().toString().equals(rePword.getText().toString())){
                if(pword.getText().toString().length() > 5){
                    try{
                        db.editUser(MainActivity.getUserName(), pword.getText().toString());
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                    mListener.onFragmentInteraction(getString(R.string.forgot_password_button));
                } else {
                    mErrorText.setText(R.string.password_length_error);
                    mErrorText.setVisibility(TextView.VISIBLE);
                }
            } else {
                mErrorText.setText(R.string.password_match_error);
                mErrorText.setVisibility(TextView.VISIBLE);
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
