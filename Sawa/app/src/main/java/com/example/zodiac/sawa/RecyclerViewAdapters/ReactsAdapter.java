package com.example.zodiac.sawa.RecyclerViewAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.zodiac.sawa.GeneralAppInfo;
import com.example.zodiac.sawa.R;
import com.example.zodiac.sawa.RecyclerViewModels.ReactsRecyclerViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Rabee on 1/3/2018.
 */

public class ReactsAdapter extends RecyclerView.Adapter<ReactsAdapter.MyViewHolder>{
    private List<ReactsRecyclerViewModel> reactsList;
    private Context context;
    public ReactsAdapter(Context context,List<ReactsRecyclerViewModel> reactsList){
        this.reactsList=reactsList;
        this.context=context;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.reacts_list_view_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReactsRecyclerViewModel reactsRecyclerViewModel=reactsList.get(position);
        String imageUrl = GeneralAppInfo.SPRING_URL + "/" + reactsRecyclerViewModel.getImage();
        Picasso.with(context).load(imageUrl).into(holder.profilePic);
        holder.fullName.setText(reactsRecyclerViewModel.getName());

    }

    @Override
    public int getItemCount() {
        return reactsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profilePic;
        TextView fullName;

        public MyViewHolder(View view) {
            super(view);
            profilePic = (CircleImageView) itemView.findViewById(R.id.profile_pic);
            fullName = (TextView) itemView.findViewById(R.id.full_name);

        }
    }
}
