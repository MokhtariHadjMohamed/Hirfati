package com.hadjmohamed.hirfati.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hadjmohamed.hirfati.Craftsman;
import com.hadjmohamed.hirfati.R;

public class AdminCraftsmenAccountInfo extends AppCompatActivity implements View.OnClickListener {

    private EditText name, familyName, birthday, address, desc, email, phone;
    private Spinner states, city, crafts, level, years;
    private TextView textViewToolsBar, craftsmanNumber;
    private String idUser;
    private ImageView backArrowToolsBar;
    // Firestor
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_craftsmen_account_info);
        idUser = getIntent().getStringExtra("idUser");
        firestore = FirebaseFirestore.getInstance();

        //element
        craftsmanNumber = findViewById(R.id.craftsmanNumberAdminCraftsmenAccountInfo);
        name = findViewById(R.id.nameAdminCraftsmenAccountInfo);
        familyName = findViewById(R.id.familyNameAdminCraftsmenAccountInfo);
        birthday = findViewById(R.id.birthdayAdminCraftsmenAccountInfo);
        address = findViewById(R.id.addressAdminCraftsmenAccountInfo);
        desc = findViewById(R.id.descAdminCraftsmenAccountInfo);
        email = findViewById(R.id.emailAdminCraftsmenAccountInfo);
        phone = findViewById(R.id.phoneNumberAdminCraftsmenAccountInfo);
        states = findViewById(R.id.statesAdminCraftsmenAccountInfo);
        city = findViewById(R.id.cityAdminCraftsmenAccountInfo);
        crafts = findViewById(R.id.craftsAdminCraftsmenAccountInfo);
        level = findViewById(R.id.levelAdminCraftsmenAccountInfo);
        years = findViewById(R.id.yearsAdminCraftsmenAccountInfo);

        // ToolsBar
        Toolbar toolBar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolBar);
        textViewToolsBar = findViewById(R.id.toolbarTitle);
        backArrowToolsBar = findViewById(R.id.backArrow);

        textViewToolsBar.setText("معلومات الحرفي");
        backArrowToolsBar.setOnClickListener(this);

        getUser();
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
                        craftsmanNumber.setText(craftsman.getIdUser());
                        name.setText(craftsman.getName());
                        familyName.setText(craftsman.getFamilyName());
                        birthday.setText(craftsman.getBirthday());
                        address.setText(craftsman.getAddress());
                        desc.setText(craftsman.getDescription());
                        email.setText(craftsman.getEmail());
                        phone.setText(craftsman.getPhoneNumber());
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == backArrowToolsBar){
            startActivity(new Intent(AdminCraftsmenAccountInfo.this, AdminCraftsmenAccount.class));
            finish();
        }
    }
}