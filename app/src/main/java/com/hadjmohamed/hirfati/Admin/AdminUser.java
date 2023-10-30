package com.hadjmohamed.hirfati.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hadjmohamed.hirfati.AdapterRecCraftsmen;
import com.hadjmohamed.hirfati.Craftsman;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;
import com.hadjmohamed.hirfati.User;

import java.util.ArrayList;
import java.util.List;

public class AdminUser extends AppCompatActivity implements RecViewInterface, View.OnClickListener {

    private Button userAdd;
    // RecyclerView
    private RecyclerView recyclerViewUserAdmin;
    private List<User> userList;
    private AdapterRecUser adapterRecUser;
    // toolbar
    private Toolbar toolbar;
    private ImageView backArrow, imageViewToolBar;
    private TextView toolbarTitle;
    // Firestor
    private FirebaseFirestore firestore;
    // progressDialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);
        firestore = FirebaseFirestore.getInstance();

        // toolbar
        toolbar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolbar);
        backArrow = findViewById(R.id.backArrow);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        imageViewToolBar = findViewById(R.id.imageViewToolBar);

        toolbarTitle.setText("المستعملين");
        backArrow.setOnClickListener(this);

        // Progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();


        // Element
        userAdd = findViewById(R.id.userAddAdminCrafts);
        userAdd.setOnClickListener(this);

        // recycle view
        recyclerViewUserAdmin = findViewById(R.id.usersAdmin);
        userList = new ArrayList<>();
        adapterRecUser = new AdapterRecUser(getApplicationContext(),
                userList, this);
        getUsers();
    }

    private void getUsers(){
        firestore.collection("Users")
                .whereEqualTo("userType", "User")
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
                            userList.add(d.toObject(Craftsman.class));
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
        if (view == "user"){
            Intent intent = new Intent(AdminUser.this, AdminUserAccount.class);
            intent.putExtra("idUser", userList.get(position).getIdUser());
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == backArrow){
            startActivity(new Intent(AdminUser.this, AdminHomePage.class));
            finish();
        }else if(view == userAdd){
            startActivity(new Intent(AdminUser.this, AdminUserAccountAdd.class));
        }
    }
}