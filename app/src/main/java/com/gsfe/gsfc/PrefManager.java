package com.gsfe.gsfc;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    private static final String PREF_NAME = "terms_pref";
    private static final String KEY_IS_AGREED = "is_agreed";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setAgreed(boolean isAgreed) {
        editor.putBoolean(KEY_IS_AGREED, isAgreed);
        editor.commit();
    }

    public boolean isAgreed() {
        return pref.getBoolean(KEY_IS_AGREED, false);
    }
}
