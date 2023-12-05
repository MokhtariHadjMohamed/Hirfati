package com.hadjmohamed.hirfati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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

public class UserAccountActivity extends AppCompatActivity implements View.OnClickListener, RecViewInterface {

    // Element
    private TextView nameAdmin, familyNameAdmin, birthdayAdmin, addressAdmin, commentNoun;
    private TextView accountInfoUserAccount, settingUserAccount, logOutUserAccount;

    private ImageView userInfoImage;
    private String idUser;
    // AdapterRecComment
    private List<Comment> commentList;
    private AdapterRecComment adapterRecComment;
    private RecyclerView recyclerView;

    // toolbar
    private Toolbar toolbar;
    private ImageView imageViewToolBar;
    private SearchView search;
    //firebase
    private FirebaseFirestore firestore;

    // progressDialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        firestore = FirebaseFirestore.getInstance();
        idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        // Toolbar
        toolbar = findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);
        search = findViewById(R.id.search);
        imageViewToolBar = findViewById(R.id.imageToolBar);
        retrieveImage(imageViewToolBar, idUser);

        //Element
        nameAdmin = findViewById(R.id.nameUserAccount);
        familyNameAdmin = findViewById(R.id.familyNameUserAccount);
        birthdayAdmin = findViewById(R.id.birthdayUserAccount);
        addressAdmin = findViewById(R.id.addressUserAccount);
        userInfoImage = findViewById(R.id.userInfoImage);
        commentNoun = findViewById(R.id.commentNounUserAccount);

        accountInfoUserAccount = findViewById(R.id.accountInfoUserAccount);
        settingUserAccount = findViewById(R.id.settingUserAccount);
        logOutUserAccount = findViewById(R.id.logOutUserAccount);

        accountInfoUserAccount.setOnClickListener(this);
        settingUserAccount.setOnClickListener(this);
        logOutUserAccount.setOnClickListener(this);

        // navigation bar Bottom
        BottomNavigation();

        // comment RecyclerView
        recyclerView = findViewById(R.id.commentUserAccount);
        commentList = new ArrayList<>();
        adapterRecComment = new AdapterRecComment(getApplicationContext(), commentList, this);

        getComment();
        getUser();
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

    private void getUser(){
        firestore.collection("Users")
                .document(idUser)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.d("get User: ", "failed");
                            return;
                        }
                        User user = task.getResult().toObject(User.class);
                        nameAdmin.setText(user.getName());
                        familyNameAdmin.setText(user.getFamilyName());
                        birthdayAdmin.setText(user.getBirthday());
                        addressAdmin.setText(user.getAddress());
                        retrieveImage(userInfoImage, user.getIdUser());
                    }
                });
    }

    private void getComment() {
        Log.d("get idUser: ", idUser);
        firestore.collection("Comments")
                .whereEqualTo("uidUser", idUser)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.d("get Comment: ", "failed");
                            return;
                        }
                        Log.d("Size comment: ", task.getResult().size() + "");
                        for (QueryDocumentSnapshot c : task.getResult()) {
                            commentList.add(c.toObject(Comment.class));
                        }
                        if (commentList.isEmpty())
                            commentNoun.setVisibility(View.VISIBLE);
                        adapterRecComment.notifyDataSetChanged();
                    }
                });
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapterRecComment);
    }

    private void BottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar_home);
        bottomNavigationView.setSelectedItemId(R.id.accountNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homeNavigation) {
                startActivity(new Intent(UserAccountActivity.this, HomePageActivity.class));
                return true;
            } else if (id == R.id.searchNavigation) {
                startActivity(new Intent(UserAccountActivity.this, SearchPageActivity.class));
                return true;
            } else if (id == R.id.accountNavigation) {
                return true;
            } else if (id == R.id.categoryNavigation) {
                startActivity(new Intent(UserAccountActivity.this, CraftsPageActivity.class));
                return true;
            } else {
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == accountInfoUserAccount){
            Intent intent = new Intent(UserAccountActivity.this, UserAccountInfoActivity.class);
            intent.putExtra("idUser", idUser);
            startActivity(intent);
        }else if (view == settingUserAccount){
            startActivity(new Intent(UserAccountActivity.this, SettingsPageActivity.class));
        }else if (view == logOutUserAccount){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(UserAccountActivity.this, LogInActivity.class));
            finish();
        }
    }

    @Override
    public void onItemClick(String view, int position) {

    }
}