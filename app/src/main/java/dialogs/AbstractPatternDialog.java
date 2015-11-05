package dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ihm15.project.phonetection.R;

import java.util.List;

import haibison.android.lockpattern.widget.LockPatternView;

public abstract class AbstractPatternDialog extends DialogFragment
        implements AlertDialog.OnClickListener, LockPatternView.OnPatternListener{

    protected Context context;
    private String dialogTitleText;
    private String positiveButtonText;
    private String negativeButtonText;

    protected AlertDialog.Builder builder;
    protected AlertDialog al;

    protected Button positiveButton;
    protected Button negativeButton;

    private LockPatternView lockPatternView;

    protected enum States {
        IDLE,
        UPDATING_PATTERN,
        PATTERN_COMPLETE
    }
    protected States state;
    private int cellNb;

    protected int pattern;

    public AbstractPatternDialog(Context context, String dialogTitleText,
                                 String positiveButtonText, String negativeButtonText) {
        this.context = context;
        this.dialogTitleText = dialogTitleText;
        this.positiveButtonText = positiveButtonText;
        this.negativeButtonText = negativeButtonText;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder = new AlertDialog.Builder(context);
        builder.setView(inflater.inflate(R.layout.pattern_layout, null));
        builder.setIcon(R.drawable.ic_apps_black_36dp);
        builder.setTitle(dialogTitleText);
        builder.setPositiveButton(positiveButtonText, this);
        builder.setNegativeButton(negativeButtonText, this);
        al = builder.create();
        al.show();

        positiveButton = al.getButton(AlertDialog.BUTTON_POSITIVE);
        negativeButton = al.getButton(AlertDialog.BUTTON_NEGATIVE);

        setDialogButtonTextColor();

        Context c = new ContextThemeWrapper(context, haibison.android.lockpattern.R.style.Alp_42447968_ThemeResources_Light);
        lockPatternView = new LockPatternView(c);
        lockPatternView.setTactileFeedbackEnabled(true);
        LinearLayout ll = (LinearLayout) al.findViewById(R.id.pattern_container);
        ll.addView(lockPatternView);

        lockPatternView.setOnPatternListener(this);
        init();

        al.hide();

        return al;
    }

    @Override
    public void onPatternStart() {
        patternStarted();
    }

    @Override
    public void onPatternCleared() {}

    @Override
    public void onPatternCellAdded(List<LockPatternView.Cell> list) {
        patternCellAdded();
    }

    @Override
    public void onPatternDetected(List<LockPatternView.Cell> list) {
        patternDetected(list);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE:
                positiveButtonClicked();
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                negativeButtonClicked();
                break;
        }
    }

    //SEEHEIM-NOYAU FONCTIONNEL/////////////////////////////////////////////////////////////////////
    public static int patternToInteger(List<LockPatternView.Cell> list){
        int intPattern = 0;
        for (LockPatternView.Cell aList : list) intPattern = intPattern*10 + (aList).getId();

        return intPattern;
    }

    //SEEHEIM-DIALOGUE//////////////////////////////////////////////////////////////////////////////

    protected void init(){
        state = States.IDLE;
        pattern = 0;
        cellNb = 0;

        disablePositiveButton();
        enableNegativeButton();
    }

    protected abstract void positiveButtonClicked();

    protected void negativeButtonClicked(){
        switch (state) {
            case IDLE:
                state = States.IDLE;


                disablePositiveButton();
                enableNegativeButton();
                dismiss();
                break;
            case UPDATING_PATTERN:
                state = States.IDLE;

                disablePositiveButton();
                enableNegativeButton();
                dismiss();
                break;
            case PATTERN_COMPLETE:
                state = States.IDLE;

                disablePositiveButton();
                enableNegativeButton();
                dismiss();
                break;
        }
    }

    private void patternStarted(){
        switch(state){
            case IDLE:
                state = States.UPDATING_PATTERN;
                break;
            case UPDATING_PATTERN:
                //IMPOSSIBLE
                Log.println(Log.ERROR, "",
                        "Pattern started error: state = UPDATING_PATTERN -> IMPOSSIBLE");
                break;
            case PATTERN_COMPLETE:
                state = States.UPDATING_PATTERN;

                break;
        }
    }

    private void patternCellAdded(){
        switch (state){
            case IDLE:
                state = States.IDLE;
                cellNb++;

                disablePositiveButton();
                enableNegativeButton();
                break;
            case UPDATING_PATTERN:
                state = States.UPDATING_PATTERN;
                cellNb++;

                disablePositiveButton();
                enableNegativeButton();
                break;
            case PATTERN_COMPLETE:
                state = States.PATTERN_COMPLETE;
                cellNb++;

                disablePositiveButton();
                enableNegativeButton();
                break;
        }
    }

    private void patternDetected(List<LockPatternView.Cell> detectedPattern){
        switch (state){
            case IDLE:
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pattern detected error: state == IDLE -> FORBIDDEN");
                break;
            case UPDATING_PATTERN:
                if (cellNb >= 4) {
                    state = States.PATTERN_COMPLETE;
                    cellNb = 0;

                    pattern = patternToInteger(detectedPattern);
                    Log.println(Log.DEBUG, "", "" + pattern);
                    enablePositiveButton();
                    enableNegativeButton();
                } else {
                    state = States.IDLE;
                    cellNb = 0;

                    lockPatternView.clearPattern();

                    disablePositiveButton();
                    enableNegativeButton();
                }
                break;
            case PATTERN_COMPLETE:
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pattern detected error: state == PATTERN_COMPLETE -> FORBIDDEN");
                break;
        }
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    protected void setDialogButtonTextColor (){
        positiveButton.setTextColor(context.getResources().getColor(R.color.accent));
        negativeButton.setTextColor(context.getResources().getColor(R.color.accent));
    }

    protected void enablePositiveButton(){ positiveButton.setEnabled(true); }

    protected void disablePositiveButton(){ positiveButton.setEnabled(false); }

    protected void enableNegativeButton(){ negativeButton.setEnabled(true); }

    protected void disableNegativeButton(){ negativeButton.setEnabled(false); }
}
