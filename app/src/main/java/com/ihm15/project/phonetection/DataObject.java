package com.ihm15.project.phonetection;

import android.media.Image;
import android.widget.ImageView;

/**
 * Created by Manon on 17/10/2015.
 */
public class DataObject {

    private int mColor;
    private String mTitle;
    private String mDescription;
    private String mButtonText;
    private int mImage;

    DataObject (int color, String title, String description, String buttonText, int image) {
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

    /*-------------------------setter-------------------------*/

    public void setmColor(int mColor){
        this.mColor = mColor;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmDescription (String mDescription) {
        this.mDescription = mDescription;
    }

    public void setmButtonText (String mButtonText) { this.mButtonText = mButtonText;}

    public void setmImage(int mImage){
        this.mImage = mImage;
    }
}
