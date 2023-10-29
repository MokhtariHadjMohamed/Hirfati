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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;
import com.hadjmohamed.hirfati.Craftsman;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;
import com.hadjmohamed.hirfati.User;

import java.util.ArrayList;
import java.util.List;

public class AdminReport extends AppCompatActivity implements RecViewInterface, View.OnClickListener {

    private Report report;

    // RecyclerView
    private RecyclerView recyclerViewReport;
    private List<Report> reportList;
    private AdapterRecReport adapterRecReport;
    // toolbar
    private Toolbar toolbar;
    private ImageView backArrow, imageViewToolBar;
    private TextView toolbarTitle;
    // Firestor
    private FirebaseFirestore firestore;
    // progressDialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report);
        firestore = FirebaseFirestore.getInstance();

        // toolbar
        toolbar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolbar);
        backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(this);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        imageViewToolBar = findViewById(R.id.imageViewToolBar);

        // Progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        // recycle view
        recyclerViewReport = findViewById(R.id.reportRecAdmin);
        reportList = new ArrayList<>();
        adapterRecReport = new AdapterRecReport(getApplicationContext(),
                reportList, this);
        getReport();

    }

    private void getReport(){
        firestore.collection("Reports")
                .orderBy("readSituation", Query.Direction.DESCENDING)
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
                            reportList.add(d.toObject(Report.class));
                        }
                        adapterRecReport.notifyDataSetChanged();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
        recyclerViewReport.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerViewReport.setAdapter(adapterRecReport);
    }

    @Override
    public void onItemClick(String view, int position) {
        Intent intent = new Intent(AdminReport.this, AdminReportInfo.class);
        intent.putExtra("idReport", reportList.get(position).getIdReport());
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if(view == backArrow){
            startActivity(new Intent(AdminReport.this, AdminHomePage.class));
            finish();
        }
    }
}