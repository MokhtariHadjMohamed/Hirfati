package com.hadjmohamed.hirfati.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hadjmohamed.hirfati.Craftsman;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;
import com.hadjmohamed.hirfati.User;

import java.util.ArrayList;
import java.util.List;

public class AdminOnlineUsers extends AppCompatActivity implements RecViewInterface {

//    RecyclerView
    private RecyclerView recyclerViewUserAdmin;
    private AdapterRecUser adapterRecUser;
    private List<User> userList;
    // ProgressDialog
    private ProgressDialog progressDialog;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_online_users);

        firestore = FirebaseFirestore.getInstance();

        // Progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();


        // recycle view
        recyclerViewUserAdmin = findViewById(R.id.onlineUsersAdmin);
        userList = new ArrayList<>();
        adapterRecUser = new AdapterRecUser(getApplicationContext(),
                userList, this);
        getUsers();


    }

    private void getUsers(){
        firestore.collection("Users")
                .whereEqualTo("userType", "User")
                .whereEqualTo("logInSituation", "Online")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.e("GetUsers", "failed");
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            return;
                        }
                        for(QueryDocumentSnapshot d: task.getResult()){
                            userList.add(d.toObject(User.class));
                        }
                        adapterRecUser.notifyDataSetChanged();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
        recyclerViewUserAdmin.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerViewUserAdmin.setAdapter(adapterRecUser);
    }

    @Override
    public void onItemClick(String view, int position) {
        Intent intent = new Intent(AdminOnlineUsers.this, AdminUserAccount.class);
        intent.putExtra("idUser",userList.get(position).getIdUser());
        startActivity(intent);
    }
}