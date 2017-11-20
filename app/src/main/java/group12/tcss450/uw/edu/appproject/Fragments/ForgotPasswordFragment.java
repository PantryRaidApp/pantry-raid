package group12.tcss450.uw.edu.appproject.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import group12.tcss450.uw.edu.appproject.Database.DBManager;
import group12.tcss450.uw.edu.appproject.Activities.MainActivity;
import group12.tcss450.uw.edu.appproject.R;


/**
 * A simple {@link Fragment} subclass.
 * Functionality for when a user forgets their password.
 */
public class ForgotPasswordFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private DBManager db;
    private EditText pword;
    private EditText rePword;
    private TextView mErrorText;
    
    /**
    * Required constructor.
    */
    public ForgotPasswordFragment() {
        // Required empty public constructor
    }

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
    public void onClick(View view) {
        if (mListener != null) {
            if(pword.getText().toString().equals(rePword.getText().toString())){
                if(pword.getText().toString().length() > 5){
                    try{
                        db.deleteUser(MainActivity.getUserName());
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                    try{
                        db.addNewUser(MainActivity.getUserName(), pword.getText().toString());
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String theString);
    }
}
