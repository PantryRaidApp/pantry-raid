package group12.tcss450.uw.edu.appproject;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class VerifyEmailPassword extends Fragment implements View.OnClickListener {

    private EditText verify;
    private OnFragmentInteractionListener mListener;
    public VerifyEmailPassword() {
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
            mListener.onFragmentInteraction(verify.getText().toString());
        }

    }
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(String theString);
    }
    }
