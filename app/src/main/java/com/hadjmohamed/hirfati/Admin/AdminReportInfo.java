package com.hadjmohamed.hirfati.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hadjmohamed.hirfati.R;

public class AdminReportInfo extends AppCompatActivity implements View.OnClickListener {
    private String idReport;
    // Element
    private TextView craftsNumber, reportHim, reported;
    private EditText reportDesc;
    private Button showReportHim, showReported, submit, delete;

    // Firebase
    private FirebaseFirestore firestore;
    // ProgressDialog
    private ProgressDialog progressDialog;
    // toolbar
    private Toolbar toolbar;
    private ImageView backArrow, imageViewToolBar;
    private TextView toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report_info);
        idReport = getIntent().getStringExtra("idReport");
        firestore = FirebaseFirestore.getInstance();

        // Element
        craftsNumber = findViewById(R.id.craftsNumberAdminReportInfo);
        reportHim = findViewById(R.id.reportHimAdminReportInfo);
        reported = findViewById(R.id.reportedAdminReportInfo);
        reportDesc = findViewById(R.id.reportAdminReportInfo);
        showReportHim = findViewById(R.id.showReportHimAdminReportInfo);
        showReportHim.setOnClickListener(this);
        showReported = findViewById(R.id.showReportedAdminReportInfo);
        showReported.setOnClickListener(this);
        submit = findViewById(R.id.submitAdminReportInfo);
        submit.setOnClickListener(this);
        delete = findViewById(R.id.deleteAdminReportInfo);
        delete.setOnClickListener(this);

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

        getReport();
    }

    private void getReport(){
        firestore.collection("Reports")
                .document(idReport)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.e("GetUsers", "failed");
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            return;
                        }
                        Report report = task.getResult().toObject(Report.class);
                        craftsNumber.setText(report.getIdReport());
                        reportHim.setText(report.getReporter());
                        reported.setText(report.getReported());
                        reportDesc.setText(report.getDesc());
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                });
    }

    private void doDelete(String uid){
        firestore.collection("Reports")
                .document(uid)
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Delete; ", "Success");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Delete: ", e.getMessage());
                    }
                });
    }

    private void getUpdate(String uid){
        firestore.collection("Reports")
                .document(uid)
                .update("readSituation", true);
    }

    @Override
    public void onClick(View view) {
        if (view == submit) {
            getUpdate(idReport);
        } else if (view == showReportHim) {
            Intent intent = new Intent(AdminReportInfo.this, AdminUserAccount.class);
            intent.putExtra("idUser", reportHim.getText().toString());
            startActivity(intent);
        } else if (view == showReported) {
            Intent intent = new Intent(AdminReportInfo.this, AdminCraftsmenAccount.class);
            intent.putExtra("idUser", reported.getText().toString());
            startActivity(intent);
        } else if (view == delete) {
            doDelete(idReport);
        } else if (view == backArrow) {
            startActivity(new Intent(AdminReportInfo.this, AdminReport.class));
            finish();
        }
    }
}