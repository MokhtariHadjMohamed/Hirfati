package com.hadjmohamed.hirfati.Admin;

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
import com.hadjmohamed.hirfati.City;
import com.hadjmohamed.hirfati.Crafts;
import com.hadjmohamed.hirfati.Craftsman;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.State;

import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AdminCraftsmenAccountInfo extends AppCompatActivity implements View.OnClickListener {

    private EditText name, familyName, birthday, address, desc, email, phone;
    private Spinner states, city, crafts, level, years;
    private Button submit;
    private TextView delete, craftsmanNumber, errorAdmin;
    private ImageView craftsmanImage;
    private EditText[] editTexts;

    // birthday
    private DatePickerDialog datePickerDialog;
    // states and city
    private List<String> stateList, cityList;
    private ArrayAdapter<CharSequence> adapterState, adapterCity;
    // crafts, level and years
    private List<String> craftsList, levelList, yearsList;
    private ArrayAdapter<String> adapterCrafts, adapterLevel, adapterExYears;
    private Craftsman craftsman;
    // toolbar
    private Toolbar toolbar;
    private ImageView backArrow, imageViewToolBar;
    private TextView toolbarTitle;

    // Firestor
    private FirebaseFirestore firestore;
    private String idUser;

    // Dialog delete
    private MaterialAlertDialogBuilder dialogBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_craftsmen_account_info);
        idUser = getIntent().getStringExtra("idUser");
        firestore = FirebaseFirestore.getInstance();
        craftsman = new Craftsman();
        craftsman.setUserType("Craftsman");

        // ToolsBar
        toolbar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        backArrow = findViewById(R.id.backArrow);

        toolbarTitle.setText("معلومات الحرفي");
        backArrow.setOnClickListener(this);
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
                datePickerDialog = new DatePickerDialog(AdminCraftsmenAccountInfo.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                birthday.setText(i2 + "/" + (i1 + 1) + "/" + i);
                                craftsman.setBirthday(i2 + "/" + (i1 + 1) + "/" + i);
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

        submit = findViewById(R.id.submitAdminCraftsmenAccountInfo);
        delete = findViewById(R.id.deleteAdminCraftsmenAccountInfo);
        submit.setOnClickListener(this);
        delete.setOnClickListener(this);

        editTexts = new EditText[]{ name, familyName, birthday, address, desc, email, phone};

        // crafts spinner
        craftsList = new ArrayList();
        adapterCrafts = new ArrayAdapter<String>(this,
                R.layout.spinner_item, craftsList);
        getCrafts();

        // level spinner
        levelList = new ArrayList<>();
        levelList.add("مستوى");
        levelList.add("مبتدأ");
        levelList.add("متوسط");
        levelList.add("محترف");
        adapterLevel = new ArrayAdapter<>(this,
                R.layout.spinner_item, levelList);

        // exYears spinner
        yearsList = new ArrayList<>();
        yearsList.add("سنوات الخبرة");
        yearsList.add("أقل من سنة");
        yearsList.add("مابين سنة و4 سنوات");
        yearsList.add("اكثر من 4 سنوات");
        adapterExYears = new ArrayAdapter<>(this,
                R.layout.spinner_item, yearsList);

        getUser();
        getStatus();
        itemStatesSelected();
    }

    private void getCrafts(){
        craftsList.add("الحرف");
        firestore.collection("Crafts")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()){
                            Log.e("GetUsers", "failed");
                            return;
                        }
                        for(QueryDocumentSnapshot d: task.getResult()){
                            craftsList.add(d.toObject(Crafts.class).getName());
                        }
                        adapterCrafts.notifyDataSetChanged();
                    }
                });

        adapterCrafts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        crafts.setAdapter(adapterCrafts);
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
                        Craftsman craftsman = task.getResult().toObject(Craftsman.class);
                        craftsmanNumber.setText(craftsman.getIdUser());
                        name.setText(craftsman.getName());
                        familyName.setText(craftsman.getFamilyName());
                        birthday.setText(craftsman.getBirthday());
                        address.setText(craftsman.getAddress());
                        desc.setText(craftsman.getDescription());
                        email.setText(craftsman.getEmail());
                        phone.setText(craftsman.getPhoneNumber());
                        stateList.add(0, craftsman.getState());
                        cityList.add(0, craftsman.getCity());
                        craftsList.add(0, craftsman.getCraft());
                        levelList.add(0, craftsman.getLevel());
                        yearsList.add(0, craftsman.getExYears());
                        getCity(craftsman.getState());
                        retrieveImage(craftsmanImage, craftsman.getIdUser());
                        adapterLevel.notifyDataSetChanged();
                        adapterExYears.notifyDataSetChanged();
                    }

                });
        adapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        level.setAdapter(adapterLevel);
        adapterExYears.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        years.setAdapter(adapterExYears);
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
        deleteReport();
    }

    private void deleteReport(){
        firestore.collection("Reports")
                .whereEqualTo("reported", idUser)
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
            Intent intent = new Intent(AdminCraftsmenAccountInfo.this, AdminCraftsmenAccount.class);
            intent.putExtra("idUser", idUser);
            startActivity(intent);
            finish();
        } else if (view == submit) {
            if (editTest(editTexts) &&
                    !states.getSelectedItem().toString().equals("ولايات") &&
                    !city.getSelectedItem().toString().equals("بلديات") &&
                    !level.getSelectedItem().toString().equals("مستوى") &&
                    !years.getSelectedItem().toString().equals("سنوات الخبرة") &&
                    !crafts.getSelectedItem().toString().equals("الحرف")){
                craftsman.setIdUser(idUser);
                craftsman.setName(name.getText().toString());
                craftsman.setFamilyName(familyName.getText().toString());
                craftsman.setBirthday(birthday.getText().toString());
                craftsman.setAddress(address.getText().toString());
                craftsman.setDescription(desc.getText().toString());
                craftsman.setPhoneNumber(phone.getText().toString());
                craftsman.setEmail(email.getText().toString());
                craftsman.setState(states.getSelectedItem().toString());
                craftsman.setCity(city.getSelectedItem().toString());
                craftsman.setCraft(crafts.getSelectedItem().toString());
                craftsman.setLevel(level.getSelectedItem().toString());
                craftsman.setExYears(years.getSelectedItem().toString());
                updateCraftsman(craftsman);
            }else if(states.getSelectedItem().toString().equals("ولايات")){
                states.setBackgroundResource(R.drawable.custom_input_error);
            }else if(city.getSelectedItem().toString().equals("بلديات")){
                city.setBackgroundResource(R.drawable.custom_input_error);
            }else if(level.getSelectedItem().toString().equals("مستوى")){
                level.setBackgroundResource(R.drawable.custom_input_error);
            }else if(years.getSelectedItem().toString().equals("سنوات الخبرة")){
                years.setBackgroundResource(R.drawable.custom_input_error);
            }else if(crafts.getSelectedItem().toString().equals("الحرف")){
                crafts.setBackgroundResource(R.drawable.custom_input_error);
            }
        } else if (view == delete) {
            dialogBuilder = new MaterialAlertDialogBuilder(this);
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

            alertDialog = dialogBuilder.create();
            alertDialog.show();
        }
    }
}