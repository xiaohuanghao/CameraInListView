package com.example.camerainlistview;

/**
 * Created by {Sanjog_Shrestha} on 12/17/2015.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

/**
 * Created by Aneh 3 on 25-06-2015.
 */
public class CustomImageAdapter extends BaseAdapter {

    List<GetSet> _data;
    Context _c;
    ViewHolder v;

    public CustomImageAdapter(List<GetSet> getData, Context context) {
        _data = getData;
        _c = context;
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int position) {
        return _data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater li = (LayoutInflater) _c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = li.inflate(R.layout.parcel_images, null);
        } else {
            view = convertView;
        }

        v = new ViewHolder();
        v.clickImage = (ImageButton) view.findViewById(R.id.capture);
        v.removeImage = (ImageButton) view.findViewById(R.id.cancel);
        v.parcelName = (TextView) view.findViewById(R.id.parcelName);
        v.label = (TextView) view.findViewById(R.id.imageFor);
        v.imageView = (ImageView) view.findViewById(R.id.imgPrv);

        // Set data in listView
        final GetSet dataSet = (GetSet) _data.get(position);

        dataSet.setListItemPosition(position);

        if (!dataSet.isHaveImage()) {
            Bitmap icon = BitmapFactory.decodeResource(_c.getResources(), R.mipmap.ic_launcher);
            v.imageView.setImageBitmap(icon);
        } else {
            v.imageView.setImageBitmap(dataSet.getImage());
        }
        v.parcelName.setText(dataSet.getLabel());
        v.label.setText(dataSet.getSubtext());
        if (dataSet.isStatus()) {
            v.clickImage.setVisibility(View.VISIBLE);
            v.removeImage.setVisibility(View.GONE);
        } else {
            v.removeImage.setVisibility(View.VISIBLE);
            v.clickImage.setVisibility(View.GONE);
        }

        v.clickImage.setFocusable(false);
        v.removeImage.setFocusable(false);


        v.clickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call parent method of activity to click image
                ((MainActivity) _c).captureImage(dataSet.getListItemPosition(), dataSet.getLabel() + "" + dataSet.getSubtext());
            }
        });

        v.removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSet.setStatus(true);
                dataSet.setHaveImage(false);
                notifyDataSetChanged();
            }
        });


        return view;
    }

    /**
     * @param position Get position of of object
     * @param imageSrc set image in imageView
     */
    public void setImageInItem(int position, Bitmap imageSrc, String imagePath) {
        GetSet dataSet = (GetSet) _data.get(position);
        dataSet.setImage(imageSrc);
        dataSet.setStatus(false);
        dataSet.setHaveImage(true);
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView imageView;
        TextView label, parcelName;
        ImageButton clickImage, removeImage;
    }

}
