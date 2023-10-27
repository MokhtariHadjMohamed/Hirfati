package com.hadjmohamed.hirfati;

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

public class CraftsmanAccountInfoActivity extends AppCompatActivity implements View.OnClickListener {

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
        setContentView(R.layout.activity_craftsman_account_info);
        idUser = getIntent().getStringExtra("idUser");
        firestore = FirebaseFirestore.getInstance();

        //element
        craftsmanNumber = findViewById(R.id.craftsmanNumberCraftsmanAccountInfo);
        name = findViewById(R.id.nameCraftsmanAccountInfo);
        familyName = findViewById(R.id.familyNameCraftsmanAccountInfo);
        birthday = findViewById(R.id.birthdayCraftsmanAccountInfo);
        address = findViewById(R.id.addressCraftsmanAccountInfo);
        desc = findViewById(R.id.descCraftsmanAccountInfo);
        email = findViewById(R.id.emailCraftsmanAccountInfo);
        phone = findViewById(R.id.phoneNumberCraftsmanAccountInfo);

        states = findViewById(R.id.statesCraftsmanAccountInfo);
        city = findViewById(R.id.cityCraftsmanAccountInfo);
        crafts = findViewById(R.id.craftsCraftsmanAccountInfo);
        level = findViewById(R.id.levelCraftsmanAccountInfo);
        years = findViewById(R.id.yearsCraftsmanAccountInfo);

        // ToolsBar
        Toolbar toolBar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolBar);
        textViewToolsBar = findViewById(R.id.toolbarTitle);
        backArrowToolsBar = findViewById(R.id.backArrow);

        textViewToolsBar.setText("معلومات الحساب");
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
            startActivity(new Intent(CraftsmanAccountInfoActivity.this, CraftsmanAccountActivity.class));
            finish();
        }
    }
}