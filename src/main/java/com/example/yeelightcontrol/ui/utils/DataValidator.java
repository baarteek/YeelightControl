package com.example.yeelightcontrol.ui.utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataValidator {

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    private static final Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);

    public static boolean isNameValid(String name) {
        if(name != null && name.length() > 0 && name.length() <= 100) {
            return true;
        }
        return false;
    }

    public static boolean isIPValid(String ip) {
        if(ip != null) {
            Matcher matcher = pattern.matcher(ip);
            return matcher.matches();
        }
        return false;
    }
}
