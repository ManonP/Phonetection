package com.ihm15.project.phonetection;

import android.media.Image;
import android.widget.ImageView;

/**
 * Created by Manon on 17/10/2015.
 */
public class CardViewObject {

    private int mColor;
    private String mTitle;
    private String mDescription;
    private String mButtonText;
    private int mImage;

    public CardViewObject (int color, String title, String description, String buttonText, int image) {
        mColor = color;
        mTitle = title;
        mDescription = description;
        mButtonText = buttonText;
        mImage = image;
    }


    /*-------------------------getter-------------------------*/

    public int getmColor(){
        return mColor;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmButtonText() {return mButtonText;}

    public int getmImage() {
        return mImage;
    }
}
