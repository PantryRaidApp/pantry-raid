package group12.tcss450.uw.edu.appproject;


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
public class RegisterFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    EditText user, pword, checkword;
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
        return v;
    }
    @Override
    public void onClick(View view) {
        if (mListener != null) {

         if(pword.getText().toString().equals(checkword.getText().toString())){

         }

        }
    }
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(String theString);
    }
}
