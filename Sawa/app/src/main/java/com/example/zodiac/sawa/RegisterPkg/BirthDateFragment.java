package com.example.zodiac.sawa.RegisterPkg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.zodiac.sawa.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zodiac on 07/16/2017.
 */

public class BirthDateFragment extends android.app.Fragment {

    Button Nextbtn;
    DatePicker birthdatePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.register_birthdate_fragment, container, false);
        Nextbtn = (Button) view.findViewById(R.id.nextBtn);
        birthdatePicker = (DatePicker) view.findViewById(R.id.BirthDatePicker);


        Nextbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Date myDate;
                birthdatePicker.getYear();
                birthdatePicker.getMonth();
                birthdatePicker.getDayOfMonth();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String stringDate = String.valueOf(birthdatePicker.getYear()) + "-" + String.valueOf(birthdatePicker.getMonth() + 1) + "-" + String.valueOf(birthdatePicker.getDayOfMonth());
                //String a =dateFormat.format(userBirthDate);
                try {
                    myDate = dateFormat.parse(stringDate);

                    ((RegisterActivity) getActivity()).setUserBirthDate(stringDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                android.app.Fragment f = new GenderFragment();
                ((RegisterActivity) getActivity()).replaceFragmnets(f);


            }
        });
        return view;
    }
}


