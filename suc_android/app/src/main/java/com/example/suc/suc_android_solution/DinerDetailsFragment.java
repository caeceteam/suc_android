package com.example.suc.suc_android_solution;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.suc.suc_android_solution.Maps.MapMarkerViewModel;
import com.example.suc.suc_android_solution.Models.Diner;
import com.example.suc.suc_android_solution.Models.DinerRequest;
import com.example.suc.suc_android_solution.Models.DinerResponse;
import com.example.suc.suc_android_solution.Models.UserDiner;
import com.example.suc.suc_android_solution.Models.UserDiners;
import com.example.suc.suc_android_solution.Services.DinerService;
import com.example.suc.suc_android_solution.Tasks.GetDinerTask;
import com.example.suc.suc_android_solution.Tasks.GetUserDinersTask;
import com.example.suc.suc_android_solution.Tasks.TaskListener;
import com.example.suc.suc_android_solution.Tasks.UserDinerFollowTask;
import com.example.suc.suc_android_solution.Tasks.UserDinerUnFollowTask;
import com.example.suc.suc_android_solution.Adapters.DinerRequestAdapter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DinerDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DinerDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DinerDetailsFragment extends Fragment {

    private static final String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    private static final String ARG_DINER_ID = "DINER_ID";
    private static final String ARG_LAST_TITLE = "LAST_TITLE";

    private String mAccountName;
    private String lastActivityTitle;

    private DinerService dinerService;

    private TextView tvName;
    private TextView tvLink;
    private TextView tvMail;
    private TextView tvPhone;
    private TextView tvAddress;

    private Button followButton;
    private OnFragmentInteractionListener mListener;

    private UserDiner userDinerRelationship;

    private BigInteger idDiner;

    private RecyclerView rvDinerRequest;
    private DinerRequestAdapter dinerRequestAdapter;
    private LinearLayout rvContainer;

    public DinerDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param accountName nombre de usuario.
     * @param lastTitle   titulo de la actividad/fragmento del que venimos
     * @return A new instance of fragment MyAccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DinerDetailsFragment newInstance(String accountName, String lastTitle, String idDiner) {
        DinerDetailsFragment fragment = new DinerDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACCOUNT_NAME, accountName);
        args.putString(ARG_LAST_TITLE, lastTitle);
        args.putString(ARG_DINER_ID, idDiner);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAccountName = getArguments().getString(ARG_ACCOUNT_NAME);
            lastActivityTitle = getArguments().getString(ARG_LAST_TITLE);
            idDiner = new BigInteger(getArguments().getString(ARG_DINER_ID));
        }

        Activity activity = getActivity();
        activity.setTitle(R.string.title_diner_details);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_diner_details, container, false);

        tvName = (TextView) view.findViewById(R.id.diner_detail_name);
        tvLink = (TextView) view.findViewById(R.id.diner_detail_link);
        tvMail = (TextView) view.findViewById(R.id.diner_detail_mail);
        tvPhone = (TextView) view.findViewById(R.id.diner_detail_phone);
        tvAddress = (TextView) view.findViewById(R.id.diner_address);
        rvDinerRequest = (RecyclerView) view.findViewById(R.id.recyclerview_dinerRequest);
        rvContainer = (LinearLayout) view.findViewById(R.id.rv_dinerRequest_container);

        followButton = (Button) view.findViewById(R.id.diner_follow_button);
        followButton.setText(R.string.map_item_follow);

        followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userDinerRelationship != null) {
                    UserDinerUnFollowTask userDinerUnFollowTask = new UserDinerUnFollowTask(getContext());
                    userDinerUnFollowTask.setTaskListener(new TaskListener() {
                        @Override
                        public void onComplete(Object response) {
                            if (response instanceof Boolean) {
                                if ((Boolean) response == Boolean.TRUE) {
                                    userDinerRelationship = null;
                                    followButton.setText(R.string.map_item_follow);
                                }
                            }
                        }

                        @Override
                        public void onMarkersReady(ArrayList<MapMarkerViewModel> markers) {

                        }
                    });

                    userDinerUnFollowTask.execute(idDiner.toString());
                } else {
                    UserDinerFollowTask userDinerFollowTask = new UserDinerFollowTask(getContext());
                    userDinerFollowTask.setTaskListener(new TaskListener() {
                        @Override
                        public void onComplete(Object response) {
                            if (response instanceof UserDiner) {
                                if (((UserDiner) response).getIdDiner() != null) {
                                    userDinerRelationship = (UserDiner) response;
                                    followButton.setText(R.string.map_item_unfollow);
                                }
                            }
                        }

                        @Override
                        public void onMarkersReady(ArrayList<MapMarkerViewModel> markers) {

                        }
                    });
                    userDinerFollowTask.execute(idDiner.toString());
                }
            }
        });

        GetDinerTask getDinerTask = new GetDinerTask(getContext());
        getDinerTask.setTaskListener(new TaskListener() {
            @Override
            public void onComplete(Object response) {
                if (response instanceof Diner) {
                    Diner diner = (Diner) response;
                    tvLink.setText(diner.getLink());
                    tvMail.setText(String.format("Mail: %s", diner.getMail()));
                    tvName.setText(diner.getName());
                    tvPhone.setText(String.format("Telefono: %s", diner.getPhone()));
                    tvAddress.setText(buildDinerAddress(diner));

                    if(diner.requests != null && diner.requests.size() > 0){
                        dinerRequestAdapter = new DinerRequestAdapter(diner.requests, getContext(), new DinerRequestAdapter.DinerRequestAdapterOnClickHandler() {
                            @Override
                            public void onClick(BigInteger idDinerRequest) {
                                //not implemented yet.
                            }
                        });

                    /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
                        LinearLayoutManager layoutManager =
                                new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                        layoutManager.setAutoMeasureEnabled(true);
                    /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
                        rvDinerRequest.setLayoutManager(layoutManager);
                        rvDinerRequest.setNestedScrollingEnabled(false);
                    /*
                     * Use this setting to improve performance if you know that changes in content do not
                     * change the child layout size in the RecyclerView
                     */
                        rvDinerRequest.setHasFixedSize(true);
                        rvDinerRequest.setAdapter(dinerRequestAdapter);
                    }else{
                        rvContainer.removeAllViews();
                    }

                }
            }

            @Override
            public void onMarkersReady(ArrayList<MapMarkerViewModel> markers) {

            }
        });
        getDinerTask.execute(idDiner.toString());

        GetUserDinersTask getUserDinersTask = new GetUserDinersTask(getContext());
        getUserDinersTask.setTaskListener(new TaskListener() {
            @Override
            public void onComplete(Object response) {
                if (response instanceof UserDiners) {
                    Collection<UserDiner> userDiners = ((UserDiners) response).getUsersDiners();
                    if (userDiners != null && userDiners.size() > 0) {
                        for (UserDiner userDiner : userDiners) {
                            if (userDiner.getIdDiner().equals(idDiner)) {
                                userDinerRelationship = userDiner;
                                followButton.setText(R.string.map_item_unfollow);
                                break;
                            }
                        }
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

    private String buildDinerAddress(Diner diner) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("Dirección: %s %d", diner.getStreet(),
                diner.getStreetNumber()));

        if (diner.getDoor() != null && !diner.getDoor().equals("")) {
            builder.append(String.format(" Puerta: %s", diner.getDoor()));
        }

        if (diner.getFloor() != null && !diner.getFloor().equals("")) {
            builder.append(String.format(" Piso: %s", diner.getFloor()));
        }
        builder.append(String.format(" - CP: %s", diner.getZipcode()));

        return builder.toString();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
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
