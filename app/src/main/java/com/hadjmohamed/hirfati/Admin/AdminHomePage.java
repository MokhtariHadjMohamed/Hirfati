package com.hadjmohamed.hirfati.Admin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.SearchManager;
import android.app.SearchManager.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.User;

public class AdminHomePage extends AppCompatActivity implements View.OnClickListener {

    // Element
    private CardView craftsmenCard, userCard, craftsCard,
            craftsmen, user, crafts, report, userOnline;
    private TextView craftsmanNumber, userNumber, craftsNumber, userOnlineNumber, reportNumber;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);
        firestore = FirebaseFirestore.getInstance();

        // Element
        craftsCard = findViewById(R.id.craftsCardAdminHomePage);
        craftsmenCard = findViewById(R.id.craftsmanCardAdminHomePage);
        userCard = findViewById(R.id.userCardAdminHomePage);

        craftsmen = findViewById(R.id.craftsmanAdminHomePage);
        crafts = findViewById(R.id.craftsAdminHomePage);
        user = findViewById(R.id.userAdminHomePage);
        userOnline = findViewById(R.id.userOnlineAdminHomePage);
        report = findViewById(R.id.reportAdminHomePage);

        userNumber = findViewById(R.id.userNumberAdminHomePage);
        craftsmanNumber = findViewById(R.id.craftsmanNumberAdminHomePage);
        craftsNumber = findViewById(R.id.craftsNumberAdminHomePage);
        userOnlineNumber = findViewById(R.id.userOnlineNumberAdminHomePage);
        reportNumber = findViewById(R.id.reportNumberAdminHomePage);

        getUsersNumber();
        getCraftsmenNumber();
        getCraftsNumber();
        getUserOnlineNumber();
        getReportNumber();

        crafts.setOnClickListener(this);
        craftsmen.setOnClickListener(this);
        user.setOnClickListener(this);

        craftsCard.setOnClickListener(this);
        craftsmenCard.setOnClickListener(this);
        userCard.setOnClickListener(this);
        userOnline.setOnClickListener(this);
        report.setOnClickListener(this);

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
                        } else {
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
                        } else {
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
                        } else {
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
                        } else {
                            Log.d(TAG, "Count failed: ", task.getException());
                        }
                    }
                });
    }
    private void getReportNumber(){
            firestore.collection("Reports")
                    .whereEqualTo("logInSituation", "Online")
                    .count()
                    .get(AggregateSource.SERVER).addOnCompleteListener(new OnCompleteListener<AggregateQuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<AggregateQuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                // Count fetched successfully
                                AggregateQuerySnapshot snapshot = task.getResult();
                                reportNumber.setText(String.valueOf(snapshot.getCount()));
                            } else {
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
    }
}