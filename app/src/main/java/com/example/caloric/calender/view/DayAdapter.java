package com.example.caloric.calender.view;

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
import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {
    private List<Meal> myList ;
    private Context context;
    private OnDayClickInterface onDayClickInterface;


    public DayAdapter(Context context, OnDayClickInterface onDayClickInterface,ArrayList<Meal> myList){
        this.context = context;
        this.onDayClickInterface = onDayClickInterface;
        this.myList = myList;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new DayViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        holder.deleteBtn.setImageResource(R.drawable.delete_icon);
        holder.mealName.setText(myList.get(position).getStrMeal());
        Glide.with(context).load(myList.get(position).getStrMealThumb())
                .apply(new RequestOptions().override(500,500)
                        .error(R.drawable.meals)).into(holder.mealImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDayClickInterface.onFavItemClicked(myList.get(holder.getAdapterPosition()).getIdMeal());
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDayClickInterface.onDeleteBtnClicked(myList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public void setList(ArrayList<Meal> myList) {
        this.myList = myList;
        notifyDataSetChanged();
    }

    public class DayViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImg, deleteBtn;
        TextView mealName;
        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImg = itemView.findViewById(R.id.mealImg);
            mealName = itemView.findViewById(R.id.mealName);
            deleteBtn = itemView.findViewById(R.id.addToFavouriteBtn);
        }
    }
}
