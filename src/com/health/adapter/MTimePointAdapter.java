package com.health.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.health.data.IFeeling;
import com.health.main.R;

public class MTimePointAdapter extends ArrayAdapter<IFeeling> {
	
    private final Context context;
    
    private final List<IFeeling> values;
    
    private int mBackgroundColor;
    
    private int mSelectedPosition = AdapterView.INVALID_POSITION;

    public MTimePointAdapter(Context context, List<IFeeling> values, int backgroundColor) {
        super(context, R.layout.item_timepoint, values);
        this.context = context;
        this.values = values;
        this.mBackgroundColor = backgroundColor;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_timepoint, parent, false);
        TextView tv = (TextView) rowView.findViewById(R.id.tvTimePoint);
        tv.setTextColor(Color.BLACK);
        IFeeling feeling = values.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        tv.setText(sdf.format(feeling.getStartDate()));
        if(mSelectedPosition == position){
        	rowView.setBackgroundColor(mBackgroundColor);
        }        
        return rowView;
    }
    
    public void setSelectedPosition(int position){
    	this.mSelectedPosition = position;
    	notifyDataSetChanged();
    }

   
}
