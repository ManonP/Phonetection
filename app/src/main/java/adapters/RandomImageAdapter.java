package adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ihm15.project.phonetection.R;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RandomImageAdapter extends BaseAdapter {
    private Context mContext;

    public RandomImageAdapter(Context c) {
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
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

    // references to our images
    private Integer[] mThumbIds = randomThumbIds();

    private Integer[] randomThumbIds(){
        Integer[] mThumb = {
                R.drawable.books, R.drawable.boussole,
                R.drawable.camera, R.drawable.coffee,
                R.drawable.deer, R.drawable.fauteuil,
                R.drawable.guitar, R.drawable.hot_air_balloon,
                R.drawable.istanbul, R.drawable.ney_york,
                R.drawable.tramway, R.drawable.van_california
        };
        List<Integer> ci = Arrays.asList(mThumb);
        Collections.shuffle(ci);
        return (Integer[]) ci.toArray();
    }
}