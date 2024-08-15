package com.example.caloric.home.view;

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
import com.example.caloric.model.Meal;

import java.util.ArrayList;

public class DailyRecyclerAdapter extends RecyclerView.Adapter<DailyRecyclerAdapter.DailyViewHolder> {
    private ArrayList<Meal> myList;
    private Context context;
    private OnClickInterface onClickInterface;

    public DailyRecyclerAdapter(Context context, OnClickInterface onClickInterface, ArrayList<Meal> myList) {
        this.context = context;
        this.onClickInterface = onClickInterface;
        this.myList = myList;
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DailyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        Meal currentMeal = myList.get(position);

        holder.mealName.setText(currentMeal.getStrMeal());
        Glide.with(context).load(currentMeal.getStrMealThumb())
                .apply(new RequestOptions().override(500, 500)
                        .error(R.drawable.images)).into(holder.mealImg);

        holder.saveBtn.setOnClickListener(v-> onClickInterface.onSaveBtnClick(currentMeal));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.onDailyInspirationItemClicked(currentMeal.getIdMeal());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (myList != null) ? myList.size() : 0;
    }

   public void setList(ArrayList<Meal> myList) {
        this.myList = myList;
       Log.d("DailyRecyclerAdapter", "Setting list with size: " + (myList != null ? myList.size() : 0));
        notifyDataSetChanged();
    }
    public class DailyViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImg, saveBtn;
        TextView mealName;

        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImg = itemView.findViewById(R.id.mealImg);
            saveBtn = itemView.findViewById(R.id.addToFavouriteBtn);
            mealName = itemView.findViewById(R.id.mealName);
        }
    }
}
