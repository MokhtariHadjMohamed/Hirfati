package com.hadjmohamed.hirfati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirestoreRegistrar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CraftsPageActivity extends AppCompatActivity implements RecViewInterface{

    // RecyclerView
    private RecyclerView recyclerViewCategory;
    private AdapterRecCategoryHor adapterRecCategoryHor;
    private List<Crafts> craftsList;
    //    ProgressDialog
    private ProgressDialog progressDialog;
    // Firebase
    private FirebaseFirestore firestore;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);
        firestore = FirebaseFirestore.getInstance();
        getUsersUid(FirebaseAuth.getInstance().getUid());

        // Progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        // navigation bar Bottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar_home);
        bottomNavigationView.setSelectedItemId(R.id.categoryNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homeNavigation) {
                startActivity(new Intent(CraftsPageActivity.this, HomePageActivity.class));
                return true;
            } else if (id == R.id.searchNavigation) {
                startActivity(new Intent(CraftsPageActivity.this, SearchPageActivity.class));
                return true;
            } else if (id == R.id.accountNavigation) {
                if (userType.equals("Craftsman"))
                    startActivity(new Intent(CraftsPageActivity.this, CraftsmanAccountActivity.class));
                else if(userType.equals("User"))
                    startActivity(new Intent(CraftsPageActivity.this, UserAccountActivity.class));
                return true;
            } else if (id == R.id.categoryNavigation) {
                return true;
            } else {
                return false;
            }
        });

        //  RecyclerView Crafts
            recyclerViewCategory = findViewById(R.id.craftsCategoryActivity);
            craftsList = new ArrayList<>();
            adapterRecCategoryHor = new AdapterRecCategoryHor(CraftsPageActivity.this,
                    craftsList, this);
            getCrafts();
    }

    private void getCrafts(){
        firestore.collection("Crafts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
                        adapterRecCategoryHor.notifyDataSetChanged();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
        recyclerViewCategory.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewCategory.setAdapter(adapterRecCategoryHor);
    }

    private void getUsersUid(String uid){
        firestore.collection("Users")
                .document(uid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.e("GetUsers", "failed");
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            return;
                        }
                        User user = task.getResult().toObject(User.class);
                        userType = user.getUserType();
                    }
                });
    }

    @Override
    public void onItemClick(String view, int position) {

    }
}