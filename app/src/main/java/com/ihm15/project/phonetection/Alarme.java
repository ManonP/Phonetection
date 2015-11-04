package com.ihm15.project.phonetection;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

/**
 * Created by Manon on 01/11/2015.
 */
public class Alarme {
    private MediaPlayer player = new MediaPlayer();
    private Context myContext;
    //MyCount counter = new MyCount(10000,1000);

    public void activeAlarame(Context context){
        //On désactive le mode qui a déclencher l'alarme
        switch (Library.WARNING_BY) {
            case 1 :
                Library.DETECTION_MODE = false;
                break;
            case 2 :
                Library.CABLE_MODE = false;
                break;
            case 3 :
                Library.SIM_MODE = false;
                break;
        }
        player = MediaPlayer.create(context,R.raw.pompier);
        player.start();
    }

    public void stopAlarme() {
        if (player.isPlaying()) {
            player.stop();
        }
    }

    public void cancelTimer(){
        mCountDownTImer.cancel();
    }

    public void activeWarningMovment(Context context){
        myContext = context;
        TextToSpeak.speekInstruction("Posez ce téléphone ! Une alarme va se déclencher dans");
        mCountDownTImer.start();
    }

    public void activeWarningCable(Context context) {
        myContext = context;
        TextToSpeak.speekInstruction("Rebranchez ce téléphone ! Une alarme va se délcencher dans");
        mCountDownTImer.start();
    }

    public void activeWarning(Context context) {
        myContext = context;
        TextToSpeak.speekInstruction(myContext.getResources().getString(R.string.dissuasive_dialog_message));
        mCountDownTImer.start();
    }

    CountDownTimer mCountDownTImer = new CountDownTimer(10000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            TextToSpeak.speekInstruction(String.valueOf(millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            while (Library.textToSpeak.isSpeak()) {
            }
            Library.alarme.activeAlarame(myContext);
        }
    };

    //public class MyCount extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        /*public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Library.textToSpeak.speekInstruction(String.valueOf(millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            while (Library.textToSpeak.isSpeak()) {
            }
            Library.alarme.activeAlarame(myContext);
        }


    }*/

}
