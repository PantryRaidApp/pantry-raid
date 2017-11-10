package group12.tcss450.uw.edu.appproject;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class chooseFragment extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    public chooseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose, container, false);
        Button b = (Button) v.findViewById(R.id.loginButton);
        b.setOnClickListener(this);
        b = (Button) v.findViewById(R.id.regButton);
        b.setOnClickListener(this);
        b= (Button) v.findViewById(R.id.searchButton);
        b.setOnClickListener(this);
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
        if (mListener != null) {
            switch (view.getId()) {
                case R.id.loginButton:
                    mListener.onFragmentInteraction(getString(R.string.login_button));
                    break;
                case R.id.regButton:
                    mListener.onFragmentInteraction(getString(R.string.register_button));
                    break;
                case R.id.searchButton:
                    mListener.onFragmentInteraction(getString(R.string.search_button));
                    break;
            }
        }
    }
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(String theString);
    }
}
