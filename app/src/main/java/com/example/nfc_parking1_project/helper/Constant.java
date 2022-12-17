package com.example.nfc_parking1_project.helper;

import java.util.regex.Pattern;

public class Constant {
    public static final String BASE_URL = "https://nfcparking.tech/?fbclid=IwAR3QbrDwHwFbKTLLx0VCnPvl8a41km6ct9tc-vidaQBnNhzNwNsVMB078RA";
    public static final String regexPassword = "^(?=.*[0-9])"
            + "(?=.*[a-z])(?=.*[A-Z])"
            + "(?=.*[@#$%^&+=])"
            + "(?=\\S+$).{8,20}$";

    public static final String regexPhoneNumber = "^\\d{10}$";
    public static final String regexNameFilter = "^\\p{L}+[\\p{L}\\p{Z}\\p{P}]{0,}";
    public static final String regexPhoneNumberFilter = "[0-9]+";
    public static final Pattern regexNamePatternFilter = Pattern.compile(regexNameFilter);
    public static final Pattern regexPhoneNumberPatternFilter = Pattern.compile(regexPhoneNumberFilter);
    public static Pattern passwordPattern = Pattern.compile(regexPassword);
    public static Pattern phoneNumberPattern = Pattern.compile(regexPhoneNumber);

    public static String ROLE_AMIN = "ADMIN";
    public static String ROLE_STAFF = "EMP";
    public static String CURRENT_ROLE="";
    public static String CARD_STATUS_LOST = "LOST";
}


