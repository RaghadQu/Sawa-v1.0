package com.example.zodiac.sawa.RegisterPkg;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.zodiac.sawa.R;

/**
 * Created by zodiac on 07/10/2017.
 */

public class NameFragment extends android.app.Fragment {

    EditText FirstName, LastName;
    Button Nextbtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

        View view = inflater.inflate(R.layout.register_name_fragment, container, false);

        FirstName = (EditText) view.findViewById(R.id.first_name);
        LastName = (EditText) view.findViewById(R.id.last_name);
        Nextbtn = (Button) view.findViewById(R.id.nextBtn);
        Nextbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (FirstName.getText().toString().equals("")) {
                    FirstName.setError("Please enter your first name");
                } else {
                    if (LastName.getText().toString().equals("")) {
                        LastName.setError("Please enter your last name");
                    } else {

                        ((RegisterActivity) getActivity()).setFirstName(FirstName.getText().toString());
                        ((RegisterActivity) getActivity()).setLastName(LastName.getText().toString());
                        android.app.Fragment f = new EmailFragment();
                        ((RegisterActivity) getActivity()).replaceFragmnets(f);
                    }

                }
            }
        });
        return view;

    }


}


