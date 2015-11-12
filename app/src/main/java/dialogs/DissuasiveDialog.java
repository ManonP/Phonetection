package dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.ihm15.project.phonetection.R;
import com.ihm15.project.phonetection.TextToSpeak;

import events.DissuasiveDialogSkipObject;

public class DissuasiveDialog extends CustomMessageDialog implements TextToSpeech.OnUtteranceCompletedListener {

    private DissuasiveDialogSkipObject ddso;
    private TextToSpeak tts;

    public DissuasiveDialog(String dialogTitle, String dialogMessage, String positiveButtonText) {
        super(R.drawable.ic_warning_orange_36px, dialogTitle, dialogMessage, positiveButtonText,
                null, null);
        ddso = new DissuasiveDialogSkipObject(this);
    }

    public void addDissuasiveDialogSkipedEventListener(
            DissuasiveDialogSkipObject.DissuasiveDialogSkippedEventListener ul){
        ddso.addDissuasiveDialogSkippedEventListener(ul);
    }

    public void removeDissuasiveDialogSkippedEventListener(
            DissuasiveDialogSkipObject.DissuasiveDialogSkippedEventListener uel){
        ddso.removeDissuasiveDialogSkippedEventListener(uel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tts = new TextToSpeak(getActivity(),
                getActivity().getString(R.string.dissuasive_dialog_message), this);
        //tts.onInit(TextToSpeech.SUCCESS);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        setCancelable(false);
        dialog.hide();

        return dialog;
    }

    @Override
    protected void positiveButtonClicked() {
        ddso.fireDissuasiveDialogSkippedEvent();
        if (tts.isSpeaking()) tts.stop();
        tts.destroy();
        dismiss();
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        Log.d("DD", "ON_UTTERANCE_COMPLETED");
        positiveButtonClicked();
    }
}
