package com.ihm15.project.phonetection;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Manon on 01/11/2015.
 */
public class Alarme {
    private MediaPlayer player;

    public void activeAlarame(Context context){
        player = MediaPlayer.create(context,R.raw.pompier);
        player.start();
    }

    public void activeWarning(Context context){

    }

}
