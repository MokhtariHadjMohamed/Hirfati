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
import com.hadjmohamed.hirfati.Adapter.AdapterRecCraftsmen;
import com.hadjmohamed.hirfati.model.Craftsman;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminCraftsmen extends AppCompatActivity implements RecViewInterface, View.OnClickListener {

    // toolbar
    private Toolbar toolbar;
    private ImageView backArrow, imageViewToolBar;
    private TextView toolbarTitle;
    // RecyclerView
    private RecyclerView recyclerViewCraftsmen;
    private Button addCraftsmanAdminCraftsman;
    private AdapterRecCraftsmen adapterRecCraftsmen;
    private List<Craftsman> craftsmanList;

    // Firestor
    private FirebaseFirestore firestore;
    // progressDialog
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_craftsmen);
        firestore = FirebaseFirestore.getInstance();

        // toolbar
        toolbar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolbar);
        backArrow = findViewById(R.id.backArrow);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        imageViewToolBar = findViewById(R.id.imageViewToolBar);

        toolbarTitle.setText("الحرفين");
        backArrow.setOnClickListener(this);
        // Progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        // addCraftsman
        addCraftsmanAdminCraftsman = findViewById(R.id.addCraftsmanAdminCraftsman);
        addCraftsmanAdminCraftsman.setOnClickListener(this);

        // recyclerView Craftsmen
        recyclerViewCraftsmen = findViewById(R.id.craftsmenAdmin);
        craftsmanList = new ArrayList<>();
        adapterRecCraftsmen = new AdapterRecCraftsmen(getApplicationContext(),
                craftsmanList, this);
        getUsers();

    }

    private void getUsers(){
        firestore.collection("Users")
                .whereEqualTo("userType", "Craftsman")
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
                            craftsmanList.add(d.toObject(Craftsman.class));
                        }
                        adapterRecCraftsmen.notifyDataSetChanged();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
        recyclerViewCraftsmen.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerViewCraftsmen.setAdapter(adapterRecCraftsmen);
    }

    @Override
    public void onItemClick(String view, int position) {
        if (Objects.equals(view, "Craftsmen")){
            Intent intent = new Intent(AdminCraftsmen.this, AdminCraftsmenAccount.class);
            intent.putExtra("idUser", craftsmanList.get(position).getIdUser());
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == backArrow) {
            startActivity(new Intent(AdminCraftsmen.this, AdminHomePage.class));
            finish();
        } else if (view == addCraftsmanAdminCraftsman) {
            startActivity(new Intent(AdminCraftsmen.this, AdminCraftsmenAccountAdd.class));
        }
    }
}