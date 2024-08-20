package com.example.caloric.meals.view;

import android.content.Context;
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

public class MealsAdapter extends RecyclerView.Adapter<MealsAdapter.MealViewHolder> {
    private ArrayList<Meal> myList = new ArrayList<>();
    private Context context;
    private OnCommonClickInterface onCommonClickInterface;


    public MealsAdapter(Context context, OnCommonClickInterface onCommonClickInterface){
        this.context = context;
        this.onCommonClickInterface = onCommonClickInterface;
    }
    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MealViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {

        holder.mealName.setText(myList.get(position).getStrMeal());
        Glide.with(context).load(myList.get(position).getStrMealThumb())
                .apply(new RequestOptions().override(500,500)
                        .error(R.drawable.meals)).into(holder.mealImg);
        holder.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCommonClickInterface.onSaveBtnClicked(myList.get(holder.getAdapterPosition()));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCommonClickInterface.onMealItemClicked(myList.get(holder.getAdapterPosition()).getIdMeal());
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

    public class MealViewHolder extends RecyclerView.ViewHolder {

        ImageView mealImg, saveBtn;
        TextView mealName;


        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImg = itemView.findViewById(R.id.mealImg);
            mealName = itemView.findViewById(R.id.mealName);
            saveBtn = itemView.findViewById(R.id.addToFavouriteBtn);
        }
    }
}
