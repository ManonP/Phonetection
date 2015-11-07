package dialogs;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ihm15.project.phonetection.R;

import adapters.ImageAdapter;
import events.LockSetObject;
import events.WrongLockSetObject;

/**
 * Created by Dimitri on 07/11/2015.
 */
public class ChangeImageDialogCardView extends AbstractImageDialog {

    private LockSetObject.LockSetEventListener lsel;
    private WrongLockSetObject.WrongLockSetEventListener wlsel;
    private int type;

    public ChangeImageDialogCardView(Context context, LockSetObject.LockSetEventListener lsel,
                                     WrongLockSetObject.WrongLockSetEventListener wlsel, int type) {
        super(context, context.getString(R.string.new_image_choice), context.getString(R.string.validate_button),
                context.getString(R.string.cancel_button));
        this.type = type;
        this.lsel = lsel;
        this.wlsel = wlsel;
    }

    @Override
    protected void setGridViewAdapter() {
        imagesGrid.setAdapter(new ImageAdapter(context));
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
                dismiss();
                showConfirmImageDialog();
        }
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    private void showConfirmImageDialog(){
        ConfirmImageDialogCardView cid = new ConfirmImageDialogCardView(context, image, lsel, wlsel, type);
        cid.show(((FragmentActivity) context).getSupportFragmentManager(), "change_image");
    }
}
