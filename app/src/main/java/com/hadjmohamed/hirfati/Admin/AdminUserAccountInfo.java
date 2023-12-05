package com.hadjmohamed.hirfati.Admin;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hadjmohamed.hirfati.City;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.State;
import com.hadjmohamed.hirfati.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
    private List<String> stateList, cityList;
    private ArrayAdapter<CharSequence> adapterState, adapterCity;
    // birthday
    private DatePickerDialog datePickerDialog;
    // toolbar
    private Toolbar toolbar;
    private ImageView backArrow, imageViewToolBar;
    private TextView toolbarTitle;

    //firebase
    private FirebaseFirestore firestore;
    private User user;
    // Dialog delete
    private MaterialAlertDialogBuilder dialogBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_account_info);
        idUser = getIntent().getStringExtra("idUser");
        firestore = FirebaseFirestore.getInstance();
        user = new User();

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

        // birthday
        birthday.setFocusable(false);
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // data Piker
                datePickerDialog = new DatePickerDialog(AdminUserAccountInfo.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                birthday.setText(i2 + "/" + (i1 + 1) + "/" + i);
                                user.setBirthday(i2 + "/" + (i1 + 1) + "/" + i);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // adapterState
        stateList = new ArrayList<>();
        adapterState = new ArrayAdapter(this, R.layout.spinner_item, stateList);

        // adapterCity
        cityList = new ArrayList<>();
        adapterCity = new ArrayAdapter(this, R.layout.spinner_item, cityList);


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
        itemStatesSelected();
    }

    private void itemStatesSelected(){
        states.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityList.clear();
                cityList.add(0, "بلديات");
                getCity(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                        stateList.add(0, user.getState());
                        cityList.add(0, user.getCity());
                        getCity(user.getState());
                        retrieveImage(userImageAdmin, user.getIdUser());
                    }
                });
    }


    private void getStatus(){
        stateList.add("ولايات");
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

    private void getCity(String uid){
        firestore.collection("City")
                .whereEqualTo("state_name", uid)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.e("GetUsers", "failed");
                            return;
                        }
                        for(QueryDocumentSnapshot d: task.getResult()){
                            cityList.add(d.toObject(City.class).getCommune_name());
                        }
                        adapterCity.notifyDataSetChanged();
                    }
                });
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(adapterCity);
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
        deleteReport();
    }

    private void deleteReport(){
        firestore.collection("Reports")
                .whereEqualTo("reporter", idUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.e("Report delete: ", "failed");
                            return;
                        }
                        for (QueryDocumentSnapshot d: task.getResult()){
                            Report report = d.toObject(Report.class);
                            firestore.collection("Reports").document(report.getIdReport()).delete();
                        }

                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == backArrow){
            Intent intent = new Intent(AdminUserAccountInfo.this, AdminUserAccount.class);
            intent.putExtra("idUser", idUser);
            startActivity(intent);
            finish();
        } else if (view == close) {
            dialogBuilder = new MaterialAlertDialogBuilder(this);
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

            alertDialog = dialogBuilder.create();
            alertDialog.show();
        } else if (view == submit){
            if (editTest(editTexts) &&
                    !states.getSelectedItem().toString().equals("ولايات") &&
            !city.getSelectedItem().toString().equals("بلديات")){
                user.setIdUser(idUser);
                user.setName(name.getText().toString());
                user.setFamilyName(familyName.getText().toString());
                user.setBirthday(birthday.getText().toString());
                user.setEmail(email.getText().toString());
                user.setAddress(address.getText().toString());
                user.setPhoneNumber(phone.getText().toString());
                user.setState(states.getSelectedItem().toString());
                user.setCity(city.getSelectedItem().toString());
                user.setUserType("User");
                updateUser(user);
            }else if(states.getSelectedItem().toString().equals("ولايات")){
                states.setBackgroundResource(R.drawable.custom_input_error);
            }else if(city.getSelectedItem().toString().equals("بلديات")){
                city.setBackgroundResource(R.drawable.custom_input_error);
            }
        }
    }
}