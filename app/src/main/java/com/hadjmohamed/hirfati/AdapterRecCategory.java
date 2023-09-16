package com.hadjmohamed.hirfati;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterRecCategory extends RecyclerView.Adapter<HolderRecCategory> {

    Context context;
    List<Category> categoryList;

    public AdapterRecCategory(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public HolderRecCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HolderRecCategory(LayoutInflater.from(context).
                inflate(R.layout.category_item_rec, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecCategory holder, int position) {
        holder.categoryName.setText(categoryList.get(position).getCategoryName());
        holder.imageView.setImageURI(categoryList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}
