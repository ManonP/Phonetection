package adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ihm15.project.phonetection.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mContext.getResources().getDrawable(mThumbIds[position]);
    }

    @Override
    public long getItemId(int position) {
        return mThumbIds[position];
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            int padding = Math.round(mContext.getResources().getDimension(R.dimen.image_margin));
            imageView = new ImageView(mContext);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(padding, padding, padding, padding);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        imageView.setTag(mThumbIds[position]);
        return imageView;
    }

    private Integer[] mThumbIds = {
            R.drawable.books, R.drawable.boussole,
            R.drawable.camera, R.drawable.coffee,
            R.drawable.deer, R.drawable.fauteuil,
            R.drawable.guitar, R.drawable.hot_air_balloon,
            R.drawable.istanbul, R.drawable.ney_york,
            R.drawable.tramway, R.drawable.van_california
    };
}