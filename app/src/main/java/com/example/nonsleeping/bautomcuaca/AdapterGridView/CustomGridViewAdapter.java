package com.example.nonsleeping.bautomcuaca.AdapterGridView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.nonsleeping.bautomcuaca.MainActivity;
import com.example.nonsleeping.bautomcuaca.ObjectGrid.ObjectGridCustom;
import com.example.nonsleeping.bautomcuaca.R;

import java.util.ArrayList;

public class CustomGridViewAdapter extends ArrayAdapter<ObjectGridCustom> {
    private Activity context;
    private ArrayList<ObjectGridCustom> mThumbIds;
    private int mLayoutInflater;
    ArrayAdapter<Integer> mAdater;
    Integer[] giatien = {0, 100, 200, 300, 400, 500};

    public CustomGridViewAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @SuppressLint("ResourceType")
    public CustomGridViewAdapter(Activity context, int mLayoutInflater, ArrayList<ObjectGridCustom> mThumbIds) {
        super(context, mLayoutInflater, mThumbIds);

        this.context = context;
        this.mLayoutInflater = mLayoutInflater;
        this.mThumbIds = mThumbIds;
        mAdater = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, giatien);
    }

    @Override
    public int getCount() {
        return mThumbIds.size();
    }

    @Override
    public long getItemId(int position) {
        return mThumbIds.get(position).getmIdImage();
    }

    @Override
    public ObjectGridCustom getItem(int position) {
        return mThumbIds.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewTextHolder viewTextHolder;
        final int pos = position;
        if(convertView==null){
            LayoutInflater inflater = context.getLayoutInflater();

            convertView = inflater.inflate(mLayoutInflater, null);
            viewTextHolder = new ViewTextHolder();
            viewTextHolder.mImageView = (ImageView) convertView.findViewById(R.id.imgGrid);
            viewTextHolder.mSpiner = (Spinner) convertView.findViewById(R.id.spinGiaCuoc);

            //Can chinh anh
           /* viewTextHolder.mImageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            viewTextHolder.mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            viewTextHolder.mImageView.setPadding(8, 8, 8, 8);*/

            viewTextHolder.mImageView.setImageResource(mThumbIds.get(position).getmIdImage());
            viewTextHolder.mSpiner.setAdapter(mAdater);
            viewTextHolder.mSpiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int positionSP, long id) {
                    MainActivity.gtDatCuoc[pos] = giatien[positionSP];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


        }else{
            viewTextHolder = (ViewTextHolder) convertView.getTag();
        }
        return convertView;
    }

    private class ViewTextHolder {
        private ImageView mImageView;
        private Spinner mSpiner;
    }
}
