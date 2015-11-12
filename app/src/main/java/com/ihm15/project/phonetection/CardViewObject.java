package com.ihm15.project.phonetection;

/**
 * Created by Manon on 17/10/2015.
 */
public class CardViewObject {

    private int mColor;
    private String mTitle;
    private String mDescription;
    private int mImage;

    public CardViewObject (int color, String title, String description, int image) {
        mColor = color;
        mTitle = title;
        mDescription = description;
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

    public int getmImage() {
        return mImage;
    }
}
