package com.hadjmohamed.hirfati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hadjmohamed.hirfati.Admin.AdminHomePage;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(MainActivity.this, LogInActivity.class));
            finish();
        }
        else {
            getUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
    }

    private void getUser(String uid){
        firestore.collection("Users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.d("getUser:", "Failed");
                            return;
                        }
                        user = task.getResult().toObject(User.class);
                        if (Objects.equals(user.getUserType(), "Admin")){
                            startActivity(new Intent(MainActivity.this, AdminHomePage.class));
                            finish();
                        }
                        else if (Objects.equals(user.getUserType(), "User")
                                || Objects.equals(user.getUserType(), "Craftsman")){
                            startActivity(new Intent(MainActivity.this, HomePageActivity.class));
                            finish();
                        }
                    }
                });
    }


}