package com.example.zodiac.sawa;

import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Rabee on 3/24/2017.
 */

public class Validation {
    public Validation() {

    }

    public static boolean isEmailValid(CharSequence email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern p = Pattern.compile(emailPattern);
        Matcher m = p.matcher(email);
        boolean b = m.matches();
        if (b) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

    public boolean isDataValide(EditText first_name, EditText last_name, EditText email, EditText mobile, EditText password, EditText confPassword) {
        boolean isValide = true;
        if ((first_name.getText().toString().trim().equals(""))) {
            first_name.setError("First name is required");
            isValide = false;
        } else if (first_name.getText().toString().contains(" ")) {
            first_name.setError("First name musn't conatain space");
            isValide = false;
        }

        if ((last_name.getText().toString().trim().equals(""))) {
            last_name.setError("Last name is required");
            isValide = false;
        } else if (last_name.getText().toString().contains(" ")) {
            first_name.setError("Last name musn't contain space");
            isValide = false;
        }

        if ((mobile.getText().toString().trim().equals(""))) {
            mobile.setError("Mobile number is required");
            isValide = false;
        } else if (!isValidMobile(mobile.getText().toString())) {
            mobile.setError("Mobile nimber is not valid");
            isValide = false;
        }
        if ((email.getText().toString().trim().equals(""))) {
            email.setError("Email is required");
            isValide = false;
        } else if (!(isEmailValid(email.getText().toString()))) {
            email.setError("Email is not valid");
            isValide = false;
        }
        if ((password.getText().toString().trim().equals(""))) {
            password.setError("Password is required");
            isValide = false;
        }
        if ((confPassword.getText().toString().trim().equals(""))) {
            confPassword.setError("Password is required");
            isValide = false;
        } else if (password.getText().toString().length() < 8) {
            password.setError("Password must contain at least 8 characters");
            isValide = false;
        } else if (!((confPassword.getText().toString()).equals(password.getText().toString()))) {
            confPassword.setError("Confirm password  doesn't match password!");
            isValide = false;
        }
        return isValide;
    }
}
