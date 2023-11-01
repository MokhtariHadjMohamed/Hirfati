package com.hadjmohamed.hirfati.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hadjmohamed.hirfati.Craftsman;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.State;

import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminCraftsmenAccountInfo extends AppCompatActivity implements View.OnClickListener {

    private EditText name, familyName, birthday, address, desc, email, phone;
    private Spinner states, city, crafts, level, years;
    private Button submit;
    private TextView delete, craftsmanNumber, errorAdmin;
    private ImageView craftsmanImage;
    private EditText[] editTexts;
    private List<String> stateList;
    private ArrayAdapter<CharSequence> adapterState;
    // toolbar
    private Toolbar toolbar;
    private ImageView backArrow, imageViewToolBar;
    private TextView toolbarTitle;
    // Firestor
    private FirebaseFirestore firestore;
    private String idUser;
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
        errorAdmin = findViewById(R.id.errorAdminAdminCraftsmenAccountInfo);
        craftsmanImage = findViewById(R.id.craftsmanImageUserAccountInfo);

        // adapterState
        stateList = new ArrayList<>();
        adapterState = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stateList);

        submit = findViewById(R.id.submitAdminCraftsmenAccountInfo);
        delete = findViewById(R.id.deleteAdminCraftsmenAccountInfo);

        editTexts = new EditText[]{ name, familyName, birthday, address, desc, email, phone};
        // ToolsBar
        Toolbar toolBar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolBar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        backArrow = findViewById(R.id.backArrow);

        toolbarTitle.setText("معلومات الحرفي");
        backArrow.setOnClickListener(this);

        getUser();
        getStatus();
    }

    private void getStatus(){
        firestore.collection("States")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.e("GetUsers", "failed");
                            return;
                        }
                        for(QueryDocumentSnapshot d: task.getResult()){
                            stateList.add(d.toObject(State.class).getAr_name());
                        }
                        adapterState.notifyDataSetChanged();
                    }
                });
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        states.setAdapter(adapterState);
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
                        stateList.add(craftsman.getState());
                        retrieveImage(craftsmanImage, craftsman.getIdUser());
                    }
                });
    }

    private void updateCraftsman(Craftsman craftsman){
        HashMap<String, Object> craftsmen = craftsman.toHashMap();
        craftsmen.put("craft", craftsman.getCraft());
        craftsmen.put("level", craftsman.getLevel());
        craftsmen.put("exYears", craftsman.getExYears());
        craftsmen.put("description", craftsman.getDescription());

        firestore.collection("Users")
                .document(idUser).update(craftsmen).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(AdminCraftsmenAccountInfo.this, AdminCraftsmenAccount.class);
                        intent.putExtra("idUser", idUser);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Craftsmen update: ", "update failed");
                    }
                });
    }
    private void deleteCraftsman(){
        firestore.collection("Users")
                .document(idUser).delete();
        FirebaseAuth.getInstance().getCurrentUser().delete();
    }

    private boolean editTest(EditText[] editTexts) {
        for (EditText e : editTexts) {
            if (e.getText().toString().isEmpty()) {
                e.setBackgroundResource(R.drawable.custom_input_error);
                errorAdmin.setText("إملء كل خانات");
                return false;
            }
            e.setBackgroundResource(R.drawable.custom_input);
        }
        errorAdmin.setText("");
        return true;
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
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    imageView.setImageResource(R.drawable.baseline_image_not_supported_24);
                    Log.e("Image " + image, "Failed");
                }
            });
        } catch (IOException e) {
            imageView.setImageResource(R.drawable.baseline_image_not_supported_24);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == backArrow){
            Intent intent = new Intent(AdminCraftsmenAccountInfo.this, AdminCraftsmenAccount.class);
            intent.putExtra("idUser", idUser);
            startActivity(intent);
            finish();
        } else if (view == submit) {
            if (editTest(editTexts)){
                Craftsman craftsman = new Craftsman();
                craftsman.setIdUser(idUser);
                craftsman.setName(name.getText().toString());
                craftsman.setFamilyName(familyName.getText().toString());
                craftsman.setBirthday(birthday.getText().toString());
                craftsman.setAddress(address.getText().toString());
                craftsman.setDescription(desc.getText().toString());
                craftsman.setPhoneNumber(phone.getText().toString());
                craftsman.setState(states.getSelectedItem().toString());
                craftsman.setCity(city.getSelectedItem().toString());
                craftsman.setCraft(crafts.getSelectedItem().toString());
                craftsman.setLevel(level.getSelectedItem().toString());
                craftsman.setExYears(years.getSelectedItem().toString());
                updateCraftsman(craftsman);
            }
        } else if (view == delete) {
            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
            dialogBuilder.setMessage("هل تريد حذف هذه الحرفة؟");
            dialogBuilder.setTitle("الطلب");
            dialogBuilder.setCancelable(false);
            dialogBuilder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteCraftsman();
                }
            }).setNegativeButton("لا", (DialogInterface.OnClickListener) (dialog, which) -> {
                // If user click no then dialog box is canceled.
                dialog.cancel();
            });

        }
    }
}