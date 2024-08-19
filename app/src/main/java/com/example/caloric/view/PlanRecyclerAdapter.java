package com.example.caloric.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yumfit.R;

import java.util.ArrayList;

public class PlanRecyclerAdapter extends RecyclerView.Adapter<PlanRecyclerAdapter.PlanViewHolder> {
    private ArrayList<String> myList = new ArrayList<>();
    private OnPlanClickInterface onPlanClickInterface;
    public PlanRecyclerAdapter(OnPlanClickInterface onPlanClickInterface){
        this.onPlanClickInterface = onPlanClickInterface;
    }
    @NonNull
    @Override
    public PlanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlanViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.plan_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlanViewHolder holder, int position) {

        holder.dayTextView.setText(myList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlanClickInterface.onShowBtnClicked(myList.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return myList.size();
    }

    public void setList() {
        myList.add("saturday");
        myList.add("sunday");
        myList.add("monday");
        myList.add("tuesday");
        myList.add("wednesday");
        myList.add("thursday");
        myList.add("friday");
        notifyDataSetChanged();
    }

    public class PlanViewHolder extends RecyclerView.ViewHolder {

        TextView dayTextView;
        public PlanViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
        }
    }
}
