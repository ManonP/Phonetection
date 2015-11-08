package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihm15.project.phonetection.CardViewObject;
import com.ihm15.project.phonetection.Data;
import com.ihm15.project.phonetection.R;

import java.util.ArrayList;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {

    private static final int MOTION_MODE_POSITION = 0;
    private static final int CHARGER_MODE_POSITION = 1;
    private static final int SIM_MODE_POSITION = 2;
    private static final int SMS_MODE_POSITION = 3;

    private ArrayList<CardViewObject> mDataset;
    private Context mContext;
    private View.OnClickListener ocl;

    private Button motionButton;
    private Button chargerButton;
    private Button simButton;
    private Button smsButton;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout background;
        public ImageView image;
        public TextView title;
        public TextView description;
        public Button activateDeactivateButton;

        public ViewHolder(View v) {
            super(v);
            background = (LinearLayout) v.findViewById(R.id.card_view_background);
            image = (ImageView) v.findViewById(R.id.card_view_image);
            title = (TextView) v.findViewById(R.id.card_view_title);
            description = (TextView) v.findViewById(R.id.card_view_description);
            activateDeactivateButton = (Button) v.findViewById(R.id.card_view_activate_deactivate_button);
        }
    }

    public CardViewAdapter(Context context, View.OnClickListener ocl){
        mContext = context;
        mDataset = getDataSet();
        this.ocl = ocl;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.background.setBackgroundColor(mDataset.get(position).getmColor());
        holder.image.setImageResource(mDataset.get(position).getmImage());
        holder.title.setText(mDataset.get(position).getmTitle());
        holder.description.setText(mDataset.get(position).getmDescription());
        holder.activateDeactivateButton.setOnClickListener(ocl);
        switch (position){
            case MOTION_MODE_POSITION:
                holder.activateDeactivateButton.setTag(Data.MOTION_BUTTON_TAG);
                motionButton = holder.activateDeactivateButton;
                if (Data.isMotionModeActivate()) motionButtonActivated();
                else motionButtonDeactivated();
                break;
            case CHARGER_MODE_POSITION:
                holder.activateDeactivateButton.setTag(Data.CHARGER_BUTTON_TAG);
                chargerButton = holder.activateDeactivateButton;
                if (Data.isCableModeActivate()) chargerButtonActivated();
                else chargerButtonDeactivated();
                break;
            case SIM_MODE_POSITION:
                holder.activateDeactivateButton.setTag(Data.SIM_BUTTON_TAG);
                simButton = holder.activateDeactivateButton;
                if (Data.isSimModeActivate()) simButtonActivated();
                else simButtonDeactivated();
                break;
            case SMS_MODE_POSITION:
                holder.activateDeactivateButton.setTag(Data.SMS_BUTTON_TAG);
                smsButton = holder.activateDeactivateButton;
                if (Data.isSmsModeActivate()) smsButtonActivated();
                else smsButtonDeactivated();
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private ArrayList<CardViewObject> getDataSet() {
        ArrayList al = new ArrayList<>();
        al.add(new CardViewObject(mContext.getResources().getColor(R.color.motion_card),
                mContext.getString(R.string.motion_detection_mode),
                mContext.getString(R.string.motion_detection_mode_description), R.drawable.moving_mode));
        al.add(new CardViewObject(mContext.getResources().getColor(R.color.charger_card),
                mContext.getString(R.string.charger_detection_mode),
                mContext.getString(R.string.charger_detection_mode_description), R.drawable.charger_mode));
        al.add(new CardViewObject(mContext.getResources().getColor(R.color.SIM_card),
                mContext.getString(R.string.sim_detection_mode),
                mContext.getString(R.string.sim_detection_mode_description), R.drawable.sim_mode));
        al.add(new CardViewObject(mContext.getResources().getColor(R.color.sms_card),
                mContext.getString(R.string.sms_detection_mode),
                mContext.getString(R.string.sms_detection_mode_description), R.drawable.sms_mode));
        return al;
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////
    public void motionButtonActivated(){
        Log.d("", "DEBUG: MOTION BUTTON ACTIVATED");
        motionButton.setText(mContext.getString(R.string.deactivate_button));
    }

    public void motionButtonDeactivated(){
        Log.d("", "DEBUG: MOTION BUTTON DEACTIVATED");
        motionButton.setText(mContext.getString(R.string.activate_button));
    }

    public void chargerButtonActivated(){
        Log.d("", "DEBUG: CHARGER BUTTON ACTIVATED");
        chargerButton.setText(mContext.getString(R.string.deactivate_button));
    }

    public void chargerButtonDeactivated(){
        Log.d("", "DEBUG: CHARGER BUTTON DEACTIVATED");
        chargerButton.setText(mContext.getString(R.string.activate_button));
    }

    public void simButtonActivated(){
        Log.d("", "DEBUG: SIM BUTTON ACTIVATED");
        simButton.setText(mContext.getString(R.string.deactivate_button));
    }

    public void simButtonDeactivated(){
        Log.d("", "DEBUG: SIM BUTTON DEACTIVATED");
        simButton.setText(mContext.getString(R.string.activate_button));
    }

    public void smsButtonActivated(){
        Log.d("", "DEBUG: SMS BUTTON ACTIVATED");
        smsButton.setText(mContext.getString(R.string.deactivate_button));
    }

    public void smsButtonDeactivated(){
        Log.d("", "DEBUG: SMS BUTTON DEACTIVATED");
        smsButton.setText(mContext.getString(R.string.activate_button));
    }

    public void enableChargerButton(){
        chargerButton.setEnabled(true);
    }

    public void disableChargerButton(){
        chargerButton.setEnabled(false);
    }

}
