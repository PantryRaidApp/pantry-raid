package group12.tcss450.uw.edu.appproject;


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


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private EditText user, pword, checkword;
    private DBManager db;
    private TextView message;
    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        Button b = (Button) v.findViewById(R.id.rButton);
        b.setOnClickListener(this);
        user = (EditText) v.findViewById(R.id.regUname);
        pword = (EditText) v.findViewById(R.id.regPW);
        checkword = (EditText) v.findViewById(R.id.rePWord);
        message = v.findViewById(R.id.eText);
        db = new DBManager();
        return v;
    }
    @Override
    public void onClick(View view) {
        boolean exists = false;
        if (mListener != null) {
            try {
                exists = db.validCredentials(user.getText().toString());

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (exists) {
                message.setVisibility(TextView.VISIBLE);
               // mListener.onFragmentInteraction(getString(R.string.register_button));
            }else{
                if (pword.getText().toString().equals(checkword.getText().toString())) {
                    boolean add = false;
                    try {
                        add = db.addNewUser(user.getText().toString(), pword.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (add) {
                        mListener.onFragmentInteraction(user.getText().toString());
                        Log.d("name", user.getText().toString());
                    }

                }
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

        void onFragmentInteraction(String theString);
    }
}
