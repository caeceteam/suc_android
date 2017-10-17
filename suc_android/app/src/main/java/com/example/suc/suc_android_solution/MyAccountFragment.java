package com.example.suc.suc_android_solution;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.suc.suc_android_solution.Models.User;
import com.example.suc.suc_android_solution.Services.UserService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyAccountFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyAccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAccountFragment extends Fragment {

    private static final String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    private static final String ARG_LAST_TITLE = "LAST_TITLE";
    private OnFragmentInteractionListener mListener;

    private String mAccountName;
    private String lastActivityTitle;

    private UserService userService;

    private AutoCompleteTextView tvName;
    private AutoCompleteTextView tvSurname;
    private AutoCompleteTextView tvAlias;
    private AutoCompleteTextView tvMail;
    private AutoCompleteTextView tvPhone;
    private AutoCompleteTextView tvStreet;
    private AutoCompleteTextView tvStreetNumber;
    private AutoCompleteTextView tvFloor;
    private AutoCompleteTextView tvDoor;
    private AutoCompleteTextView tvDocument;

    private EditText etBornDate;

    private Button updateDataButton;

    public MyAccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param accountName nombre de usuario.
     * @return A new instance of fragment MyAccountFragment.
     */
    public static MyAccountFragment newInstance(String accountName, String lastTitle) {
        MyAccountFragment fragment = new MyAccountFragment();
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
        activity.setTitle(R.string.title_my_account);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

        tvName = (AutoCompleteTextView) view.findViewById(R.id.user_name);
        tvSurname = (AutoCompleteTextView) view.findViewById(R.id.user_surname);
        tvAlias = (AutoCompleteTextView) view.findViewById(R.id.user_alias);
        tvMail = (AutoCompleteTextView) view.findViewById(R.id.user_mail);
        tvPhone = (AutoCompleteTextView) view.findViewById(R.id.user_phone);
        tvStreet = (AutoCompleteTextView) view.findViewById(R.id.user_street);
        tvStreetNumber = (AutoCompleteTextView) view.findViewById(R.id.user_street_number);
        tvFloor = (AutoCompleteTextView) view.findViewById(R.id.user_floor);
        tvDoor = (AutoCompleteTextView) view.findViewById(R.id.user_door);
        tvDocument = (AutoCompleteTextView) view.findViewById(R.id.user_doc_number);

        etBornDate = (EditText) view.findViewById(R.id.user_born_date);
        setBornDateCalendar();

        updateDataButton = (Button) view.findViewById(R.id.user_update_button);

        updateDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserData();
            }
        });

        userService = new UserService(view.getContext());

        new GetUserTask().execute();

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


    private void setBornDateCalendar() {

        final Calendar myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(myCalendar);
            }

        };

        etBornDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getView().getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    void updateLabel (Calendar myCalendar) {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("es", "ES"));

        etBornDate.setText(sdf.format(myCalendar.getTime()));
    }

    void updateUserData(){

        String[] parameters = new String[11];
        parameters[0] = tvAlias.getText().toString();
        parameters[1] = tvName.getText().toString();
        parameters[2] = tvSurname.getText().toString();
        parameters[3] = tvMail.getText().toString();
        parameters[4] = tvPhone.getText().toString();
        parameters[5] = tvStreet.getText().toString();
        parameters[6] = tvStreetNumber.getText().toString();
        parameters[7] = tvFloor.getText().toString();
        parameters[8] = tvDoor.getText().toString();
        parameters[9] = tvDocument.getText().toString();
        parameters[10] = etBornDate.getText().toString();

        new PutUserTask().execute(parameters);

    }

    public class PutUserTask extends AsyncTask<String, Void, User> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(String... params) {

            try {
                User newUser = new User.Builder()
                        .setAlias(params[0])
                        .setName(params[1])
                        .setSurname(params[2])
                        .setMail(params[3])
                        .setPhone(params[4])
                        .setStreet(params[5])
                        .setStreetNumber(Integer.parseInt(params[6]))
                        .setFloor(params[7])
                        .setDoor(params[8])
                        .setDocNumber(params[9])
                        .setBornDate(new SimpleDateFormat("dd/MM/yy").parse(params[10]))
                        .build();

                User updatedUser = userService.putUser(getArguments().get(ARG_ACCOUNT_NAME).toString(), newUser);

                /*if(registeredUser != null){
                    MailParams mailParams = new MailParams();
                    mailParams.setDestination_email(params[3]);
                    mailParams.setUserName(params[3]);
                    mailParams.setPassword(params[4]);
                    mailParams.setMailType(MailType.NO_VALIDATABLE_REGISTRATION.getValue());
                    emailService.sendNoValidatableRegistrationMail(mailParams);
                }*/


                return updatedUser;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(User userRegistered) {
            if(userRegistered != null){
                Toast.makeText(getContext(), getString(R.string.update_successful), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(), getString(R.string.update_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class GetUserTask extends AsyncTask<String, Void, User> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected User doInBackground(String... params) {

            try {

                User user = userService.getUser(getArguments().get(ARG_ACCOUNT_NAME).toString());

                /*if(registeredUser != null){
                    MailParams mailParams = new MailParams();
                    mailParams.setDestination_email(params[3]);
                    mailParams.setUserName(params[3]);
                    mailParams.setPassword(params[4]);
                    mailParams.setMailType(MailType.NO_VALIDATABLE_REGISTRATION.getValue());
                    emailService.sendNoValidatableRegistrationMail(mailParams);
                }*/


                return user;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(User user) {
            if(user != null){
                tvName.setText(user.getName());
                tvSurname.setText(user.getSurname());
                tvMail.setText(user.getMail());
                tvAlias.setText(user.getAlias());
                tvPhone.setText(user.getPhone());
                tvStreet.setText(user.getStreet());
                tvStreetNumber.setText(user.getStreetNumber() != null ? user.getStreetNumber().toString() : "");
                tvFloor.setText(user.getFloor());
                tvDoor.setText(user.getDoor());
                tvDocument.setText(user.getDocNumber());
                etBornDate.setText(user.getBornDate() != null ? new SimpleDateFormat("dd/MM/yy").format(user.getBornDate()) : "");
            }
        }
    }


}
