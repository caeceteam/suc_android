package com.example.suc.suc_android_solution;

import android.accounts.AccountManager;
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
import android.widget.FrameLayout;

import com.example.suc.suc_android_solution.Adapters.DonationsAdapter;
import com.example.suc.suc_android_solution.Maps.MapMarkerViewModel;
import com.example.suc.suc_android_solution.Models.Donation;
import com.example.suc.suc_android_solution.Models.DonationsResponse;
import com.example.suc.suc_android_solution.Services.DonationService;
import com.example.suc.suc_android_solution.Tasks.GetAllDonationsTask;
import com.example.suc.suc_android_solution.Tasks.TaskListener;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DonationsListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DonationsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DonationsListFragment extends Fragment {
    private static final String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    private static final String ARG_LAST_TITLE = "LAST_TITLE";

    private String mAccountName;
    private String lastActivityTitle;

    private DonationService donationService;

    private DonationsListFragment.OnFragmentInteractionListener mListener;
    private AccountManager accountManager;

    private RecyclerView rvDonations;
    private DonationsAdapter donationsAdapter;
    private FrameLayout rvContainer;

    public DonationsListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param accountName nombre de usuario.
     * @param lastTitle   titulo de la actividad/fragmento del que venimos
     * @return A new instance of fragment DonationsListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DonationsListFragment newInstance(String accountName, String lastTitle) {
        DonationsListFragment fragment = new DonationsListFragment();
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
        accountManager = AccountManager.get(getContext());
        Activity activity = getActivity();
        activity.setTitle(R.string.title_donations_list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donations_list, container, false);

        rvDonations = (RecyclerView) view.findViewById(R.id.recyclerview_donations);
        rvContainer = (FrameLayout) view.findViewById(R.id.rv_donations_container);


        final GetAllDonationsTask getAllDonationsTask = new GetAllDonationsTask(getContext());
        initializeGetAllDonationsTask(getAllDonationsTask);
        getAllDonationsTask.execute("0");

        return view;
    }

    private void initializeGetAllDonationsTask(final GetAllDonationsTask getAllDonationsTask) {
        final int[] page = {0};
        final int[] previousTotal = {0};
        final boolean[] loading = {true};
        final int visibleThreshold = 10;
        final int[] firstVisibleItem = new int[1];
        final int[] visibleItemCount = new int[1];
        final int[] totalItemCount = new int[1];

        getAllDonationsTask.setTaskListener(new TaskListener() {
            @Override
            public void onComplete(Object response) {
                if (response instanceof DonationsResponse) {
                    final List<Donation> donations = ((DonationsResponse) response).getDonations();

                    if (donations != null && donations.size() > 0) {
                        donationsAdapter = new DonationsAdapter(donations, getContext(), new DonationsAdapter.DonationsAdapterOnClickHandler() {
                            @Override
                            public void onClick(BigInteger idDonation) {
                                //not implemented
                            }
                        });

                    /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
                        final LinearLayoutManager layoutManager =
                                new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                        layoutManager.setAutoMeasureEnabled(true);
                    /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
                        rvDonations.setLayoutManager(layoutManager);

                        rvDonations.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);

                                visibleItemCount[0] = rvDonations.getChildCount();
                                totalItemCount[0] = layoutManager.getItemCount();
                                firstVisibleItem[0] = layoutManager.findFirstVisibleItemPosition();

                                if (loading[0]) {
                                    if (totalItemCount[0] > previousTotal[0]) {
                                        loading[0] = false;
                                        previousTotal[0] = totalItemCount[0];
                                    }
                                }
                                if (!loading[0] && (totalItemCount[0] - visibleItemCount[0])
                                        <= (firstVisibleItem[0] + visibleThreshold)) {
                                    // End has been reached
                                    page[0] = page[0] + 1;
                                    GetAllDonationsTask getAllDonationsTaskNested = new GetAllDonationsTask(getContext());
                                    getAllDonationsTaskNested.setTaskListener(new TaskListener() {
                                        @Override
                                        public void onComplete(Object response) {
                                            List<Donation> donationsNested = ((DonationsResponse) response).getDonations();
                                            donationsAdapter.add(donationsNested);
                                            donationsAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onMarkersReady(ArrayList<MapMarkerViewModel> markers) {

                                        }
                                    });

                                    getAllDonationsTaskNested.execute(String.valueOf(page[0]));

                                    // Do something

                                    loading[0] = true;
                                }
                            }
                        });

                    /*
                     * Use this setting to improve performance if you know that changes in content do not
                     * change the child layout size in the RecyclerView
                     */
                        rvDonations.setHasFixedSize(true);
                        rvDonations.setAdapter(donationsAdapter);
                    } else {
                        rvContainer.removeAllViews();
                    }

                }
            }

            @Override
            public void onMarkersReady(ArrayList<MapMarkerViewModel> markers) {

            }
        });
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
}
