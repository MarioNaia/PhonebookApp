package com.example.itsectorphonebook.contacts;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itsectorphonebook.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }





    Button b1,aboutBtn;
    EditText ed1, ed2;
    private EditText edt1, edt2,edt3,edt4;
    EditText firstET, secondET;



    TextView tx1;
    int counter = 3;
    StringBuilder currentCode=new StringBuilder();
    StringBuilder trueCode=new StringBuilder("1121");



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        //View rootView = inflater.inflate(R.layout.word_list, container, false);

        if (container != null) {
            container.removeAllViews();
            currentCode.setLength(0);
        }


        edt1 = (EditText) view.findViewById(R.id.edt1);
        edt2 = (EditText) view.findViewById(R.id.edt2);
        edt3 = (EditText) view.findViewById(R.id.edt3);
        edt4 = (EditText) view.findViewById(R.id.edt4);

        ed1 = (EditText) view.findViewById(R.id.editText);

        b1 = (Button) view.findViewById(R.id.button);
        aboutBtn = (Button) view.findViewById(R.id.button2);

        tx1 = (TextView) view.findViewById(R.id.textView3);
        tx1.setVisibility(View.GONE);




        /*Password with 4 numerical pins
         * changes focus as its written
         *
         *
         */

        edt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String edtChar = edt1.getText().toString();
                if (edtChar.length() == 1) {
                    currentCode.append(edtChar);

                    edt2.requestFocus();
                } else if (edtChar.length() == 0&& currentCode.length()>=1) {
                    currentCode.deleteCharAt(0);
                    edt1.requestFocus();
                }


            }



        });


        edt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String edtChar = edt2.getText().toString();
                if (edtChar.length() == 1) {
                    currentCode.append(edtChar);

                    edt3.requestFocus();
                } else if (edtChar.length() == 0&& currentCode.length()>=2) {
                    currentCode.deleteCharAt(1);
                    edt1.requestFocus();
                }


            }
        });
        edt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String edtChar = edt3.getText().toString();
                if (edtChar.length() == 1) {
                    currentCode.append(edtChar);

                    edt4.requestFocus();
                } else if (edtChar.length() == 0 && currentCode.length()>=3) {
                    Log.d("da", String.valueOf(currentCode)+currentCode.length());
                    currentCode.deleteCharAt(2);
                    edt2.requestFocus();
                }


            }
        });
        edt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String edtChar = edt4.getText().toString();
                if (edtChar.length() == 1) {
                    currentCode.append(edtChar);

                    edt4.requestFocus();
                } else if (edtChar.length() == 0 && currentCode.length()>=4) {
                    currentCode.deleteCharAt(3);
                    edt2.requestFocus();
                }


            }
        });



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Only logs in if the following is written
                 *Name:  admin
                 *pass:  1121
                 *after 3 tries disables log in button
                 */

                if (currentCode.toString().equals(trueCode.toString()) &&
                        ed1.getText().toString().equals("admin")) {
                    Toast.makeText(getActivity(),
                            "Login Válido", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), PhonebookActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Login Inválido", Toast.LENGTH_SHORT).show();

                    tx1.setVisibility(View.VISIBLE);
                    tx1.setBackgroundColor(Color.RED);
                    counter--;
                    tx1.setText(Integer.toString(counter));



                    if (counter == 0) {
                        b1.setEnabled(false);
                    }
                }

            }
        });



        aboutBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "About Page...", Toast.LENGTH_SHORT).show();
                Fragment fragment = null;
                fragment = new AboutFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.mainFragment,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                currentCode=new StringBuilder("0000");

            }
        });

        return view;
    }
}
