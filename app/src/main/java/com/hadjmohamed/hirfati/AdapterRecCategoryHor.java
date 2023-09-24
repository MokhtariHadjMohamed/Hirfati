package com.hadjmohamed.hirfati;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterRecCategoryHor extends RecyclerView.Adapter<HolderRecCategory> {

    private final RecViewInterface recViewInterface;
    Context context;
    List<Category> categoryList;

    public AdapterRecCategoryHor(Context context, List<Category> categoryList, RecViewInterface recViewInterface) {
        this.context = context;
        this.categoryList = categoryList;
        this.recViewInterface = recViewInterface;
    }

    @NonNull
    @Override
    public HolderRecCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HolderRecCategory(LayoutInflater.from(context).
                inflate(R.layout.category_item_rec_hor, parent, false), recViewInterface);
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
