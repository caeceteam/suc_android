package com.example.suc.suc_android_solution;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.suc.suc_android_solution.Adapters.DinersAdapter;
import com.example.suc.suc_android_solution.Enumerations.AuthConfig;
import com.example.suc.suc_android_solution.Maps.MapMarkerViewModel;
import com.example.suc.suc_android_solution.Models.Diner;
import com.example.suc.suc_android_solution.Models.Diners;
import com.example.suc.suc_android_solution.Models.UserDiners;
import com.example.suc.suc_android_solution.Services.DinerService;
import com.example.suc.suc_android_solution.Tasks.GetAllDinersTask;
import com.example.suc.suc_android_solution.Tasks.GetUserDinersTask;
import com.example.suc.suc_android_solution.Tasks.TaskListener;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DinersListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DinersListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DinersListFragment extends Fragment {
    private static final String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    private static final String ARG_LAST_TITLE = "LAST_TITLE";
    private static final String ARG_VIEW_TYPE = "VIEW_TYPE";

    private static final Integer REQUEST_FILTER = 0;

    public static final String VIEW_TYPE_LIST = "dinersList";
    public static final String VIEW_TYPE_FILTER = "dinersListFilter";

    private String mAccountName;
    private String lastActivityTitle;
    private String viewType;

    private OnFragmentInteractionListener mListener;
    private AccountManager accountManager;

    private RecyclerView rvDiners;
    private DinersAdapter dinersAdapter;
    private FrameLayout rvContainer;
    private View mProgressView;

    private String dinerNameFilter;

    private UserDiners userDiners;


    public DinersListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param accountName nombre de usuario.
     * @param lastTitle   titulo de la actividad/fragmento del que venimos
     * @param viewType    define en que modo se usara este fragmento. si como lista o filtro.
     * @return A new instance of fragment DinersListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DinersListFragment newInstance(String accountName, String lastTitle, String viewType) {
        DinersListFragment fragment = new DinersListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACCOUNT_NAME, accountName);
        args.putString(ARG_LAST_TITLE, lastTitle);
        args.putString(ARG_VIEW_TYPE, viewType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAccountName = getArguments().getString(ARG_ACCOUNT_NAME);
            lastActivityTitle = getArguments().getString(ARG_LAST_TITLE);
            viewType = getArguments().getString(ARG_VIEW_TYPE);
        }
        accountManager = AccountManager.get(getContext());
        Activity activity = getActivity();

        final GetUserDinersTask getUserDinersTask = new GetUserDinersTask(getContext());
        getUserDinersTask.setTaskListener(new TaskListener() {
            @Override
            public void onComplete(Object response) {

                userDiners = (UserDiners) response;
            }

            @Override
            public void onMarkersReady(ArrayList<MapMarkerViewModel> markers) {

            }
        });

        getUserDinersTask.execute();

        if(viewType == VIEW_TYPE_FILTER){
            activity.setTitle(R.string.title_diner_list_filter);
        }else{
            activity.setTitle(R.string.title_diner_list);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.filter, menu);  // Use filter.xml from step 1
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter) {
            Intent filterActivity = new Intent(getContext(), DinerFilterActivity.class);
            filterActivity.putExtra("dinerName", dinerNameFilter);
            startActivityForResult(filterActivity, REQUEST_FILTER);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_diners_list, container, false);

        rvDiners = (RecyclerView) view.findViewById(R.id.recyclerview_diners);
        rvContainer = (FrameLayout) view.findViewById(R.id.rv_diners_container);
        mProgressView = (View) getActivity().findViewById(R.id.loading_progress);
        showProgress(true);


        final GetAllDinersTask getAllDinersTask = new GetAllDinersTask(getContext());
        initializeGetAllDinersTask(getAllDinersTask);
        getAllDinersTask.execute("0", dinerNameFilter);

        return view;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            rvContainer.setVisibility(show ? View.GONE : View.VISIBLE);
            rvContainer.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    rvContainer.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            rvContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void initializeGetAllDinersTask(final GetAllDinersTask getAllDinersTask) {
        final int[] page = {0};
        final int[] previousTotal = {0};
        final boolean[] loading = {true};
        final int visibleThreshold = 10;
        final int[] firstVisibleItem = new int[1];
        final int[] visibleItemCount = new int[1];
        final int[] totalItemCount = new int[1];

        getAllDinersTask.setTaskListener(new TaskListener() {
            @Override
            public void onComplete(Object response) {
                if (response instanceof Diners) {
                    List<Diner> diners = ((Diners) response).getDiners();

                    if (diners != null && diners.size() > 0) {
                        dinersAdapter = new DinersAdapter(diners, getContext(), new DinersAdapter.DinersAdapterOnClickHandler() {
                            @Override
                            public void onClick(BigInteger idDiner) {
                                Account[] accounts = accountManager.getAccountsByType(AuthConfig.KEY_ACCOUNT_TYPE.getConfig());

                                if (viewType == VIEW_TYPE_LIST) {
                                    goToDinerDetailsFragment(accounts[0], idDiner);
                                } else if (viewType == VIEW_TYPE_FILTER) {
                                    goToDonateFragment(accounts[0], idDiner);
                                }
                            }
                        }, userDiners);

                    /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
                        final LinearLayoutManager layoutManager =
                                new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);

                        layoutManager.setAutoMeasureEnabled(true);
                    /* setLayoutManager associates the LayoutManager we created above with our RecyclerView */
                        rvDiners.setLayoutManager(layoutManager);

                        rvDiners.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);

                                visibleItemCount[0] = rvDiners.getChildCount();
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
                                    GetAllDinersTask getAllDinersTaskNested = new GetAllDinersTask(getContext());
                                    getAllDinersTaskNested.setTaskListener(new TaskListener() {
                                        @Override
                                        public void onComplete(Object response) {
                                            List<Diner> diners = ((Diners) response).getDiners();
                                            dinersAdapter.add(diners);
                                            dinersAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onMarkersReady(ArrayList<MapMarkerViewModel> markers) {

                                        }
                                    });

                                    getAllDinersTaskNested.execute(String.valueOf(page[0]), dinerNameFilter);

                                    // Do something

                                    loading[0] = true;
                                }
                            }
                        });

                        //rvDiners.setNestedScrollingEnabled(false);
                    /*
                     * Use this setting to improve performance if you know that changes in content do not
                     * change the child layout size in the RecyclerView
                     */
                        rvDiners.setHasFixedSize(true);
                        rvDiners.setAdapter(dinersAdapter);
                    } else {
                        rvContainer.removeAllViews();
                    }

                }
                showProgress(false);
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

    private void goToDinerDetailsFragment(Account account, BigInteger idDiner) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DinerDetailsFragment dinerFragment = DinerDetailsFragment.newInstance(account.name, getActivity().getTitle().toString(), idDiner.toString());
        fragmentTransaction.replace(R.id.suc_content, dinerFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    private void goToDonateFragment(Account account, BigInteger idDiner) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DonationFragment donationFragment = DonationFragment.newInstance(account.name, getActivity().getTitle().toString(), idDiner.toString());
        fragmentTransaction.replace(R.id.suc_content, donationFragment);
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        final GetAllDinersTask getAllDinersTask = new GetAllDinersTask(getContext());
        initializeGetAllDinersTask(getAllDinersTask);
        getAllDinersTask.execute("0", dinerNameFilter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(data != null){
            dinerNameFilter = data.getStringExtra("filterName") != null && data.getStringExtra("filterName").length() > 0 ?
                    data.getStringExtra("filterName") : null;


            dinersAdapter.clear();
            dinersAdapter.notifyDataSetChanged();
        }
    }
}
