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
 * A simple {@link Fragment} subclass.
 * Helper class to verify user input of email and password.
 */
public class VerifyEmailPasswordFragment extends Fragment implements View.OnClickListener {

    private EditText verify;
    private OnFragmentInteractionListener mListener;

    /**
     * Required constructor.
     */
    public VerifyEmailPasswordFragment() {
        // Required empty public constructor
    }

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
        if (mListener != null){
            mListener.onFragmentInteraction("verify:"+verify.getText().toString());
        }

    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String theString);
    }
}