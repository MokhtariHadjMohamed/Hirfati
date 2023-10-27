package com.hadjmohamed.hirfati.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hadjmohamed.hirfati.AdapterRecComment;
import com.hadjmohamed.hirfati.Comment;
import com.hadjmohamed.hirfati.Craftsman;
import com.hadjmohamed.hirfati.CraftsmanAccountActivity;
import com.hadjmohamed.hirfati.CraftsmanAccountInfoActivity;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.User;

import java.util.ArrayList;
import java.util.List;

public class AdminCraftsmenAccount extends AppCompatActivity implements View.OnClickListener {

    private Button craftsmanInfo, reportCraftsman, deleteCraftsmen;
    private TextView numberAdmin, nameAndFamilyNameAdmin, craftsAdmin, descAdmin;
    private String idUser;
    private TextView textViewToolsBar;
    private ImageView backArrowToolsBar;

    //firebase
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_craftsmen_account);
        idUser = getIntent().getStringExtra("idUser");
        firestore = FirebaseFirestore.getInstance();

        numberAdmin = findViewById(R.id.numberAdminCraftsmenAccount);
        nameAndFamilyNameAdmin = findViewById(R.id.nameAndFamilyNameAdminCraftsmenAccount);
        craftsAdmin = findViewById(R.id.craftsAdminCraftsmenAccount);
        descAdmin = findViewById(R.id.descAdminCraftsmenAccount);

        craftsmanInfo = findViewById(R.id.craftsmanInfoAdminCraftsmenAccount);
        reportCraftsman = findViewById(R.id.reportCraftsmanAdminCraftsmenAccount);
        deleteCraftsmen = findViewById(R.id.deleteCraftsmenAdminCraftsmenAccount);

        craftsmanInfo.setOnClickListener(this);
        reportCraftsman.setOnClickListener(this);
        deleteCraftsmen.setOnClickListener(this);

        // ToolsBar
        Toolbar toolBar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolBar);
        textViewToolsBar = findViewById(R.id.toolbarTitle);
        backArrowToolsBar = findViewById(R.id.backArrow);

        textViewToolsBar.setText("الحرفي");
        backArrowToolsBar.setOnClickListener(this);
        
        getUser();

        // comment RecyclerView
        RecyclerView recyclerView = findViewById(R.id.commentAdminCraftsmenAccount);
        List<Comment> commentList = new ArrayList<>();
        commentList.add(new Comment(null, "جاك سيمسون", "بلا بلا بلا بلا"));
        commentList.add(new Comment(null, "جاك سيمسون", "بلا بلا بلا بلا"));
        commentList.add(new Comment(null, "جاك سيمسون", "بلا بلا بلا بلا"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(new AdapterRecComment(getApplicationContext(), commentList));
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
                        Craftsman craftsman = task.getResult().toObject(Craftsman.class);
                        numberAdmin.setText(craftsman.getIdUser());
                        nameAndFamilyNameAdmin.setText(craftsman.getName() + " " + craftsman.getFamilyName());
                        craftsAdmin.setText(craftsman.getCraft());
                        descAdmin.setText(craftsman.getDescription());
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == craftsmanInfo){
            Intent intent = new Intent(AdminCraftsmenAccount.this, AdminCraftsmenAccountInfo.class);
            intent.putExtra("idUser", idUser);
            startActivity(intent);
        }
        else if(view == reportCraftsman)
            Toast.makeText(this, "Report", Toast.LENGTH_SHORT).show();
        else if(view == deleteCraftsmen)
            Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
        else if(view == backArrowToolsBar){
            startActivity(new Intent(AdminCraftsmenAccount.this, AdminCraftsmen.class));
            finish();
        }
    }
}