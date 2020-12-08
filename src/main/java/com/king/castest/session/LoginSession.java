package com.king.castest.session;

import java.util.HashMap;
import java.util.Map;

public class LoginSession {
    private static Map<String, String> cash = new HashMap<>();

    private static final String USERNAME = "username";

    public static String setUserName(String userName){
        return cash.put(USERNAME, userName);
    }

    public static String getUserName(String userName){
        return cash.get(USERNAME);
    }
}
