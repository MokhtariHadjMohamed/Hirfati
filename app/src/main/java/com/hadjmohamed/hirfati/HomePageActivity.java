package com.hadjmohamed.hirfati;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomePageActivity extends AppCompatActivity implements RecViewInterface {

    private RecyclerView recyclerViewCategory, recyclerViewCraftsmen;
    private AdapterRecCraftsmen adapterRecCraftsmen;
    private List<Craftsman> craftsmanList;
    // Firestor
    private FirebaseFirestore firestore;
    // progressDialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firestore = FirebaseFirestore.getInstance();

        // Progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        // navigation bar Bottom
        navigationBottom();

        // RecyclerView Category
        recyclerViewCategory = findViewById(R.id.categoryHome);
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("سباك", Uri.parse("android.resource:" + R.drawable.logo)));
        categoryList.add(new Category("نجار", Uri.parse("android.resource:" + R.drawable.logo)));
        categoryList.add(new Category("كهربائي", Uri.parse("android.resource:" + R.drawable.logo)));
        categoryList.add(new Category("طيار", Uri.parse("android.resource:" + R.drawable.logo)));

        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategory.setAdapter(new AdapterRecCategoryHor(getApplicationContext(),
                categoryList, this));

        // recyclerView Craftsmen
        recyclerViewCraftsmen = findViewById(R.id.craftsmenHome);
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
        if (Objects.equals(view, "Category"))
            startActivity(new Intent(HomePageActivity.this, CategoryPageActivity.class));
        else if (Objects.equals(view, "Craftsmen"))
            startActivity(new Intent(HomePageActivity.this, CraftsmanInfoActivity.class));
    }

    private void navigationBottom(){
        // navigation bar Bottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar_home);
        bottomNavigationView.setSelectedItemId(R.id.homeNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homeNavigation) {
                return true;
            } else if (id == R.id.searchNavigation) {
                startActivity(new Intent(HomePageActivity.this, SearchPageActivity.class));
                return true;
            } else if (id == R.id.accountNavigation) {
                startActivity(new Intent(HomePageActivity.this, UserAccountActivity.class));
                return true;
            } else if (id == R.id.categoryNavigation) {
                startActivity(new Intent(HomePageActivity.this, CategoryPageActivity.class));
                return true;
            } else {
                return false;
            }
        });
    }
}