package dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.ihm15.project.phonetection.R;

public abstract class AbstractImageDialog extends DialogFragment
        implements AlertDialog.OnClickListener, AdapterView.OnItemClickListener {

    protected Context context;
    private String dialogTitleText;
    private String positiveButtonText;
    private String negativeButtonText;

    protected AlertDialog.Builder builder;
    protected AlertDialog al;

    protected Button positiveButton;
    protected Button negativeButton;

    protected ImageView selectedImage;

    protected GridView imagesGrid;

    protected enum States {
        IDLE,
        IMAGE_SELECTED
    }
    protected States state;

    protected String image;

    public AbstractImageDialog(Context context, String dialogTitleText,
                                 String positiveButtonText, String negativeButtonText) {
        this.context = context;
        this.dialogTitleText = dialogTitleText;
        this.positiveButtonText = positiveButtonText;
        this.negativeButtonText = negativeButtonText;
        image = null;
        selectedImage = null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder = new AlertDialog.Builder(context);
        builder.setView(inflater.inflate(R.layout.image_layout, null));
        builder.setIcon(R.drawable.ic_apps_indigo_36px);
        builder.setTitle(dialogTitleText);
        builder.setPositiveButton(positiveButtonText, this);
        builder.setNegativeButton(negativeButtonText, this);
        al = builder.create();
        al.show();

        positiveButton = al.getButton(AlertDialog.BUTTON_POSITIVE);
        negativeButton = al.getButton(AlertDialog.BUTTON_NEGATIVE);

        imagesGrid = (GridView) al.findViewById(R.id.image_gridview);

        if (imagesGrid == null)
            Log.println(Log.ERROR, "", "imagesGrid == NULL");

        setGridViewAdapter();

        setDialogButtonTextColor();
        imagesGrid.setOnItemClickListener(this);


        init();

        al.hide();

        return al;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        imageClicked(view);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE:
                positiveButtonClicked();
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                negativeButtonClicked();
                break;
        }
    }

    //SEEHEIM-NOYAU FONCTIONNEL/////////////////////////////////////////////////////////////////////
    public String imageViewToString(ImageView iv){
        return iv.getTag().toString();
    }

    //SEEHEIM-DIALOGUE//////////////////////////////////////////////////////////////////////////////

    protected void init(){
        state = States.IDLE;
        image = "";

        disablePositiveButton();
        enableNegativeButton();
    }

    protected abstract void positiveButtonClicked();

    protected void negativeButtonClicked(){
        switch (state) {
            case IDLE:
                state = States.IDLE;

                disablePositiveButton();
                enableNegativeButton();
                dismiss();
                break;
            case IMAGE_SELECTED:
                state = States.IDLE;

                disablePositiveButton();
                enableNegativeButton();
                dismiss();
                break;
        }
    }

    private void imageClicked(View view){
        switch(state){
            case IDLE:
                state = States.IMAGE_SELECTED;
                image = imageViewToString((ImageView) view);

                selectImage((ImageView) view);

                enablePositiveButton();
                enableNegativeButton();
                break;
            case IMAGE_SELECTED:
                state = States.IMAGE_SELECTED;
                image = imageViewToString((ImageView) view);

                selectImage((ImageView) view);

                enablePositiveButton();
                enableNegativeButton();
                break;
        }
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    protected void setDialogButtonTextColor (){
        positiveButton.setTextColor(context.getResources().getColor(R.color.accent));
        negativeButton.setTextColor(context.getResources().getColor(R.color.accent));
    }

    protected void enablePositiveButton(){ positiveButton.setEnabled(true); }

    protected void disablePositiveButton(){ positiveButton.setEnabled(false); }

    protected void enableNegativeButton(){ negativeButton.setEnabled(true); }

    protected void disableNegativeButton(){ negativeButton.setEnabled(false); }

    protected abstract void setGridViewAdapter();

    protected void selectImage(ImageView iv){
        if (selectedImage != null) {
            selectedImage.setBackgroundColor(context.getResources().getColor(R.color.transparent));
        }
        iv.setBackgroundColor(context.getResources().getColor(R.color.accent));
        selectedImage = iv;
    }
}
