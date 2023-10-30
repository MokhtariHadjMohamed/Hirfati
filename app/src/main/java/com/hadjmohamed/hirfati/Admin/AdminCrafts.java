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
import com.hadjmohamed.hirfati.AdapterRecCategoryVer;
import com.hadjmohamed.hirfati.Crafts;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;

import java.util.ArrayList;
import java.util.List;

public class AdminCrafts extends AppCompatActivity implements RecViewInterface, View.OnClickListener {

    private Button addCrafts;
    // toolbar
    private Toolbar toolbar;
    private ImageView backArrow, imageViewToolBar;
    private TextView toolbarTitle;
    // RecyclerView
    private RecyclerView recyclerViewCategory;
    private AdapterRecCategoryVer adapterRecCategoryVer;
    private List<Crafts> craftsList;

    // Firestor
    private FirebaseFirestore firestore;
    // progressDialog
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_crafts);
        firestore = FirebaseFirestore.getInstance();

        // Element
        addCrafts = findViewById(R.id.craftsAddAdminCrafts);
        addCrafts.setOnClickListener(this);

        // toolbar
        toolbar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolbar);
        backArrow = findViewById(R.id.backArrow);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        imageViewToolBar = findViewById(R.id.imageViewToolBar);

        toolbarTitle.setText("الحرف");
        backArrow.setOnClickListener(this);
        // Progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        // RecyclerView Crafts
        recyclerViewCategory = findViewById(R.id.craftsAdmin);
        craftsList = new ArrayList<>();
        adapterRecCategoryVer = new AdapterRecCategoryVer(getApplicationContext(),
                craftsList, this);
        getCrafts();
    }

    private void getCrafts(){
        firestore.collection("Crafts")
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
                            craftsList.add(d.toObject(Crafts.class));
                        }
                        adapterRecCategoryVer.notifyDataSetChanged();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerViewCategory.setAdapter(adapterRecCategoryVer);
    }

    @Override
    public void onItemClick(String view, int position) {
        Intent intent = new Intent(AdminCrafts.this, AdminCraftsInfo.class);
        intent.putExtra("idCraft", craftsList.get(position).getUid());
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        if (view == addCrafts){
            startActivity(new Intent(AdminCrafts.this, AdminCraftsAdd.class));
        } else if (view == backArrow) {
            startActivity(new Intent(AdminCrafts.this, AdminHomePage.class));
            finish();
        }
    }
}