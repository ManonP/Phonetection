package dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ihm15.project.phonetection.R;

import adapters.ImageAdapter;


public class ConfirmImageDialog extends AbstractImageDialog {
    String newImage;

    public ConfirmImageDialog(Context context, String newImage){
        super(context, context.getString(R.string.new_pattern_confirm),
                context.getString(R.string.confirm_button),
                context.getString(R.string.cancel_button));
        this.newImage = newImage;
    }

    //SEEHEIM-NOYAU FONCTIONNEL/////////////////////////////////////////////////////////////////////

    protected void saveImage() {
        Log.println(Log.DEBUG, "", "SAVE IMAGE: " + image);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(getContext().getString(R.string.pref_key_image), image);
        editor.commit();
    }

    //SEEHEIM-DIALOGUE//////////////////////////////////////////////////////////////////////////////

    @Override
    protected void positiveButtonClicked() {
        switch (state){
            case IDLE:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Positive button clicked error: state == IDLE -> FORBIDDEN");
                break;
            case IMAGE_SELECTED:
                Log.println(Log.DEBUG, "", "IMAGE_SELECTED: " + image + ", " + newImage);
                if (!image.equals(newImage)){
                    state = AbstractImageDialog.States.IDLE;

                    disablePositiveButton();
                    enableNegativeButton();
                    showWrongImageDialog();
                    dismiss();
                } else {
                    state = AbstractImageDialog.States.IDLE;

                    disablePositiveButton();
                    enableNegativeButton();
                    saveImage();
                    showSuccessDialog();
                    dismiss();
                }
        }
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    protected void showSuccessDialog(){
        CustomMessageDialog cmd = new CustomMessageDialog(R.drawable.ic_done_indigo_36px,
                getContext().getString(R.string.image_saved_dialog), null,
                getContext().getString(R.string.ok_button), null, null);
        cmd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "image_change_success");
    }

    protected void showWrongImageDialog(){
        CustomMessageDialog cmd = new CustomMessageDialog(R.drawable.ic_error_outline_red_36px,
                getContext().getString(R.string.different_image_dialog),
                getContext().getString(R.string.different_image_dialog_message),
                getContext().getString(R.string.ok_button), null, null);
        cmd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "image_change_failure");
    }

    @Override
    protected void setGridViewAdapter() {
        imagesGrid.setAdapter(new ImageAdapter(getContext()));
    }
}
