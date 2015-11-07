package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<CardViewObject> mDataset;
    private Context mContext;
    private View.OnClickListener ocl;

    private Button motionButton;
    private Button chargerButton;
    private Button simButton;
    private Button smsButton;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
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
        holder.activateDeactivateButton.setText(mDataset.get(position).getmButtonText());
        holder.activateDeactivateButton.setOnClickListener(ocl);
        switch (position){
            case MOTION_MODE_POSITION:
                holder.activateDeactivateButton.setTag(Data.MOTION_BUTTON_TAG);
                motionButton = holder.activateDeactivateButton;
                break;
            case CHARGER_MODE_POSITION:
                holder.activateDeactivateButton.setTag(Data.CHARGER_BUTTON_TAG);
                chargerButton = holder.activateDeactivateButton;
                break;
            case SIM_MODE_POSITION:
                holder.activateDeactivateButton.setTag(Data.SIM_BUTTON_TAG);
                simButton = holder.activateDeactivateButton;
                break;
            case SMS_MODE_POSITION:
                holder.activateDeactivateButton.setTag(Data.SMS_BUTTON_TAG);
                smsButton = holder.activateDeactivateButton;
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /*@Override
    public int getCount() {
        return mDataset.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null){
            v = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                    .inflate(R.layout.card_view, parent, false);
            v.findViewById(R.id.card_view_background)
                    .setBackgroundColor((mDataset.get(position).getmColor()));
            ((ImageView) v.findViewById(R.id.card_view_image))
                    .setImageResource(mDataset.get(position).getmImage());
            ((TextView) v.findViewById(R.id.card_view_title))
                    .setText(mDataset.get(position).getmTitle());
            ((TextView) v.findViewById(R.id.card_view_description))
                    .setText(mDataset.get(position).getmDescription());
            Button button = ((Button) v.findViewById(R.id.card_view_activate_deactivate_button));
            button.setText(mDataset.get(position).getmButtonText());
            button.setOnClickListener(ocl);

            switch (position){
                case MOTION_MODE_POSITION:
                    motionButton = button;
                    button.setTag(Data.MOTION_BUTTON_TAG);
                    if (Data.isMotionModeActivate()) motionButtonActivated();
                    else motionButtonDeactivated();
                    break;
                case CHARGER_MODE_POSITION:
                    chargerButton = button;
                    button.setTag(Data.CHARGER_BUTTON_TAG);
                    if (Data.isCableModeActivate()) chargerButtonActivated();
                    else chargerButtonDeactivated();
                    break;
                case SIM_MODE_POSITION:
                    simButton = button;
                    button.setTag(Data.SIM_BUTTON_TAG);
                    if (Data.isSimModeActivate()) simButtonActivated();
                    else simButtonDeactivated();
                    break;
                case SMS_MODE_POSITION:
                    smsButton = button;
                    button.setTag(Data.SMS_BUTTON_TAG);
                    if (Data.isSmsModeActivate()) smsButtonActivated();
                    else smsButtonDeactivated();
                    break;
            }
        } else {
            v = convertView;
        }
        return v;
    }*/

    private ArrayList<CardViewObject> getDataSet() {
        ArrayList al = new ArrayList<>();
        al.add(new CardViewObject(mContext.getResources().getColor(R.color.motion_card),
                mContext.getString(R.string.motion_detection_mode),
                mContext.getString(R.string.motion_detection_mode_description),
                mContext.getString(R.string.activate_button), R.drawable.moving_mode));
        al.add(new CardViewObject(mContext.getResources().getColor(R.color.charger_card),
                mContext.getString(R.string.charger_detection_mode),
                mContext.getString(R.string.charger_detection_mode_description),
                mContext.getString(R.string.activate_button), R.drawable.charger_mode));
        al.add(new CardViewObject(mContext.getResources().getColor(R.color.SIM_card),
                mContext.getString(R.string.sim_detection_mode),
                mContext.getString(R.string.sim_detection_mode_description),
                mContext.getString(R.string.activate_button), R.drawable.sim_mode));
        al.add(new CardViewObject(mContext.getResources().getColor(R.color.sms_card),
                mContext.getString(R.string.sms_detection_mode),
                mContext.getString(R.string.sms_detection_mode_description),
                mContext.getString(R.string.activate_button), R.drawable.sms_mode));
        return al;
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////
    public void motionButtonActivated(){
        motionButton.setText(mContext.getString(R.string.deactivate_button));
    }

    public void motionButtonDeactivated(){
        motionButton.setText(mContext.getString(R.string.activate_button));
    }

    public void chargerButtonActivated(){
        chargerButton.setText(mContext.getString(R.string.deactivate_button));
    }

    public void chargerButtonDeactivated(){
        chargerButton.setText(mContext.getString(R.string.activate_button));
    }

    public void simButtonActivated(){
        simButton.setText(mContext.getString(R.string.deactivate_button));
    }

    public void simButtonDeactivated(){
        simButton.setText(mContext.getString(R.string.activate_button));
    }

    public void smsButtonActivated(){
        smsButton.setText(mContext.getString(R.string.deactivate_button));
    }

    public void smsButtonDeactivated(){
        smsButton.setText(mContext.getString(R.string.activate_button));
    }

    public void enableChargerButton(){
        chargerButton.setEnabled(true);
    }

    public void disableChargerButton(){
        chargerButton.setEnabled(false);
    }

}
