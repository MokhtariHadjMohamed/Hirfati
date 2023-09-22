package com.hadjmohamed.hirfati;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterRecComment extends RecyclerView.Adapter<HolderRecComment> {

    private Context context;
    private List<Comment> commentList;

    public AdapterRecComment(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public HolderRecComment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HolderRecComment(LayoutInflater.from(context)
                .inflate(R.layout.comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecComment holder, int position) {
        if(commentList.get(position) == null){
            Log.e("ERROR", "______________________________");
            return;
        }
        holder.nameAndFamilyName.setText(commentList.get(position).getName());
        holder.description.setText(commentList.get(position).getText());
        holder.imageView.setImageURI(commentList.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
