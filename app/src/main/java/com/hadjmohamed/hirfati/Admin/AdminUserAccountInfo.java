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
import com.hadjmohamed.hirfati.Crafts;
import com.hadjmohamed.hirfati.Craftsman;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.State;
import com.hadjmohamed.hirfati.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminUserAccountInfo extends AppCompatActivity implements View.OnClickListener {

    // Element
    private EditText name, familyName, birthday, address, email, phone;
    private ImageView userImageAdmin;
    private Spinner states, city;
    private Button submit;
    private TextView close, errorAdmin, numberAdmin;
    private EditText[] editTexts;
    private String idUser;

    private List<String> stateList;
    private ArrayAdapter<CharSequence> adapterState;
    // toolbar
    private Toolbar toolbar;
    private ImageView backArrow, imageViewToolBar;
    private TextView toolbarTitle;

    //firebase
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_account_info);
        idUser = getIntent().getStringExtra("idUser");
        firestore = FirebaseFirestore.getInstance();

        // Element
        numberAdmin = findViewById(R.id.userNumberAdminUserAccountInfo);
        name = findViewById(R.id.nameAdminUserAccountInfo);
        familyName = findViewById(R.id.familyNameAdminUserAccountInfo);
        birthday = findViewById(R.id.birthdayAdminUserAccountInfo);
        address = findViewById(R.id.addressAdminUserAccountInfo);
        email = findViewById(R.id.emailAdminUserAccountInfo);
        phone = findViewById(R.id.phoneAdminUserAccountInfo);
        userImageAdmin = findViewById(R.id.userImageAdminUserAccountInfo);

        editTexts = new EditText[]{name, familyName, birthday, address, email, phone};

        states = findViewById(R.id.statesAdminUserAccountInfo);
        city = findViewById(R.id.cityAdminUserAccountInfo);
        errorAdmin = findViewById(R.id.errorAdminUserAccountInfo);

        // adapterState
        stateList = new ArrayList<>();
        adapterState = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stateList);

        submit = findViewById(R.id.submitAdminUserAccountInfo);
        submit.setOnClickListener(this);
        close = findViewById(R.id.closeAccountAdminUserAccountInfo);
        close.setOnClickListener(this);

        // ToolsBar
        toolbar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        backArrow = findViewById(R.id.backArrow);

        toolbarTitle.setText("معلومات الحساب");
        backArrow.setOnClickListener(this);

        getUser();
        getStatus();
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
                        numberAdmin.setText(user.getIdUser());
                        name.setText(user.getName());
                        familyName.setText(user.getFamilyName());
                        birthday.setText(user.getBirthday());
                        address.setText(user.getAddress());
                        email.setText(user.getEmail());
                        phone.setText(user.getPhoneNumber());
                        stateList.add(user.getState());
                        retrieveImage(userImageAdmin, user.getIdUser());
                    }
                });
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

    private void updateUser(User user){
        HashMap<String, Object> userMap = user.toHashMap();
        firestore.collection("Users")
                .document(idUser).update(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Intent intent = new Intent(AdminUserAccountInfo.this,
                                AdminUserAccount.class);
                        intent.putExtra("idUser", idUser);
                        startActivity(intent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Craftsmen update: ", "update failed");
                    }
                });;
    }

    private void deleteUser(String uid){
        firestore.collection("Users").document(uid).delete();
        FirebaseAuth.getInstance().getCurrentUser().delete();
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

    @Override
    public void onClick(View view) {
        if (view == backArrow){
            Intent intent = new Intent(AdminUserAccountInfo.this, AdminUserAccount.class);
            intent.putExtra("idUser", idUser);
            startActivity(intent);
            finish();
        } else if (view == close) {
            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(this);
            dialogBuilder.setMessage("هل تريد حذف هذه الحرفة؟");
            dialogBuilder.setTitle("الطلب");
            dialogBuilder.setCancelable(false);
            dialogBuilder.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteUser(idUser);
                }
            }).setNegativeButton("لا", (DialogInterface.OnClickListener) (dialog, which) -> {
                // If user click no then dialog box is canceled.
                dialog.cancel();
            });
        } else if (view == submit){
            if (editTest(editTexts)){
                User user = new User();
                user.setIdUser(idUser);
                user.setName(name.getText().toString());
                user.setFamilyName(familyName.getText().toString());
                user.setBirthday(birthday.getText().toString());
                user.setAddress(address.getText().toString());
                user.setPhoneNumber(phone.getText().toString());
                user.setState(states.getSelectedItem().toString());
//                user.setCity(city.getSelectedItem().toString());

                updateUser(user);
            }
        }
    }
}