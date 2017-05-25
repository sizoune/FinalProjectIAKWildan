package com.iak.mwi.finalprojectiakwildan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iak.mwi.finalprojectiakwildan.Model.Trailer;
import com.iak.mwi.finalprojectiakwildan.R;

import java.util.ArrayList;

/**
 * Created by mwi on 5/23/17.
 */

public class AdapterTrailer extends BaseAdapter {
    private Context context;
    private ArrayList<Trailer> dataTrailer;

    public AdapterTrailer(Context context, ArrayList<Trailer> dataTrailer) {
        this.context = context;
        this.dataTrailer = dataTrailer;
    }

    @Override
    public int getCount() {
        return dataTrailer.size();
    }

    @Override
    public Trailer getItem(int position) {
        return dataTrailer.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.list_row_trailer, parent, false);

        TextView nama = (TextView) v.findViewById(R.id.txtNamaTrailer);
        nama.setText(getItem(position).getTitle());

        return v;
    }
}
