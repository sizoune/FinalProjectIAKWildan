package com.iak.mwi.finalprojectiakwildan.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.iak.mwi.finalprojectiakwildan.Model.Movie;
import com.iak.mwi.finalprojectiakwildan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwi on 5/23/17.
 */

public class AdapterUtama extends RecyclerView.Adapter<AdapterUtama.ViewHolder> {
    private final Context context;
    private ArrayList<Movie> dataMovie;
    OnItemClickListener mItemClickListener;

    public AdapterUtama(Context context, ArrayList<Movie> dataMovie) {
        this.context = context;
        this.dataMovie = dataMovie;
    }


    @Override
    public AdapterUtama.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_gambar, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(context).load(dataMovie.get(position).getPoster()).fit().into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return dataMovie.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView cover;

        public ViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.coverMovie);
            cover.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
