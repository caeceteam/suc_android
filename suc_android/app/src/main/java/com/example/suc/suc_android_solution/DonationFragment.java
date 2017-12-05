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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.suc.suc_android_solution.Enumerations.AuthConfig;
import com.example.suc.suc_android_solution.Maps.MapMarkerViewModel;
import com.example.suc.suc_android_solution.Models.Diner;
import com.example.suc.suc_android_solution.Models.Donation;
import com.example.suc.suc_android_solution.Models.DonationItem;
import com.example.suc.suc_android_solution.Services.DonationService;
import com.example.suc.suc_android_solution.Tasks.GetDinerTask;
import com.example.suc.suc_android_solution.Tasks.PostDonationTask;
import com.example.suc.suc_android_solution.Tasks.TaskListener;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


/**
 * Clase para el fragmento que muestra los datos de una donación
 *
 * @author Marco.Cupo
 */
public class DonationFragment extends Fragment {

    private static final String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    private static final String ARG_LAST_TITLE = "LAST_TITLE";
    private static final String ARG_DINER_ID = "DINER_ID";
    private OnFragmentInteractionListener mListener;

    private String mAccountName;
    private String lastActivityTitle;
    private BigInteger idDiner;

    private DonationService donationService;

    private AutoCompleteTextView tvDiner;
    private AutoCompleteTextView tvTitle;
    private AutoCompleteTextView tvDescription;

    private Button donateButton;
    private Context mContext;
    private AccountManager accountManager;

    private View form;
    private View mProgressView;

    private GetDinerTask dinerTask;

    public DonationFragment() {
        // Required empty public constructor
    }

    /**
     * factory method para crear fragmentos del tipo DonationFragment
     *
     * @param accountName nombre del usuario.
     * @param lastTitle   página de donde vino.
     * @param idDiner     diner al que se hara la donacion
     * @return Nueva instancia del fragmento DonationFragment.
     */

    public static DonationFragment newInstance(String accountName, String lastTitle, String idDiner) {
        DonationFragment fragment = new DonationFragment();
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

        dinerTask = new GetDinerTask(getContext());
        Activity activity = getActivity();
        activity.setTitle(R.string.donation_screen_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //return inflater.inflate(R.layout.fragment_donation, container, false);
        View view = inflater.inflate(R.layout.fragment_donation, container, false);

        tvDiner = (AutoCompleteTextView) view.findViewById(R.id.donation_diner);
        tvTitle = (AutoCompleteTextView) view.findViewById(R.id.donation_title);
        tvDescription = (AutoCompleteTextView) view.findViewById(R.id.donation_item_description);

        donateButton = (Button) view.findViewById(R.id.donation_save_button);

        mProgressView = (View) getActivity().findViewById(R.id.loading_progress);
        form = (View) view.findViewById(R.id.donation_form);
        showProgress(true);
        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptDonate();
            }
        });

        donationService = new DonationService(view.getContext());

        dinerTask.setTaskListener(new TaskListener() {
            @Override
            public void onComplete(Object response) {
                if (response instanceof Diner) {
                    tvDiner.setText(((Diner) response).getName());
                }
                showProgress(false);
            }

            @Override
            public void onMarkersReady(ArrayList<MapMarkerViewModel> markers) {

            }
        });
        dinerTask.execute(idDiner.toString());
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

            form.setVisibility(show ? View.GONE : View.VISIBLE);
            form.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    form.setVisibility(show ? View.GONE : View.VISIBLE);
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
            form.setVisibility(show ? View.GONE : View.VISIBLE);
        }
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

    /**
     * Valida los campos del formulario
     */
    private void attemptDonate() {
        tvTitle.setError(null);
        tvDescription.setError(null);

        String title = tvTitle.getText().toString();
        String description = tvDescription.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(title)) {
            tvTitle.setError(getString(R.string.error_field_required));
            if (focusView == null) focusView = tvTitle;
            cancel = true;
        }
        if (TextUtils.isEmpty(description)) {
            tvDescription.setError(getString(R.string.error_field_required));
            if (focusView == null) focusView = tvDescription;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            saveDonationData();
        }
    }

    void saveDonationData() {
        String[] parameters = new String[3];

        parameters[0] = idDiner.toString();
        parameters[1] = tvTitle.getText().toString();
        parameters[2] = tvDescription.getText().toString();

        PostDonationTask postDonationTask = new PostDonationTask(getContext());

        showProgress(true);
        postDonationTask.setTaskListener(new TaskListener() {
            @Override
            public void onComplete(Object response) {
                showProgress(false);
                if (response instanceof Donation) {
                    Toast.makeText(getContext(), "Tu donación se envío correctamente", Toast.LENGTH_SHORT).show();
                    goToDonationsList();
                } else {
                    Toast.makeText(getContext(), "Ocurrió un error, volve a intentar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onMarkersReady(ArrayList<MapMarkerViewModel> markers) {

            }
        });

        postDonationTask.execute(parameters);
    }

    private void goToDonationsList() {
        String DINERS_LIST_TAG = "seeAllTag";
        FragmentManager fragmentManager = getFragmentManager();
        /**
         * Al agregar esto al principio, logro que no se sume de forma indefinida el mismo fragmento en el stack.
         * De tal forma, al hacer back, vuelvo al main.
         */
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        /*****************************/
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        DonationsListFragment donationsListFragment = DonationsListFragment.newInstance(mAccountName, lastActivityTitle);
        fragmentTransaction.replace(R.id.suc_content, donationsListFragment, DINERS_LIST_TAG);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}