package com.example.zodiac.sawa.RecoverPassword;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.zodiac.sawa.R;

public class CheckCodeFragment extends android.app.Fragment {
    Button btn;
    int counter = 1;
    EditText code;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check_code, container, false);
        code = (EditText) view.findViewById(R.id.code);
        btn = (Button) view.findViewById(R.id.btn);
        final String uniqueId = ((RecoverPass) getActivity()).getUniqueID();
        Log.d("Ibrahim11", uniqueId);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String codeString = code.getText().toString();

                if (codeString.equals(uniqueId)) {
                    android.app.Fragment f = new newPasswordFragment();
                    ((RecoverPass) getActivity()).replaceFragmnets(f);
                } else {
                    code.setError("Incorrect Code");
                    Log.d("arrive", "not equal");
                    counter++;
                }
                if (counter == 3) {


                }

            }
        });
        return view;
    }


}
