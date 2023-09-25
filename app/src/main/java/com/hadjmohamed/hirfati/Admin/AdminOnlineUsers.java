package com.hadjmohamed.hirfati.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;
import com.hadjmohamed.hirfati.User;

import java.util.ArrayList;
import java.util.List;

public class AdminOnlineUsers extends AppCompatActivity implements RecViewInterface {

    private RecyclerView recyclerViewUserAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_online_users);



        // recycle view
        recyclerViewUserAdmin = findViewById(R.id.onlineUsersAdmin);
        List<User> userList = new ArrayList<>();
        userList.add(new User("sdfaadfadfafd","حاج", "مختاري", null));
        userList.add(new User("sdfaadfadfafd","حاج", "مختاري", null));
        userList.add(new User("sdfaadfadfafd","حاج", "مختاري", null));
        userList.add(new User("sdfaadfadfafd","حاج", "مختاري", null));

        recyclerViewUserAdmin.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerViewUserAdmin.setAdapter(new AdapterRecUser(getApplicationContext(),
                userList, this));


    }

    @Override
    public void onItemClick(String view, int position) {
        startActivity(new Intent(AdminOnlineUsers.this, AdminUserAccount.class));
    }
}