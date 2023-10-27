package com.hadjmohamed.hirfati;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class AdapterRecCraftsmen extends RecyclerView.Adapter<HolderRecCraftsmen> {

    private RecViewInterface recViewInterface;
    Context context;
    List<Craftsman> commentList;

    public AdapterRecCraftsmen(Context context, List<Craftsman> categoryList,RecViewInterface recViewInterface) {
        this.context = context;
        this.commentList = categoryList;
        this.recViewInterface = recViewInterface;
    }

    @NonNull
    @Override
    public HolderRecCraftsmen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HolderRecCraftsmen(LayoutInflater.from(context).
                inflate(R.layout.craftsman_item_rec, parent, false), recViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecCraftsmen holder, int position) {
        if (commentList.get(position) == null){
            Log.e("ERROR", "______________________________");
            return;
        }
        holder.nameAndFamilyName.setText(commentList.get(position).getName() + " "
                + commentList.get(position).getFamilyName());
        holder.crafts.setText(commentList.get(position).getCraft());
        holder.description.setText(commentList.get(position).getDescription());
        holder.imageView.setImageResource(R.drawable.baseline_image_not_supported_24);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
