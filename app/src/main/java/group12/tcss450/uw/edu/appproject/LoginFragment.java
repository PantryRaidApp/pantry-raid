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
public class LoginFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    EditText login, password;
    DBManager db = new DBManager();
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        Button b = (Button) v.findViewById(R.id.LoginPageButton);
        b.setOnClickListener(this);
        login = (EditText) v.findViewById(R.id.uName);
        password = (EditText) v.findViewById(R.id.pWord);
        return v;
    }
    @Override
    public void onClick(View view) {
        if (mListener != null) {


        }
    }
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(String theString);
    }

}
