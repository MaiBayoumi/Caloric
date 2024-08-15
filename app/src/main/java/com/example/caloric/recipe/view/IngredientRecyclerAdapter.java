package com.example.caloric.recipe.view;

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
import com.example.caloric.model.IngredientModel;


import java.util.ArrayList;

public class IngredientRecyclerAdapter extends RecyclerView.Adapter<IngredientRecyclerAdapter.IngredientViewHolder> {
    private ArrayList<IngredientModel> myList = new ArrayList<>();
    private Context context;

    public IngredientRecyclerAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IngredientViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        holder.ingredientName.setText(myList.get(position).getIngredientName());
        holder.measureTV.setText(myList.get(position).getIngredientMeasure());
        Glide.with(context).load(myList.get(position).getIngredientThumb())
                .apply(new RequestOptions().override(500, 500)
                        .error(com.google.firebase.appcheck.interop.R.drawable.googleg_standard_color_18)).into(holder.ingredientImg);


    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public void setList(ArrayList<IngredientModel> myList) {
        this.myList = myList;
        notifyDataSetChanged();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        ImageView ingredientImg;
        TextView measureTV, ingredientName;

        public IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            ingredientImg = itemView.findViewById(R.id.ingredientImg);
            measureTV = itemView.findViewById(R.id.measureTextView);
            ingredientName = itemView.findViewById(R.id.ingredientTextView);
        }
    }
}
