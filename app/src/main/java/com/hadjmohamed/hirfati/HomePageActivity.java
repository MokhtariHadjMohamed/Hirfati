package com.hadjmohamed.hirfati;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
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
import java.util.Objects;

public class HomePageActivity extends AppCompatActivity implements RecViewInterface, View.OnClickListener {

    private TextView more;

    // RecyclerView
    private RecyclerView recyclerViewCategory, recyclerViewCraftsmen;
    private AdapterRecCraftsmen adapterRecCraftsmen;
    private AdapterRecCategoryHor adapterRecCategoryHor;
    private List<Crafts> craftsList;
    private List<Craftsman> craftsmanList;
    // toolbar
    private Toolbar toolbar;
    private ImageView imageViewToolBar;
    private SearchView search;
    // Firestor
    private FirebaseFirestore firestore;
    // progressDialog
    private ProgressDialog progressDialog;
    private String idUser, userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        firestore = FirebaseFirestore.getInstance();
        idUser = FirebaseAuth.getInstance().getUid();
        getUsers(idUser);
        // Progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        // Element
        more = findViewById(R.id.more);
        more.setOnClickListener(this);

        // Toolbar
        toolbar = findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);
        search = findViewById(R.id.search);
        imageViewToolBar = findViewById(R.id.imageToolBar);
        retrieveImage(imageViewToolBar, idUser);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(HomePageActivity.this, SearchPageActivity.class);
                intent.putExtra("search", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        // navigation bar Bottom
        navigationBottom();

        // RecyclerView Crafts
        recyclerViewCategory = findViewById(R.id.categoryHome);
        craftsList = new ArrayList<>();
        adapterRecCategoryHor = new AdapterRecCategoryHor(HomePageActivity.this,
                craftsList, this);

        // recyclerView Craftsmen
        recyclerViewCraftsmen = findViewById(R.id.craftsmenHome);
        craftsmanList = new ArrayList<>();
        adapterRecCraftsmen = new AdapterRecCraftsmen(HomePageActivity.this,
                craftsmanList, this);
        getUsers();
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
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategory.setAdapter(adapterRecCategoryHor);
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
                    }
                });
        recyclerViewCraftsmen.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerViewCraftsmen.setAdapter(adapterRecCraftsmen);
        getCrafts();
    }

    private void getUsers(String uid){
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
                if (userType.equals("Craftsman"))
                    startActivity(new Intent(HomePageActivity.this, CraftsmanAccountActivity.class));
                else if(userType.equals("User"))
                    startActivity(new Intent(HomePageActivity.this, UserAccountActivity.class));
                return true;
            } else if (id == R.id.categoryNavigation) {
                startActivity(new Intent(HomePageActivity.this, CraftsPageActivity.class));
                return true;
            } else {
                return false;
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
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    imageView.setImageResource(R.drawable.baseline_image_not_supported_24);
                    Log.e("Image " + image, "Failed");
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
            });
        } catch (IOException e) {
            imageView.setImageResource(R.drawable.baseline_image_not_supported_24);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            throw new RuntimeException(e);
        }

    }
    @Override
    public void onItemClick(String view, int position) {
        if (Objects.equals(view, "Crafts")){
            Intent intent = new Intent(HomePageActivity.this, SearchPageActivity.class);
            intent.putExtra("IdCrafts", craftsList.get(position).getUid());
            startActivity(intent);
        }
        else if (Objects.equals(view, "Craftsmen")){
            Intent intent = new Intent(HomePageActivity.this, CraftsmanInfoActivity.class);
            intent.putExtra("idUser", craftsmanList.get(position).getIdUser());
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == more){
            startActivity(new Intent(HomePageActivity.this, CraftsPageActivity.class));
        }
    }
}