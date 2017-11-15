package com.example.suc.suc_android_solution;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.suc.suc_android_solution.Models.Donation;
import com.example.suc.suc_android_solution.Models.DonationItem;
import com.example.suc.suc_android_solution.Services.DonationService;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * Clase para el fragmento que muestra los datos de una donación
 *
 * @author Marco.Cupo
 */
public class DonationFragment extends Fragment {

    private static final String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    private static final String ARG_LAST_TITLE = "LAST_TITLE";
    private OnFragmentInteractionListener mListener;

    private String mAccountName;
    private String lastActivityTitle;

    private DonationService donationService;

    private AutoCompleteTextView tvDiner;
    private AutoCompleteTextView tvTitle;
    private AutoCompleteTextView tvDescription;

    private Button donateButton;

    public DonationFragment() {
        // Required empty public constructor
    }

    /**
     * factory method para crear fragmentos del tipo DonationFragment
     *
     * @param accountName nombre del usuario.
     * @param lastTitle página de donde vino.
     * @return Nueva instancia del fragmento DonationFragment.
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

        Activity activity = getActivity();
        activity.setTitle(R.string.donation_screen_title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //return inflater.inflate(R.layout.fragment_donation, container, false);
        View view = inflater.inflate(R.layout.fragment_donation, container, false);

        tvDiner         = (AutoCompleteTextView) view.findViewById(R.id.donation_diner);
        tvTitle         = (AutoCompleteTextView) view.findViewById(R.id.donation_title);
        tvDescription   = (AutoCompleteTextView) view.findViewById(R.id.donation_item_description);

        donateButton = (Button) view.findViewById(R.id.donation_save_button);

        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDonationData();
            }
        });

        donationService = new DonationService(view.getContext());

        return view;
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

    void saveDonationData() {
        String[] parameters = new String[3];

        parameters[0] = tvDiner.getText().toString();
        parameters[1] = tvTitle.getText().toString();
        parameters[2] = tvDescription.getText().toString();

        new PostDonationTask().execute(parameters);
    }

    public class PostDonationTask extends AsyncTask<String, Void, Donation>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Donation doInBackground(String... params) {

            try {
                DonationItem newDonationItem = new DonationItem.Builder().setDescription(params[2]).build();
                List<DonationItem> items = new ArrayList<DonationItem>();
                items.add(newDonationItem);
                Donation newDonation = new Donation.Builder()
                            .setIdUserSender(BigInteger.valueOf(1))//TODO Obtener el usuario logeado
                            .setIdDinerReceiver(new BigInteger(params[0]))
                            .setTitle(params[1])
                            .setItems(items)
                            .build();

                Donation savedDonation = donationService.postDonation(newDonation);

                return savedDonation;

            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Donation donationSaved) {
            if(donationSaved != null){
                Toast.makeText(getContext(), "Tu donación se envío correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), "Ocurrió un error, volve a intentar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}