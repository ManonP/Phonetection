package com.ihm15.project.phonetection;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Manon on 17/10/2015.
 */
public class MyRecyclerViewAdapter extends RecyclerView
        .Adapter<MyRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<DataObject> mDataset;
    private static MyClickListener myClickListener;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        LinearLayout background;
        TextView label;
        TextView description;
        Button button;
        ImageView image;

        public DataObjectHolder(View itemView) {
            super(itemView);
            background = (LinearLayout) itemView.findViewById(R.id.card_view_background);
            label = (TextView) itemView.findViewById(R.id.card_view_title);
            description = (TextView) itemView.findViewById(R.id.card_view_description);
            button = (Button) itemView.findViewById(R.id.card_view_activate_deactivate_button);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            //Log.i(LOG_TAG, "Adding Listener");
            button.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        MyRecyclerViewAdapter.myClickListener = myClickListener;
    }

    public MyRecyclerViewAdapter(ArrayList<DataObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.background.setBackgroundColor(mDataset.get(position).getmColor());
        holder.label.setText(mDataset.get(position).getmTitle());
        holder.description.setText(mDataset.get(position).getmDescription());
        holder.button.setText(mDataset.get(position).getmButtonText());
        holder.image.setImageResource(mDataset.get(position).getmImage());
    }

    public void addItem(DataObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }
}
