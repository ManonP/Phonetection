package com.ihm15.project.phonetection;

/**Classe permettant de réunir les variables globales
 * Created by Manon on 01/11/2015.
 */
public class Library {

    public static Boolean CONNECTED = false;
    public static Boolean DETECTION_MODE;
    public static Boolean CABLE_MODE;
    public static Boolean SIM_MODE;

    public static TextToSpeak textToSpeak;

    public static Alarme alarme = new Alarme();
}



