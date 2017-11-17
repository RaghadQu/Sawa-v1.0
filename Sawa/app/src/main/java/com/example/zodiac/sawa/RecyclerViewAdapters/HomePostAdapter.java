package com.example.zodiac.sawa.RecyclerViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Rabee on 11/17/2017.
 */

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.RecyclerViewHolder> {
    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder  {
        public RecyclerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
