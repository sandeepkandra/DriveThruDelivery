package com.nanitesol.delivery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by praveen kumar on 1/23/2016.
 */
public class Tokens {


    private String mName;
    private boolean mOnline;

    public Tokens(String name, boolean online) {
        mName = name;
        mOnline = online;
    }

    public String getName() {
        return mName;
    }

    public boolean isOnline() {
        return mOnline;
    }

    private static int lastContactId = 0;

    public static List<Tokens> createContactsList(int numContacts) {
        List<Tokens> contacts = new ArrayList<Tokens>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new Tokens("Token " + ++lastContactId, i <= numContacts / 2));
        }

        return contacts;
    }
}
