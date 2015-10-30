package preferences;

import android.content.Context;
import android.preference.Preference;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;

import dialogs.ChangeImageDialog;

public class ImagePreference extends Preference {
    public ImagePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick() {
        ChangeImageDialog cid = new ChangeImageDialog(getContext());
        cid.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "choose_image");
    }
}
