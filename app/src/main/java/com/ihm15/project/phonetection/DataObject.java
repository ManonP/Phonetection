package com.ihm15.project.phonetection;

import android.media.Image;
import android.widget.ImageView;

/**
 * Created by Manon on 17/10/2015.
 */
public class DataObject {

    private String mText1;
    private String mText2;
    private String mActivation;
    private int mImage;

    DataObject (String text1, String text2, String activation, int image) {
        mText1 = text1;
        mText2 = text2;
        mActivation = activation;
        mImage = image;
    }


    /*-------------------------getter-------------------------*/

    public String getmText1() {
        return mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public String getmActivation() {return mActivation;}

    public int getmImage() {
        return mImage;
    }

    /*-------------------------setter-------------------------*/
    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public void setmText2 (String mText2) {
        this.mText2 = mText2;
    }

    public void setmActivation (String mActivation) { this.mActivation = mActivation;}

    public void setmImage(int mImage){
        this.mImage = mImage;
    }
}
