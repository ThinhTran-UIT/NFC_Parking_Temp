package com.example.nfc_parking1_project.helper;

import java.util.regex.Pattern;

public class Constant {
    public static final String BASE_URL = "https://a04b-2001-ee0-4f4f-d9d0-ad01-452a-4cc3-c667.ap.ngrok.io";
    public static final String regexPassword = "^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[@#$%^&+=])"
            + "(?=\\S+$).{8,20}$";

    public static final String regexPhoneNumber = "^\\d{10}$";
    public static final String regexNameFilter = "^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{0,}";
    public static final String regexPhoneNumberFilter = "[0-9]+";
    public static final Pattern regexNamePatternFilter = Pattern.compile(regexNameFilter);
    public static final Pattern regexPhoneNumberPatternFilter =Pattern.compile(regexPhoneNumberFilter);
    public static Pattern passwordPattern = Pattern.compile(regexPassword);
    public static Pattern phoneNumberPattern = Pattern.compile(regexPhoneNumber);

    public static String CARD_STATUS_LOST = "LOST";
}


