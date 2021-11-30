package com.example.kanete.AuthenticationActivities;

import android.util.Patterns;
import android.widget.EditText;

import com.example.kanete.R;

// TODO move all Strings to strings.xml
public class AuthenticationUtils {

    public static boolean checkFirstName(String name, EditText editText){
        if (name.isEmpty()){
            editText.setError("Please enter you first name!");
            return false;
        }
        else if (!name.matches("^[A-Za-z]{3,}+$")) {
            editText.setError("Please enter a proper first name!");
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean checkLastName(String name, EditText editText){
        if (name.isEmpty()){
            editText.setError("Please enter you last name!");
            return false;
        }
        else if (!name.matches("^[A-Za-z]{3,}(?: [A-Z][a-z]*)*$")) {
            editText.setError("Please enter a proper last name!");
            return false;
        }
        else {
            return true;
        }
    }

    public static String getGender(int gender_id) {
        if (gender_id == R.id.radioButtonMale){
            return "Male";
        }
        else {
            return "Female";
        }
    }

    public static boolean checkEmail(String email, EditText editText) {
        if (email.isEmpty()){
            editText.setError("Please enter you email!");
            return false;
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editText.setError("Please enter a proper email!");
            return false;
        }
        else{
            return true;
        }
    }

    public static boolean checkPassword(String password, EditText editText) {
        if (password.isEmpty()){
            editText.setError("Please enter you password!");
            return false;
        }
        else if (password.length() < 6){
            editText.setError("The password should be at least 6 characters!");
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean checkRePassword(String password, String repassword, EditText editText) {
        if (!password.equals(repassword)){
            editText.setError("Password doesn't match!");
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean checkName(String name, EditText editText){
        if (name.isEmpty()){
            editText.setError("Please enter you store name!");
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean checkAddress(String address, EditText editText) {
        if (address.isEmpty()){
            editText.setError("Please enter you store address!");
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean checkPhone(String phone, EditText editText) {
        if (phone.isEmpty()){
            editText.setError("Please enter you store phone!");
            return false;
        }
        else if (!phone.matches("^[+]?[0-9]{10,13}$")) {
            editText.setError("Please enter a proper phone number!");
            return false;
        }
        else {
            return true;
        }
    }

    public static boolean checkFacebook(String facebook, EditText editText) {
        if (!facebook.isEmpty()){
            if (!Patterns.WEB_URL.matcher(facebook).matches() || facebook.contains("facebook.com")) {
                editText.setError("Please enter a proper facebook url!");
                return false;
            }
        }
        return true;
    }

    public static boolean checkInstagram(String instagram, EditText editText) {
        if (!instagram.isEmpty()){
            if (!Patterns.WEB_URL.matcher(instagram).matches() || instagram.contains("instagram.com")) {
                editText.setError("Please enter a proper instagram url!");
                return false;
            }
        }
        return true;
    }

    public static boolean checkWebsite(String website, EditText editText) {
        if (!website.isEmpty()){
            if (!Patterns.WEB_URL.matcher(website).matches()) {
                editText.setError("Please enter a proper website url!");
                return false;
            }
        }
        return true;
    }
}
