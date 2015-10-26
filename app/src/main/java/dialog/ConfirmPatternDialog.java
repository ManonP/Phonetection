package dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Button;

import com.ihm15.project.phonetection.R;

import java.util.List;

import haibison.android.lockpattern.widget.LockPatternView;

public class ConfirmPatternDialog extends AbstractPatternDialog {


    String newPattern;

    public ConfirmPatternDialog(Context context, String newPattern){
        super(context, context.getString(R.string.new_pattern_confirm),
                context.getString(R.string.clear_button),
                context.getString(R.string.confirm_button));
        this.newPattern = newPattern;
    }

    @Override
    protected void validateButtonClicked() {
        switch (state){
            case IDLE:
                //INTERDIT
                break;
            case UPDATING_PATTERN:
                //INTERDIT
                break;
            case PATTERN_COMPLETE:
                this.dismiss();
                if (!pattern.equals(newPattern)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(false);
                    builder.setTitle(R.string.different_pattern_dialog);
                    builder.setMessage(R.string.different_pattern_dialog_message);
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
                    sp.edit().putString(
                            getContext().getString(R.string.pref_key_pattern),
                            pattern);
                    sp.edit().commit();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(false);
                    builder.setTitle(R.string.pattern_saved_dialog);
                    builder.setMessage(R.string.pattern_saved_dialog_message);
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
}
