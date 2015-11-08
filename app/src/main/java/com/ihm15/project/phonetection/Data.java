package com.ihm15.project.phonetection;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**Classe permettant de réunir les variables globales
 * Created by Manon on 01/11/2015.
 */
public final class Data {

    public static final String EXTRA_WITH_ALARM = "com.ihm15.project.phonetection.WITH_ALARM";
    public static final String EXTRA_MODE = "com.ihm15.project.phonetection.MODE";

    public static final String MOTION_BUTTON_TAG = "motion_button";
    public static final String CHARGER_BUTTON_TAG = "charger_button";
    public static final String SIM_BUTTON_TAG = "sim_button";
    public static final String SMS_BUTTON_TAG = "sms_button";

    public static final int MOTION_MODE = 0;
    public static final int CHARGER_MODE = 1;
    public static final int SIM_MODE = 2;
    public static final int SMS_MODE = 3;

    public static final int PIN_UNLOCK = 10;
    public static final int PATTERN_UNLOCK = 11;
    public static final int IMAGE_UNLOCK = 12;
    public static final int WRONG_PIN_UNLOCK = 13;
    public static final int WRONG_PATTERN_UNLOCK = 14;
    public static final int WRONG_IMAGE_UNLOCK = 15;

    private static volatile Data instance = null;
    private static Context context;
    private static SharedPreferences sp;

    private Data (Context c){
        super();
        context = c;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static Data getInstance(Context context){
        if (Data.instance == null){
            synchronized(Data.class) {
                if (Data.instance == null) {
                    Data.instance = new Data(context);
                }
            }
        }
        return Data.instance;
    }

    public static boolean isLightAlarmActivate(){
        return sp.getBoolean(context.getString(R.string.pref_key_light_alarm),
                context.getResources().getBoolean(R.bool.pref_light_alarm_default));
    }

    public static int getGraceTime(){
        return sp.getInt(context.getString(R.string.pref_key_grace_time),
                context.getResources().getInteger(R.integer.pref_grace_time_default));
    }

    public static String getSms(){
        return sp.getString(context.getString(R.string.pref_key_sms),
                context.getString(R.string.pref_sms_default));
    }

    public static String getSecurityLevel(){
        return sp.getString(context.getString(R.string.pref_key_security_level),
                context.getResources().getString(R.string.pref_security_level_default));
    }

    public static String getPin(){
        return sp.getString(context.getString(R.string.pref_key_pin),
                context.getResources().getString(R.string.pref_pin_default));
    }

    public static int getPattern(){
        return sp.getInt(context.getString(R.string.pref_key_pattern),
                context.getResources().getInteger(R.integer.pref_pattern_default));
    }

    public static String getImage(){
        return sp.getString(context.getString(R.string.pref_key_image),
                context.getResources().getString(R.string.pref_image_default));
    }

    public static boolean isMotionModeActivate(){
        return sp.getBoolean(context.getString(R.string.pref_key_motion_mode),
                context.getResources().getBoolean(R.bool.pref_motion_mode_default));
    }

    public static boolean isCableModeActivate(){
        return sp.getBoolean(context.getString(R.string.pref_key_cable_mode),
                context.getResources().getBoolean(R.bool.pref_cable_mode_default));
    }

    public static boolean isSimModeActivate(){
        return sp.getBoolean(context.getString(R.string.pref_key_sim_mode),
                context.getResources().getBoolean(R.bool.pref_sim_mode_default));
    }

    public static boolean isSmsModeActivate(){
        return sp.getBoolean(context.getString(R.string.pref_key_sms_mode),
                context.getResources().getBoolean(R.bool.pref_sms_mode_default));
    }

    public static void setMotionMode(boolean activate) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(context.getString(R.string.pref_key_motion_mode), activate);
        editor.commit();
    }

    public static void setCableMode(boolean activate) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(context.getString(R.string.pref_key_cable_mode), activate);
        editor.commit();
    }

    public static void setSimMode(boolean activate) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(context.getString(R.string.pref_key_sim_mode), activate);
        editor.commit();
    }

    public static void setSmsMode(boolean activate) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(context.getString(R.string.pref_key_sms_mode), activate);
        editor.commit();
    }

    public static void setLightAlarm(boolean activate) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(context.getString(R.string.pref_key_light_alarm), activate);
        editor.commit();
    }

    public static void setGraceTime(int graceTime) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(context.getString(R.string.pref_key_grace_time), graceTime);
        editor.commit();
    }

    public static void setSms(String sms) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(context.getString(R.string.pref_key_sms), sms);
    }

    public static void setSecurityLevel(String securityLevel) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(context.getString(R.string.pref_key_security_level),
                securityLevel);
        editor.commit();
    }

    public static void setPin(String pin) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(context.getString(R.string.pref_key_pin),
                pin);
        editor.commit();
    }

    public static void setPattern(int pattern) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(context.getString(R.string.pref_key_pattern),
                pattern);
        editor.commit();
    }

    public static void setImage(String image) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(context.getString(R.string.pref_key_image),
                image);
        editor.commit();
    }
}