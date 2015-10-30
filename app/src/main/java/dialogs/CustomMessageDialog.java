package dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.ihm15.project.phonetection.R;

public class CustomMessageDialog extends DialogFragment implements DialogInterface.OnClickListener {

    private Integer iconId;
    private String dialogTitle;
    private String dialogMessage;
    private String positiveButtonText;
    private String negativeButtonText;
    private String neutralButtonText;

    public CustomMessageDialog(Integer iconId, String dialogTitle, String dialogMessage,
                               String positiveButtonText, String negativeButtonText,
                               String neutralButtonText){
        this.iconId = iconId;
        this.dialogTitle = dialogTitle;
        this.dialogMessage = dialogMessage;
        this.positiveButtonText = positiveButtonText;
        this.negativeButtonText = negativeButtonText;
        this.neutralButtonText = neutralButtonText;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog al;
        if (iconId != null)
            builder.setIcon(iconId);
        if (dialogTitle != null)
            builder.setTitle(dialogTitle);
        if (dialogMessage != null)
            builder.setMessage(dialogMessage);
        if (positiveButtonText != null)
            builder.setPositiveButton(positiveButtonText, this);
        if (negativeButtonText != null)
            builder.setNegativeButton(negativeButtonText, this);
        if (neutralButtonText != null)
            builder.setNeutralButton(neutralButtonText, this);
        al = builder.create();
        al.show();
        setButtonTextColor(al);
        al.hide();
        return al;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which){
            case AlertDialog.BUTTON_POSITIVE:
                positiveButtonClicked();
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                negativeButtonClicked();
                break;
            case AlertDialog.BUTTON_NEUTRAL:
                neutralButtonClicked();
                break;
        }
    }

    protected void positiveButtonClicked(){
        this.dismiss();
    }

    protected void negativeButtonClicked(){
        this.dismiss();
    }

    protected void neutralButtonClicked(){
        this.dismiss();
    }

    private void setButtonTextColor(AlertDialog al){
        if(positiveButtonText != null)
            al.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getContext().getResources().getColor(R.color.accent));
        if(negativeButtonText != null)
            al.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getContext().getResources().getColor(R.color.accent));
        if(neutralButtonText != null)
            al.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(getContext().getResources().getColor(R.color.accent));
    }
}
