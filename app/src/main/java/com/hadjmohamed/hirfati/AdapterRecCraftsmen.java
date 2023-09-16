package com.hadjmohamed.hirfati;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class AdapterRecCraftsmen extends RecyclerView.Adapter<HolderRecCraftsmen> {

    Context context;
    List<Craftsman> craftsmenList;

    public AdapterRecCraftsmen(Context context, List<Craftsman> categoryList) {
        this.context = context;
        this.craftsmenList = categoryList;
    }

    @NonNull
    @Override
    public HolderRecCraftsmen onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HolderRecCraftsmen(LayoutInflater.from(context).
                inflate(R.layout.craftsman_item_rec, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecCraftsmen holder, int position) {
        if (craftsmenList.get(position) == null){
            Log.e("ERROR", "______________________________");
            return;
        }
        Log.d("Name", craftsmenList.get(position).getName());
        holder.nameAndFamilyName.setText(craftsmenList.get(position).getName() + " "
                + craftsmenList.get(position).getFamilyName());
        holder.crafts.setText(craftsmenList.get(position).getCrafts());
        holder.description.setText(craftsmenList.get(position).getDescription());
        holder.imageView.setImageURI(craftsmenList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return craftsmenList.size();
    }
}
