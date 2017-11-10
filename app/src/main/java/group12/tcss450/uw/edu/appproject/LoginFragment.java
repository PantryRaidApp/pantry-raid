package group12.tcss450.uw.edu.appproject;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private EditText login, password;
    private DBManager db;
    private boolean exists;
    private TextView mErrorText;
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        db = new DBManager();
        Button b = (Button) v.findViewById(R.id.LoginPageButton);
        b.setOnClickListener(this);
        b = v.findViewById(R.id.forgotPWord);
        b.setOnClickListener(this);
        login = (EditText) v.findViewById(R.id.uName);
        password = (EditText) v.findViewById(R.id.pWord);
        mErrorText = v.findViewById(R.id.errorText);
        return v;
    }
    @Override
    public void onClick(View view) {
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.loginButton:
                    try{
                        exists = db.validCredentials(login.getText().toString(),
                                password.getText().toString());

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    if (exists) {
                     mListener.onFragmentInteraction(getString(R.string.search_button));
                    } else {
                      mErrorText.setVisibility(TextView.VISIBLE);
                        mListener.onFragmentInteraction(getString(R.string.login_button));
                    }
                break;
                case R.id.forgotPWord:
                    mListener.onFragmentInteraction(login.getText().toString());
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
