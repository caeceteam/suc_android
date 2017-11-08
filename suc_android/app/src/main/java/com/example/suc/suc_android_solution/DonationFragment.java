package com.example.suc.suc_android_solution;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DonationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DonationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonationFragment extends Fragment {

    private static final String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    private static final String ARG_LAST_TITLE = "LAST_TITLE";
    private OnFragmentInteractionListener mListener;

    private String mAccountName;
    private String lastActivityTitle;

    public DonationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param accountName nombre del usuario.
     * @param lastTitle página de donde vino.
     * @return A new instance of fragment DonationFragment.
     */

    public static DonationFragment newInstance(String accountName, String lastTitle) {
        DonationFragment fragment = new DonationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACCOUNT_NAME, accountName);
        args.putString(ARG_LAST_TITLE, lastTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAccountName = getArguments().getString(ARG_ACCOUNT_NAME);
            lastActivityTitle = getArguments().getString(ARG_LAST_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_donation, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
