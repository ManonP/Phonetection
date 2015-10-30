package dialogs;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ihm15.project.phonetection.R;

import adapters.ImageAdapter;

public class ChangeImageDialog extends AbstractImageDialog {

    public ChangeImageDialog(Context context) {
        super(context, context.getString(R.string.new_image_choice), context.getString(R.string.validate_button),
                context.getString(R.string.cancel_button));
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
                state = States.IDLE;

                showConfirmImageDialog();

                disablePositiveButton();
                enableNegativeButton();
                dismiss();
                break;
        }
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    private void showConfirmImageDialog(){
        ConfirmImageDialog cid = new ConfirmImageDialog(context, image);
        cid.show(((FragmentActivity) context).getSupportFragmentManager(), "change_image");
    }

    @Override
    protected void setGridViewAdapter() {
        imagesGrid.setAdapter(new ImageAdapter(context));
    }


}
