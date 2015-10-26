package preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.Window;

import com.ihm15.project.phonetection.R;

import dialog.ChangeImageDialog;

//import dialog.ChangeImageDialog;

public class ImagePreference extends Preference {
    public ImagePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick() {
        ChangeImageDialog cid = new ChangeImageDialog(getContext());
        cid.show();
        cid.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_collections_indigo_36px);
    }
}
