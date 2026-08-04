package com.dinsaren.springbootjwtapi.utils;

public class helper {

    public static String generateUsername(String firstName,
                                          String lastName,
                                          Integer id) {

        String fn = firstName == null ? "" : firstName.trim().replaceAll("\\s+", "");
        String ln = lastName == null ? "" : lastName.trim().replaceAll("\\s+", "");

        return (fn + ln + id).toLowerCase();
    }
}