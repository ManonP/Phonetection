package com.ihm15.project.phonetection;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

/**
 * Created by Manon on 01/11/2015.
 */
public class Alarme {
    private MediaPlayer player;
    private Context myContext;

    public void activeAlarame(Context context){
        player = MediaPlayer.create(context,R.raw.pompier);
        player.start();
    }

    public void activeWarning(Context context){
        myContext = context;
        Library.textToSpeak.speekInstruction("Posez ce téléphone ! Une alarme va se déclencher dans");
        MyCount counter = new MyCount(5000,1000);
        counter.start();
    }

    public class MyCount extends CountDownTimer {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            Library.textToSpeak.speekInstruction(String.valueOf(millisUntilFinished/1000));
        }

        @Override
        public void onFinish() {
            while (Library.textToSpeak.isSpeak()) {
            }
            Library.alarme.activeAlarame(myContext);
        }
    }

}
