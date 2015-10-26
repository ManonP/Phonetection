package dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.ihm15.project.phonetection.R;


public abstract class AbstractImageDialog extends Dialog implements View.OnClickListener,
        AdapterView.OnItemClickListener{

    private String dialogTitleText;
    private String validateButtonText;

    private Button validateButton;
    private Button cancelButton;

    private ImageView selectedImage;
    protected GridView gridview;

    protected enum States {
        IDLE,
        IMAGE_SELECTED
    }
    protected States state;

    protected String image;

    public AbstractImageDialog(Context context, String dialogTitleText,
                                 String validateButtonText) {
        super(context);
        this.dialogTitleText = dialogTitleText;
        this.validateButtonText = validateButtonText;
        this.image = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.image_layout);

        this.setTitle(dialogTitleText);


        cancelButton = (Button) findViewById(R.id.image_cancel_button);
        validateButton = (Button) findViewById(R.id.image_validate_button);

        gridview = (GridView) findViewById(R.id.image_gridview);
        setGridViewAdapter();

        cancelButton.setOnClickListener(this);
        validateButton.setOnClickListener(this);
        validateButton.setText(validateButtonText);
        gridview.setOnItemClickListener(this);

        selectedImage = null;
        init();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_cancel_button:
                cancelButtonClicked();
                break;
            case R.id.image_validate_button:
                validateButtonClicked();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        imageClicked(view);
    }

    private void init(){
        state = States.IDLE;
        image = "";

        disableValidateButton();
    }

    protected abstract void validateButtonClicked();

    private void cancelButtonClicked(){
        switch (state){
            case IDLE:
                dismiss();
                break;
            case IMAGE_SELECTED:
                dismiss();
                break;
        }
    }

    private void imageClicked(View view){
        switch(state){
            case IDLE:
                state = States.IMAGE_SELECTED;
                setImageBackgroundColor(view);
                image = view.toString();
                selectedImage = (ImageView) view;
                enableValidateButton();
                break;
            case IMAGE_SELECTED:
                if (view.equals(selectedImage)){
                    state = States.IDLE;
                    setImageBackgroundColor(view);
                    image = view.toString();
                    selectedImage = null;
                    disableValidateButton();
                } else {
                    state = States.IMAGE_SELECTED;
                    setImageBackgroundColor(view);
                    image = view.toString();
                    selectedImage = (ImageView) view;
                    enableValidateButton();
                }
                break;

        }
    }

    //Presentation
    private void disableValidateButton(){
        validateButton.setEnabled(false);
    }

    private void enableValidateButton(){
        validateButton.setEnabled(true);
    }

    private void setImageBackgroundColor(View view){
        view.setBackgroundColor(getContext().getResources().getColor(R.color.accent));
        if(selectedImage != null){
            selectedImage.setBackgroundColor(getContext().getResources().getColor(R.color.transparent));
        }
    }

    protected abstract void setGridViewAdapter();

}
