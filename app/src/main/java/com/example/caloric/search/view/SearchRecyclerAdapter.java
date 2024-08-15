package com.example.caloric.search.view;
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

public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.SearchViewHolder> {
    private ArrayList<Meal> myList = new ArrayList<>();
    private Context context;
    private OnSearchClickInterface onSearchClickInterface;
    public SearchRecyclerAdapter(Context context, OnSearchClickInterface onSearchClickInterface){
        this.context = context;
        this.onSearchClickInterface = onSearchClickInterface;
        this.myList=myList;
    }
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.meal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Meal mealList= myList.get(position);

        Glide.with(context).load(mealList.getStrMealThumb())
                .apply(new RequestOptions().override(500,500)
                        .error(R.drawable.meals)).into(holder.mealImg);
        holder.mealName.setText(mealList.getStrMeal());

        holder.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchClickInterface.onSaveBtnClicked(myList.get(holder.getAdapterPosition()));
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchClickInterface.onItemClicked(myList.get(holder.getAdapterPosition()).getIdMeal());
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

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        ImageView mealImg, saveBtn;
        TextView mealName;
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImg = itemView.findViewById(R.id.mealImg);
            mealName = itemView.findViewById(R.id.mealName);
            saveBtn = itemView.findViewById(R.id.addToFavouriteBtn);
        }
    }
}
