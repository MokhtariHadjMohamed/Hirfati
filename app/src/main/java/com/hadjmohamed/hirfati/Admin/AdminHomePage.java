package com.hadjmohamed.hirfati.Admin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hadjmohamed.hirfati.LogInActivity;
import com.hadjmohamed.hirfati.R;

public class AdminHomePage extends AppCompatActivity implements View.OnClickListener {

    // Element
    private CardView craftsmenCard, userCard, craftsCard,
            craftsmen, user, crafts, report, userOnline, singOut;
    private TextView craftsmanNumber, userNumber, craftsNumber, userOnlineNumber, reportNumber;

    private FirebaseFirestore firestore;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);
        firestore = FirebaseFirestore.getInstance();

        // Progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        // Element
        craftsCard = findViewById(R.id.craftsCardAdminHomePage);
        craftsmenCard = findViewById(R.id.craftsmanCardAdminHomePage);
        userCard = findViewById(R.id.userCardAdminHomePage);

        craftsmen = findViewById(R.id.craftsmanAdminHomePage);
        crafts = findViewById(R.id.craftsAdminHomePage);
        user = findViewById(R.id.userAdminHomePage);
        userOnline = findViewById(R.id.userOnlineAdminHomePage);
        report = findViewById(R.id.reportAdminHomePage);
        singOut = findViewById(R.id.singOutAdminHomePage);
        userNumber = findViewById(R.id.userNumberAdminHomePage);
        craftsmanNumber = findViewById(R.id.craftsmanNumberAdminHomePage);
        craftsNumber = findViewById(R.id.craftsNumberAdminHomePage);
        userOnlineNumber = findViewById(R.id.userOnlineNumberAdminHomePage);
        reportNumber = findViewById(R.id.reportNumberAdminHomePage);

        getUsersNumber();

        crafts.setOnClickListener(this);
        craftsmen.setOnClickListener(this);
        user.setOnClickListener(this);

        craftsCard.setOnClickListener(this);
        craftsmenCard.setOnClickListener(this);
        userCard.setOnClickListener(this);
        userOnline.setOnClickListener(this);
        report.setOnClickListener(this);
        singOut.setOnClickListener(this);

    }

    private void getUsersNumber(){
        firestore.collection("Users")
                .whereEqualTo("userType", "User")
                .count()
                .get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Count fetched successfully
                            AggregateQuerySnapshot snapshot = task.getResult();
                            userNumber.setText(String.valueOf(snapshot.getCount()));
                            getCraftsmenNumber();
                        } else {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.d(TAG, "Count failed: ", task.getException());
                        }
                    }
                });
    }
    private void getCraftsNumber(){
        firestore.collection("Crafts")
                .count()
                .get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Count fetched successfully
                            AggregateQuerySnapshot snapshot = task.getResult();
                            craftsNumber.setText(String.valueOf(snapshot.getCount()));
                            getUserOnlineNumber();
                        } else {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.d(TAG, "Count failed: ", task.getException());
                        }
                    }
                });
    }

    private void getCraftsmenNumber(){
        firestore.collection("Users")
                .whereEqualTo("userType", "Craftsman")
                .count()
                .get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Count fetched successfully
                            AggregateQuerySnapshot snapshot = task.getResult();
                            craftsmanNumber.setText(String.valueOf(snapshot.getCount()));
                            getCraftsNumber();
                        } else {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.d(TAG, "Count failed: ", task.getException());
                        }
                    }
                });
    }
    private void getUserOnlineNumber(){
        firestore.collection("Users")
                .whereEqualTo("logInSituation", "Online")
                .count()
                .get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Count fetched successfully
                            AggregateQuerySnapshot snapshot = task.getResult();
                            userOnlineNumber.setText(String.valueOf(snapshot.getCount()));
                            getReportNumber();
                        } else {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.d(TAG, "Count failed: ", task.getException());
                        }
                    }
                });
    }
    private void getReportNumber(){
            firestore.collection("Reports")
                    .count()
                    .get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                // Count fetched successfully
                                AggregateQuerySnapshot snapshot = task.getResult();
                                reportNumber.setText(String.valueOf(snapshot.getCount()));
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                            } else {
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                                Log.d(TAG, "Count failed: ", task.getException());
                            }
                        }
                    });
        }


    @Override
    public void onClick(View view) {
        if (view == craftsmen || view == craftsmenCard)
            startActivity(new Intent(AdminHomePage.this, AdminCraftsmen.class));
        else if(view == craftsCard || view == crafts)
            startActivity(new Intent(AdminHomePage.this, AdminCrafts.class));
        else if(view == userCard || view == user)
            startActivity(new Intent(AdminHomePage.this, AdminUser.class));
        else if (view == report)
            startActivity(new Intent(AdminHomePage.this, AdminReport.class));
        else if (view == userOnline)
            startActivity(new Intent(AdminHomePage.this, AdminOnlineUsers.class));
        else if (view == singOut){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AdminHomePage.this, LogInActivity.class));
            finish();
        }
    }
}