package com.example.mechanic2.app;

import android.content.Context;
import android.content.SharedPreferences.Editor;

import java.util.Map;

import static com.example.mechanic2.app.Application.getContext;

/**
 * The type Shared pref utils.
 *
 * @author Nishant Srivastava
 */
public class SharedPrefUtils {

    public static final String PREF_APP = "pref_app";

    private SharedPrefUtils() {
        throw new UnsupportedOperationException(
                "Should not create instance of Util class. Please use as static..");
    }

    /**
     * Gets boolean data.
     *
     * param context the context
     * @param key     the key
     * @return the boolean data
     */
    static public boolean getBooleanData(String key) {

        return getContext().getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getBoolean(key, false);
    }

    /**
     * Gets int data.
     *
     * param context the context
     * @param key     the key
     * @return the int data
     */
    static public int getIntData( String key) {
        return  getContext().getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getInt(key, 0);
    }

    /**
     * Gets string data.
     *
     * param context the context
     * @param key     the key
     * @return the string data
     */
    // Get Data
    static public String getStringData( String key) {
        return  getContext().getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getString(key, "-1");
    }// Get Data
    static public float getFloatData( String key) {
        return  getContext().getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getFloat(key, 0);
    }


    static public double  getDoubleData( String key) {
        return Double.longBitsToDouble(getContext().getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getLong(key, Double.doubleToLongBits(0)));
    }

    /**
     * Save data.
     *
     * param context the context
     * @param key     the key
     * @param val     the val
     */
    // Save Data
    static public void saveData( String key, String val) {
        getContext().getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).edit().putString(key, val).apply();
    }// Save Data
    static public void saveData( String key, Float val) {
        getContext().getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).edit().putFloat(key, val).apply();
    }

    static public void saveData( String key, double val) {
        getContext().getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).edit().putLong(key, Double.doubleToRawLongBits(val)).apply();
    }

    /**
     * Save data.
     *
     * param context the context
     * @param key     the key
     * @param val     the val
     */
    static public void saveData( String key, int val) {
        getContext().getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).edit().putInt(key, val).apply();
    }

    /**
     * Save data.
     *
     * param context the context
     * @param key     the key
     * @param val     the val
     */
    static public void saveData( String key, boolean val) {
        getContext().getSharedPreferences(PREF_APP, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(key, val)
                .apply();
    }

    static public Editor getSharedPrefEditor( String pref) {
        return getContext().getSharedPreferences(pref, Context.MODE_PRIVATE).edit();
    }

    static public void clearDataByArgument(String startWith){
        Map<String,?> keys = getContext().getSharedPreferences(SharedPrefUtils.PREF_APP, Context.MODE_PRIVATE).getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
           if(entry.getKey().startsWith(startWith)) saveData(entry.getKey(),0);
        }
    }
    static public void removeDataByArgument(String key) {
        Map<String, ?> keys = getContext().getSharedPreferences(SharedPrefUtils.PREF_APP, Context.MODE_PRIVATE).getAll();

        for (Map.Entry<String, ?> entry : keys.entrySet()) {
            if (entry.getKey().equals(key))
                getSharedPrefEditor(PREF_APP).remove(entry.getKey()).apply();
        }

    }

    static public void saveData(Editor editor) {
        editor.apply();
    }

    public static class Keys{
        public static String USER_TYPE="USER_TYPE";
        public static String USER_MOBILE="USER_MOBILE";
        public static String CODE="CODE";
        public static String USER_ID="USER_ID";

    }
}