package com.example.suc.suc_android_solution;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.suc.suc_android_solution.Models.Authentication.AuthCredentials;
import com.example.suc.suc_android_solution.Models.Authentication.AuthenticationResponse;
import com.example.suc.suc_android_solution.Models.Authentication.UpdateCredentials;
import com.example.suc.suc_android_solution.Models.User;
import com.example.suc.suc_android_solution.Services.AuthenticationService;

import java.text.SimpleDateFormat;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangePasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {
    private static final String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    private static final String ARG_LAST_TITLE = "LAST_TITLE";

    private String mAccountName;
    private String lastActivityTitle;

    private AutoCompleteTextView mOldPassword;
    private AutoCompleteTextView mNewPassword;
    private AutoCompleteTextView mNewPasswordValidate;
    private Button updatePasswordButton;

    private AuthenticationService authenticationService;

    private View form;
    private View mProgressView;

    private OnFragmentInteractionListener mListener;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param accountName nombre del usuario.
     * @param lastTitle Titulo que estaba antes en el stack
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordFragment newInstance(String accountName, String lastTitle) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
        activity.setTitle(R.string.title_change_password);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        mOldPassword = (AutoCompleteTextView) view.findViewById(R.id.user_old_password);
        mNewPassword = (AutoCompleteTextView) view.findViewById(R.id.user_new_password);
        mNewPasswordValidate = (AutoCompleteTextView) view.findViewById(R.id.user_new_password_validate);
        updatePasswordButton = (Button) view.findViewById(R.id.user_change_password_button);

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });

        mProgressView = (View) getActivity().findViewById(R.id.loading_progress);
        form = (View) view.findViewById(R.id.change_password_form);


        authenticationService = new AuthenticationService(view.getContext());
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

    private void changePassword() {
        if(!mNewPassword.getText().toString().equals(mNewPasswordValidate.getText().toString())){
            Toast.makeText(getView().getContext(), getString(R.string.password_missmatch), Toast.LENGTH_SHORT).show();
        }else{
            showProgress(true);
            new ChangePasswordTask().execute(mAccountName, mOldPassword.getText().toString(), mNewPassword.getText().toString());
        }
    }

    public class ChangePasswordTask extends AsyncTask<String, Void, AuthenticationResponse> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected AuthenticationResponse doInBackground(String... params) {

            try {
                UpdateCredentials updateCredentials = new UpdateCredentials(params[0], params[1], params[2]);

                AuthenticationResponse authResponse = authenticationService.changePassword(updateCredentials);

                return authResponse;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(AuthenticationResponse authResponse) {
            showProgress(false);
            if(authResponse != null){
                Toast.makeText(getContext(), getString(R.string.update_successful), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), getString(R.string.update_error), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
