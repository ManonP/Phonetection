package com.ihm15.project.phonetection;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Manon on 01/11/2015.
 */
public class TextToSpeak implements TextToSpeech.OnInitListener {

    public static final String END_ID = "END_ID";
    private static final String TAG = "tts";
    private TextToSpeech tts;

    private Context context;
    private String textToSpeech;
    private TextToSpeech.OnUtteranceCompletedListener utteranceCompletedListener;

    public TextToSpeak (Context activity, String text, TextToSpeech.OnUtteranceCompletedListener ucl){
        context = activity;
        utteranceCompletedListener = ucl;
        tts = new TextToSpeech(context, this);
        textToSpeech = text;

    }

    @Override
    public void onInit(int status) {
        tts.setOnUtteranceCompletedListener(utteranceCompletedListener);
        HashMap<String, String> params = new HashMap<String, String>();

        params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, END_ID);

        switch (status){
            case TextToSpeech.ERROR:
                Log.e(TAG, "ERROR ON_INIT: ERROR");
                break;
            case TextToSpeech.SUCCESS:
                int res = tts.setLanguage(Locale.getDefault());
                switch (res){
                    case TextToSpeech.LANG_AVAILABLE:
                        tts.speak(textToSpeech, TextToSpeech.QUEUE_FLUSH, params);
                        break;
                    case TextToSpeech.LANG_COUNTRY_AVAILABLE:
                        tts.speak(textToSpeech,TextToSpeech.QUEUE_FLUSH, params);
                        break;
                    case TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE:
                        tts.speak(textToSpeech, TextToSpeech.QUEUE_FLUSH, params);
                        break;
                    case TextToSpeech.LANG_MISSING_DATA:
                        Log.e(TAG, "ERROR ON_INIT: LANG_MISSING_DATA");
                        break;
                    case TextToSpeech.LANG_NOT_SUPPORTED:
                        Log.e(TAG, "ERROR ON_INIT: LANG_NOT_SUPPORTED");
                        break;
                }
                break;
        }
    }

    public boolean isSpeaking(){
        return tts.isSpeaking();
    }

    public void stop(){
        tts.stop();
    }

    public void destroy(){
        tts.shutdown();
    }

}
