package com.example.caloric.favourites.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.caloric.R;
import com.example.caloric.model.Category;
import com.example.caloric.model.Meal;


import java.util.ArrayList;

public class FavouriteRecyclerAdapter extends RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder> {
    private ArrayList<Meal> myList = new ArrayList<>();
    private Context context;
    OnClickFavouriteInterface onClickFavouriteInterface;


    public FavouriteRecyclerAdapter(Context context, OnClickFavouriteInterface onClickFavouriteInterface){
        this.context = context;
        this.onClickFavouriteInterface = onClickFavouriteInterface;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavouriteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        Meal favlist = myList.get(position);

        holder.saveBtn.setImageResource(R.drawable.heart_fill);
        Glide.with(context).load(myList.get(position).getStrMealThumb())
                .apply(new RequestOptions().override(500,500)
                        .error(R.drawable.meals)).into(holder.mealImg);
        holder.mealName.setText(favlist.getStrMeal());

        holder.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFavouriteInterface.onDeleteBtnClicked(myList.get(holder.getAdapterPosition()));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickFavouriteInterface.onFavItemClicked(myList.get(holder.getAdapterPosition()).getIdMeal());
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public void setList(ArrayList<Meal> myList) {
        this.myList = myList;
        Log.d("FavouriteRecyclerAdapter", "List updated: " + myList.size() + " meals");
        notifyDataSetChanged();
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder {

        ImageView mealImg, saveBtn;
        TextView mealName;

        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImg = itemView.findViewById(R.id.mealImg);
            mealName = itemView.findViewById(R.id.mealName);
            saveBtn = itemView.findViewById(R.id.addToFavouriteBtn);

        }
    }
}
