package dialogs;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ihm15.project.phonetection.Data;
import com.ihm15.project.phonetection.R;

import adapters.ImageAdapter;
import events.LockSetObject;
import events.UnlockObject;
import events.WrongLockSetObject;
import events.WrongUnlockObject;

/**
 * Created by Dimitri on 07/11/2015.
 */
public class ConfirmImageDialogCardView extends AbstractImageDialog {

    private String newImage;

    private LockSetObject lockSetObject;
    private WrongLockSetObject wrongLockSetObject;

    private LockSetObject.LockSetEventListener lsel;
    private WrongLockSetObject.WrongLockSetEventListener wlsel;
    private int type;


    public ConfirmImageDialogCardView(Context context, String newImage,
                                      LockSetObject.LockSetEventListener lsel,
                                      WrongLockSetObject.WrongLockSetEventListener wlsel,
                                      int type) {
        super(context, context.getString(R.string.new_image_confirm), context.getString(R.string.confirm_button),
                context.getString(R.string.cancel_button));
        this.newImage = newImage;

        this.lsel = lsel;
        this.wlsel = wlsel;
        this.type = type;

        lockSetObject = new LockSetObject(this, type);
        lockSetObject.addLockSetEventListener(lsel);
        wrongLockSetObject = new WrongLockSetObject(this, type);
        wrongLockSetObject.addWrongLockSetEventListener(wlsel);
    }

    //SEEHEIM-NOYAU FONCTIONNEL/////////////////////////////////////////////////////////////////////
    protected void saveImage() {
        Data.getInstance(context);
        Data.setImage(image);
    }

    //SEEHEIM-DIALOGUE//////////////////////////////////////////////////////////////////////////////
    @Override
    protected void positiveButtonClicked() {
        switch (state) {
            case IDLE:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Positive button clicked error: state == IDLE -> FORBIDDEN");
                break;
            case IMAGE_SELECTED:
                if (!image.equals(newImage)) {
                    showWrongImageDialog();
                    dismiss();
                    wrongLockSetObject.fireWrongLockSetEvent();
                } else {
                    saveImage();
                    showSuccessDialog();
                    dismiss();
                    lockSetObject.fireLockSetEvent();
                }
                break;
        }
    }

    @Override
    protected void setGridViewAdapter() {
        imagesGrid.setAdapter(new ImageAdapter(context));
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    protected void showSuccessDialog() {
        CustomMessageDialog cmd = new CustomMessageDialog(R.drawable.ic_check_circle_black_36dp,
                context.getString(R.string.image_saved_dialog), null,
                context.getString(R.string.ok_button), null, null);
        cmd.show(((FragmentActivity) context).getSupportFragmentManager(), "image_change_success");
    }

    protected void showWrongImageDialog() {
        CustomMessageDialog cmd = new CustomMessageDialog(R.drawable.ic_error_white_36px,
                context.getString(R.string.different_image_dialog),
                context.getString(R.string.different_image_dialog_message),
                context.getString(R.string.ok_button), null, null);
        cmd.show(((FragmentActivity) context).getSupportFragmentManager(), "image_change_failure");
    }
}