package com.example.suc.suc_android_solution;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.suc.suc_android_solution.Enumerations.AuthConfig;
import com.example.suc.suc_android_solution.Maps.MapMarkerViewModel;
import com.example.suc.suc_android_solution.Models.DinerRequest;
import com.example.suc.suc_android_solution.Models.UserDiner;
import com.example.suc.suc_android_solution.Models.UserDiners;
import com.example.suc.suc_android_solution.Tasks.GetUserDinersTask;
import com.example.suc.suc_android_solution.Tasks.TaskListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    private static final String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    private static final String ARG_LAST_TITLE = "LAST_TITLE";

    private String mAccountName;
    private String lastActivityTitle;
    private AccountManager accountManager;
    private Button donateButton;
    private TextView tvMainDinerRequests;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param accountName nombre del usuario
     * @param lastTitle titulo del fragmento que lo llamo antes
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String accountName, String lastTitle) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACCOUNT_NAME, accountName);
        args.putString(ARG_LAST_TITLE,lastTitle);
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

        Activity activity = getActivity();
        activity.setTitle(R.string.title_activity_start);

        accountManager = AccountManager.get(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        donateButton = (Button) view.findViewById(R.id.button_donate);
        tvMainDinerRequests = (TextView) view.findViewById(R.id.tv_main_diner_requests);

        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDonationFragment();
            }
        });

        GetUserDinersTask getUserDinersTask = new GetUserDinersTask(getContext());
        getUserDinersTask.setTaskListener(new TaskListener() {
            @Override
            public void onComplete(Object response) {
                if(response instanceof UserDiners){
                    Collection<UserDiner> userDinersCol = ((UserDiners) response).getUsersDiners();
                    List<DinerRequest> allDinerRequest = new LinkedList<DinerRequest>();
                    if(userDinersCol != null && userDinersCol.size() > 0){
                        for(UserDiner userDiner : userDinersCol){
                            allDinerRequest.addAll(userDiner.getDiner().requests);
                        }
                    }

                    if(allDinerRequest.size() > 0){
                        tvMainDinerRequests.setText(Html.fromHtml(String.format("Los comedores que estas siguiendo han generado <b>%d</b> solicitudes de ayuda.", allDinerRequest.size())));
                        tvMainDinerRequests.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onMarkersReady(ArrayList<MapMarkerViewModel> markers) {

            }
        });

        getUserDinersTask.execute();

        return view;
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
    public void onDestroy() {
        super.onDestroy();
        getActivity().setTitle(lastActivityTitle);
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

    private void goToDonationFragment() {
        Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DinersListFragment donationFragment = DinersListFragment.newInstance(accounts[0].name, getActivity().getTitle().toString(), DinersListFragment.VIEW_TYPE_FILTER);
        fragmentTransaction.replace(R.id.suc_content, donationFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }
}
