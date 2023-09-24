package com.hadjmohamed.hirfati.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hadjmohamed.hirfati.Category;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;
import com.hadjmohamed.hirfati.User;

import java.util.List;

public class AdapterRecUser extends RecyclerView.Adapter<HolderRecUser>{

    private final RecViewInterface recViewInterface;
    Context context;
    List<User> userList;

    public AdapterRecUser(Context context, List<User> userList, RecViewInterface recViewInterface) {
        this.context = context;
        this.userList = userList;
        this.recViewInterface = recViewInterface;
    }

    @NonNull
    @Override
    public HolderRecUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HolderRecUser(LayoutInflater.from(context).
                inflate(R.layout.user_item, parent, false), recViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecUser holder, int position) {
        holder.userUid.setText(userList.get(position).getUid());
        holder.imageView.setImageURI(userList.get(position).getImage());
        holder.userName.setText(userList.get(position).getName() + userList.get(position).getFamilyName());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
