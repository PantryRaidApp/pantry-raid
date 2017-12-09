package group12.tcss450.uw.edu.appproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import group12.tcss450.uw.edu.appproject.R;

/**
 * Helper class to verify user input of email and password.
 */
public class VerifyEmailPasswordFragment extends Fragment implements View.OnClickListener {
    /** Enter box for verification code */
    private EditText verify;

    /** Listener to send back user data. */
    private OnFragmentInteractionListener mListener;

    /**
     * Required constructor.
     */
    public VerifyEmailPasswordFragment() { }

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
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_verify_email_password, container, false);
        Button b = v.findViewById(R.id.verButton);
        b.setOnClickListener(this);
        verify = v.findViewById(R.id.verCode);
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
        if (mListener != null){
            mListener.onFragmentInteraction("verify:"+verify.getText().toString());
        }

    }

    /**
     * Returns a string back to MainPageActivity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String theString);
    }
}