package dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Button;

import com.ihm15.project.phonetection.R;

import adapter.ImageAdapter;


public class ConfirmImageDialog extends AbstractImageDialog {
    String newImage;

    public ConfirmImageDialog(Context context, String newImage){
        super(context, context.getString(R.string.new_image_confirm),
                context.getString(R.string.confirm_button));
        this.newImage = newImage;
    }

    @Override
    protected void validateButtonClicked() {
        switch (state){
            case IDLE:
                //INTERDIT
                break;
            case IMAGE_SELECTED:
                this.dismiss();
                if (!image.equals(newImage)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(false);
                    builder.setTitle(R.string.different_image_dialog);
                    builder.setMessage(R.string.different_image_dialog_message);
                    builder.setPositiveButton(R.string.ok_button, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog ad = builder.create();
                    ad.show();
                    Button button = ad.getButton(AlertDialog.BUTTON_POSITIVE);
                    button.setTextColor(getContext().getResources().getColor(R.color.accent));
                } else {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                    sp.edit().putString(getContext().getString(R.string.pref_key_image), image);
                    sp.edit().commit();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(false);
                    builder.setTitle(R.string.image_saved_dialog);
                    builder.setMessage(R.string.image_saved_dialog_message);
                    builder.setPositiveButton(R.string.ok_button, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog ad = builder.create();
                    ad.show();
                    Button button = ad.getButton(AlertDialog.BUTTON_POSITIVE);
                    button.setTextColor(getContext().getResources().getColor(R.color.accent));
                }
        }
    }

    @Override
    protected void setGridViewAdapter() {
        gridview.setAdapter(new ImageAdapter(getContext()));
    }
}
