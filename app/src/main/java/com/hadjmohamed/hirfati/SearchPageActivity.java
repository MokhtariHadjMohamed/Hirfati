package com.hadjmohamed.hirfati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchPageActivity extends AppCompatActivity implements RecViewInterface {

    // Element
    private TextView noneSearch;

    // RecyclerView
    private RecyclerView recyclerView;
    private AdapterRecCraftsmen adapterRecCraftsmen;
    private List<Craftsman> craftsmanList;
    // Firebase
    private FirebaseFirestore firestore;

    // Toolbar
    private TextView textViewToolsBar;
    private ImageView imageViewToolBar;
    private SearchView searchView;

    // ProgressDialog
    private ProgressDialog progressDialog;
    private String search, userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        firestore = FirebaseFirestore.getInstance();
        getUsersUid(FirebaseAuth.getInstance().getUid());
        // Progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        // Element
        noneSearch = findViewById(R.id.noneSearchActivity);
        if (craftsmanList == null)
            noneSearch.setText("إبحث عن حرفي");

        // Toolbar
        Toolbar toolBar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolBar);
        textViewToolsBar = findViewById(R.id.toolbarTitle);
        imageViewToolBar = findViewById(R.id.imageToolBar);
        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressDialog.show();
                craftsmanList.clear();
                getUsers(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        retrieveImage(imageViewToolBar, FirebaseAuth.getInstance().getUid());

        // RecyclerView
        recyclerView = findViewById(R.id.craftsmenSearch);
        craftsmanList = new ArrayList<>();
        adapterRecCraftsmen = new AdapterRecCraftsmen(SearchPageActivity.this,
                craftsmanList, this);
        if (getIntent().getStringExtra("search") != null){
            search = getIntent().getStringExtra("search");
            searchView.setQuery(search, false);
            getUsers(search);
        }
        else if (getIntent().getStringExtra("IdCrafts") != null) {
            search = getIntent().getStringExtra("IdCrafts");
            getCrafts(search);
        }else {
            if (progressDialog.isShowing())
                progressDialog.dismiss();
        }

        // navigation bar Bottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar_home);
        bottomNavigationView.setSelectedItemId(R.id.searchNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homeNavigation) {
                startActivity(new Intent(SearchPageActivity.this, HomePageActivity.class));
                return true;
            } else if (id == R.id.searchNavigation) {
                return true;
            } else if (id == R.id.accountNavigation) {
                if (userType.equals("Craftsman"))
                    startActivity(new Intent(SearchPageActivity.this, CraftsmanAccountActivity.class));
                else if(userType.equals("User"))
                    startActivity(new Intent(SearchPageActivity.this, UserAccountActivity.class));
                return true;
            } else if (id == R.id.categoryNavigation) {
                startActivity(new Intent(SearchPageActivity.this, CraftsPageActivity.class));
                return true;
            } else {
                return false;
            }
        });
    }

    private void getCrafts(String uid) {
        firestore.collection("Crafts")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("GetCrafts", "failed");
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            return;
                        }
                        Crafts crafts = task.getResult().toObject(Crafts.class);
                        getUsers(crafts.getName());
                    }
                });
    }

    private void getUsers(String search) {
        firestore.collection("Users")
                .whereEqualTo("userType", "Craftsman")
                .where(Filter.or(
                        Filter.equalTo("craft", search),
                        Filter.equalTo("familyName", search),
                        Filter.equalTo("name", search)
                ))
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("GetUsers", "failed");
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            return;
                        }
                        for (QueryDocumentSnapshot d : task.getResult()) {
                            craftsmanList.add(d.toObject(Craftsman.class));
                        }
                        if (craftsmanList.isEmpty())
                            noneSearch.setText("لايوجد حرفي");
                        else
                            noneSearch.setText(null);
                        adapterRecCraftsmen.notifyDataSetChanged();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapterRecCraftsmen);
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

    private void retrieveImage(ImageView imageView, String image) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference().child("Image")
                .child(image);

        final File file;
        try {
            file = File.createTempFile("img", "png");

            storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    imageView.setImageResource(R.drawable.baseline_image_not_supported_24);
                    Log.e("Image " + image, "Failed");
                }
            });
        } catch (IOException e) {
            imageView.setImageResource(R.drawable.baseline_image_not_supported_24);
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onItemClick(String view, int position) {
        Intent intent = new Intent(SearchPageActivity.this, CraftsmanInfoActivity.class);
        intent.putExtra("idUser", craftsmanList.get(position).getIdUser());
        startActivity(intent);
    }
}