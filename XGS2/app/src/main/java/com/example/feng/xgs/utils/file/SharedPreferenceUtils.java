package com.example.feng.xgs.utils.file;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.feng.xgs.config.IPerfectConfig;
import com.example.feng.xgs.config.key.ShareKeys;


public final class SharedPreferenceUtils {

    /**
     * 提示:
     * Activity.getPreferences(int mode)生成 Activity名.xml 用于Activity内部存储
     * PreferenceManager.getDefaultSharedPreferences(Context)生成 包名_preferences.xml
     * Context.getSharedPreferences(String name,int mode)生成name.xml
     */
    private static final SharedPreferences PREFERENCES =
            PreferenceManager.getDefaultSharedPreferences(IPerfectConfig.getApplicationContext());
    private static final String APP_PREFERENCES_KEY = "profile";
    private static final String UID = "userId";
    private static final String SIGN_TAG = "signTag";


    private static SharedPreferences getAppPreference() {
        return PREFERENCES;
    }


    public static void setUId(String val) {
        getAppPreference()
                .edit()
                .putString(UID, val)
                .apply();
    }

    public static String getUserId() {
        return getAppPreference().getString(UID, null);
    }

    public static String getPeopleId() {
        return getAppPreference().getString(ShareKeys.PEOPLE_ID, null);
    }


    public static void clearAppPreferences() {
        getAppPreference()
                .edit()
                .clear()
                .apply();
    }

    public static void setAppSign(boolean flag) {
        getAppPreference()
                .edit()
                .putBoolean(SIGN_TAG, flag)
                .apply();
    }

    public static boolean getAppSign() {
        return getAppPreference()
                .getBoolean(SIGN_TAG, false);
    }

    public static void setAppFlag(String key, boolean flag) {
        getAppPreference()
                .edit()
                .putBoolean(key, flag)
                .apply();
    }

    public static boolean getAppFlag(String key) {
        return getAppPreference()
                .getBoolean(key, false);
    }

    public static void setCustomAppProfile(String key, String val) {
        getAppPreference()
                .edit()
                .putString(key, val)
                .apply();
    }
    public static void setCustomAppProfile2(String key, String val) {
        getAppPreference()
                .edit()
                .putString(key, val)
                .apply();
    }

    public static void setCustomAppLong(String key, long val) {
        getAppPreference()
                .edit()
                .putLong(key, val)
                .apply();
    }
    public static void setCustomAppSex(String key, String val) {
        getAppPreference()
                .edit()
                .putString(key, val)
                .apply();
    }
    public static void setCustomInt(String key, int val) {
        getAppPreference()
                .edit()
                .putInt(key, val)
                .apply();
    }

    public static int getCustomInt(String key) {
        return getAppPreference().getInt(key, 0);
    }

    public static long getCustomAppLong(String key) {
        return getAppPreference().getLong(key, 0);
    }

    public static String getCustomAppProfile(String key) {
        return getAppPreference().getString(key, "");
    }
    public static String getCustomAppSex(String key) {
        return getAppPreference().getString(key,null);
    }
    public static String getCustomAppProfile2(String key) {
        return getAppPreference().getString(key, "");
    }
}
