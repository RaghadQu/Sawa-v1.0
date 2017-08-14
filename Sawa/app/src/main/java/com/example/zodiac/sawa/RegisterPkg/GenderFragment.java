package com.example.zodiac.sawa.RegisterPkg;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.zodiac.sawa.R;

/**
 * Created by zodiac on 07/16/2017.
 */

public class GenderFragment extends android.app.Fragment {
    Button Nextbtn;
    RadioButton male, female;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_gender_fragment, container, false);
        male = (RadioButton) view.findViewById(R.id.radioM);
        female = (RadioButton) view.findViewById(R.id.radioF);
        Nextbtn = (Button) view.findViewById(R.id.nextBtn);

        Nextbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (male.isChecked()) {
                    ((RegisterActivity) getActivity()).setUserGender("male");
                    android.app.Fragment f = new PasswordFragment();
                    ((RegisterActivity) getActivity()).replaceFragmnets(f);
                    Log.d("Male Pressed", "-----");

                } else if (female.isChecked()) {
                    ((RegisterActivity) getActivity()).setUserGender("female");
                    android.app.Fragment f = new PasswordFragment();
                    ((RegisterActivity) getActivity()).replaceFragmnets(f);
                    Log.d("Female Pressed", "-----");


                } else {
                    Log.d("Not Pressed", "-----");
                }


            }
        });
        return view;
    }
}
