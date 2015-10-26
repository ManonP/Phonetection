package dialog;

import android.content.Context;
import android.view.Window;

import com.ihm15.project.phonetection.R;

import adapter.ImageAdapter;

public class ChangeImageDialog extends AbstractImageDialog {

    public ChangeImageDialog(Context context){
        super(context, context.getString(R.string.new_image_choice),
                context.getString(R.string.validate_button));
    }

    @Override
    protected void validateButtonClicked() {
        switch (state){
            case IDLE:
                //INTERDIT
                break;
            case IMAGE_SELECTED:
                ConfirmImageDialog cid = new ConfirmImageDialog(getContext(), image);
                this.dismiss();
                cid.show();
                cid.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_collections_indigo_36px);
                dismiss();
                break;
        }
    }

    @Override
    protected void setGridViewAdapter() {
        gridview.setAdapter(new ImageAdapter(getContext()));
    }


}
