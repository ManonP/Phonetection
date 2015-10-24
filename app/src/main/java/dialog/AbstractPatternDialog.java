package dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.ihm15.project.phonetection.R;

import java.util.Iterator;
import java.util.List;

import haibison.android.lockpattern.widget.LockPatternView;

public abstract class AbstractPatternDialog extends Dialog
        implements View.OnClickListener, LockPatternView.OnPatternListener{
    private String dialogTitleText;
    private String clearButtonText;
    private String validateButtonText;

    private LockPatternView lockPatternView;
    private Button clearButton;
    private Button validateButton;
    private Button cancelButton;

    protected enum States {
        IDLE,
        UPDATING_PATTERN,
        PATTERN_COMPLETE
    }
    protected States state;
    private int cellNb;

    protected String pattern;

    public AbstractPatternDialog(Context context, String dialogTitleText,
                                 String clearButtonText,
                                 String validateButtonText) {
        super(context);
        this.dialogTitleText = dialogTitleText;
        this.clearButtonText = clearButtonText;
        this.validateButtonText = validateButtonText;
        pattern = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.pattern_layout);

        this.setTitle(dialogTitleText);

        Context context = new ContextThemeWrapper(getContext(), haibison.android.lockpattern.R.style.Alp_42447968_ThemeResources_Light);
        lockPatternView = new LockPatternView(context);
        lockPatternView.setTactileFeedbackEnabled(true);
        LinearLayout ll = (LinearLayout) findViewById(R.id.pattern_container);
        ll.addView(lockPatternView);
        clearButton = (Button) findViewById(R.id.pattern_clear_button);
        cancelButton = (Button) findViewById(R.id.pattern_cancel_button);
        validateButton = (Button) findViewById(R.id.pattern_validate_button);

        clearButton.setOnClickListener(this);
        clearButton.setText(clearButtonText);
        cancelButton.setOnClickListener(this);
        validateButton.setOnClickListener(this);
        validateButton.setText(validateButtonText);
        lockPatternView.setOnPatternListener(this);
        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pattern_clear_button:
                clearButtonClicked();
                break;
            case R.id.pattern_cancel_button:
                cancelButtonClicked();
                break;
            case R.id.pattern_validate_button:
                validateButtonClicked();
                break;
        }
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

    private void init(){
        state = States.IDLE;
        cellNb = 0;
        pattern = "";

        disableValidateButton();
        disableClearButton();
    }

    protected abstract void validateButtonClicked();

    private void cancelButtonClicked(){
        switch (state){
            case IDLE:
                dismiss();
                break;
            case UPDATING_PATTERN:
                dismiss();
                break;
            case PATTERN_COMPLETE:
                dismiss();
                break;
        }
    }

    private void clearButtonClicked(){
        switch (state){
            case IDLE:
                //INTERDIT
                break;
            case UPDATING_PATTERN:
                state = States.IDLE;

                erasePattern();
                disableValidateButton();
                disableClearButton();
                break;
            case PATTERN_COMPLETE:
                state = States.IDLE;

                erasePattern();
                disableValidateButton();
                disableClearButton();
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
                Log.println(Log.ERROR, "", "IMPOSSIBLE_PATTERN_STARTED");
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

                disableClearButton();
                disableValidateButton();
                break;
            case UPDATING_PATTERN:
                state = States.UPDATING_PATTERN;
                cellNb++;

                disableClearButton();
                disableValidateButton();
                break;
            case PATTERN_COMPLETE:
                state = States.PATTERN_COMPLETE;
                cellNb++;

                disableClearButton();
                disableValidateButton();
                break;
        }
    }

    private void patternDetected(List<LockPatternView.Cell> detectedPattern){
        switch (state){
            case IDLE:
                //IMPOSSIBLE
                Log.println(Log.ERROR, "", "IMPOSSIBLE_PATTERN_DETECTED");
                break;
            case UPDATING_PATTERN:
                if (cellNb >= 4) {
                    state = States.PATTERN_COMPLETE;
                    cellNb = 0;

                    pattern = patternToString(detectedPattern);
                    Log.println(Log.DEBUG, "", pattern);
                    enableValidateButton();
                    enableClearButton();
                } else {
                    state = States.IDLE;
                    cellNb = 0;

                    lockPatternView.clearPattern();

                    disableValidateButton();
                    disableClearButton();
                }
                break;
            case PATTERN_COMPLETE:
                //IMPOSSIBLE
                Log.println(Log.ERROR, "", "IMPOSSIBLE_PATTERN_DETECTED");
                break;
        }
    }

    //Presentation
    private void erasePattern(){
        lockPatternView.clearPattern();
    }

    private void disableClearButton(){
        clearButton.setEnabled(false);
    }

    private void enableClearButton(){
        clearButton.setEnabled(true);
    }

    private void disableValidateButton(){
        validateButton.setEnabled(false);
    }

    private void enableValidateButton(){
        validateButton.setEnabled(true);
    }

    public static String patternToString(List<LockPatternView.Cell> list){
        String stringPattern = "";
        for (LockPatternView.Cell aList : list) stringPattern += (aList).getId();

        return stringPattern;
    }
}
