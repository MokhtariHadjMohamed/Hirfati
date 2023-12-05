package com.hadjmohamed.hirfati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewRegisterCraftsmanActivity extends AppCompatActivity
        implements View.OnClickListener {

    // Element
    private EditText name, familyName, address, birthday, phoneNumber, email,
            password, password02, descriptionCraftsman;
    private EditText[] editTextsPage01, editTextsPage02;
    private TextView errorCraftsman;
    private Button next, submit, logIn, goBack;
    private Spinner stateCraftsman, cityCraftsman, crafts, level, exYears;
    private DatePickerDialog datePickerDialog;
    private Craftsman craftsman;

    // states and city
    private List<String> stateList, cityList;
    private ArrayAdapter<CharSequence> adapterState, adapterCity;
    // crafts, level and years
    private List<String> craftsList, levelList, yearsList;
    private ArrayAdapter<String> adapterCrafts, adapterLevel, adapterExYears;

    // FireBase
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    // progressDialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        craftsman = new Craftsman();
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        page01();
    }

    private void page01() {
        setContentView(R.layout.activity_new_register_craftsman01);
        next = findViewById(R.id.nextBtnCraftsman01);
        logIn = findViewById(R.id.goBackSignInCraftsman);
        next.setOnClickListener(this);
        logIn.setOnClickListener(this);
        errorCraftsman = findViewById(R.id.errorCraftsmanPage01);

        //info
        name = findViewById(R.id.nameCraftsman);
        familyName = findViewById(R.id.familyNameCraftsman);
        address = findViewById(R.id.addressCraftsman);

        // birthday selected
        birthday = findViewById(R.id.dateCraftsman);
        birthday.setFocusable(false);
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // data Piker
                datePickerDialog = new DatePickerDialog(NewRegisterCraftsmanActivity.this,
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
        stateCraftsman = findViewById(R.id.stateCraftsman);
        cityCraftsman = findViewById(R.id.cityCraftsman);

        // adapterState
        stateList = new ArrayList<>();
        adapterState = new ArrayAdapter(this, R.layout.spinner_item, stateList);

        // adapterCity
        cityList = new ArrayList<>();
        adapterCity = new ArrayAdapter(this, R.layout.spinner_item, cityList);

        getStatus();
        itemStatesSelected();

        editTextsPage01 = new EditText[]{name, familyName, address, birthday};
    }

    private void itemStatesSelected(){
        stateCraftsman.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        stateCraftsman.setAdapter(adapterState);
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
        cityCraftsman.setAdapter(adapterCity);
    }


    private void page02() {
        setContentView(R.layout.activity_new_register_craftsman02);
        goBack = findViewById(R.id.goBackCraftsman);
        submit = findViewById(R.id.submitNewRegisterCraftsman);
        submit.setOnClickListener(this);
        goBack.setOnClickListener(this);
        errorCraftsman = findViewById(R.id.errorCraftsmanPage02);

        crafts = findViewById(R.id.craftCraftsman);
        level = findViewById(R.id.levelCraftsman);
        exYears = findViewById(R.id.yearsCraftsman);
        phoneNumber = findViewById(R.id.phoneCraftsman);
        email = findViewById(R.id.emailCraftsman);
        password = findViewById(R.id.password01Craftsman);
        password02 = findViewById(R.id.password02Craftsman);
        descriptionCraftsman = findViewById(R.id.descriptionCraftsman);

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                EditText e = (EditText) view;
                if (!b){
                    if(e.getText().toString().length() <= 6)
                        errorCraftsman.setText("كلمة سر يجب ان تكون أكثر من 6 أحرف");
                    else
                        errorCraftsman.setVisibility(View.GONE);
                }
            }
        });
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
        adapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        level.setAdapter(adapterLevel);
        // exYears spinner
        yearsList = new ArrayList<>();
        yearsList.add("سنوات الخبرة");
        yearsList.add("أقل من سنة");
        yearsList.add("مابين سنة و4 سنوات");
        yearsList.add("اكثر من 4 سنوات");
        adapterExYears = new ArrayAdapter<>(this,
                R.layout.spinner_item, yearsList);
        adapterExYears.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exYears.setAdapter(adapterExYears);

        editTextsPage02 = new EditText[]{phoneNumber, email, password, password02};
    }

    private void getCrafts() {
        craftsList.add("الحرف");
        firestore.collection("Crafts")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("GetUsers", "failed");
                            return;
                        }
                        for (QueryDocumentSnapshot d : task.getResult()) {
                            craftsList.add(d.toObject(Crafts.class).getName());
                        }
                        adapterCrafts.notifyDataSetChanged();
                    }
                });

        adapterCrafts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        crafts.setAdapter(adapterCrafts);
    }

    private boolean editTest(EditText[] editTexts) {
        for (EditText e : editTexts) {
            if (e.getText().toString().isEmpty()) {
                e.setBackgroundResource(R.drawable.custom_input_error);
                errorCraftsman.setText("إملء كل خانات");
                errorCraftsman.setVisibility(View.VISIBLE);
                return false;
            }
            e.setBackgroundResource(R.drawable.custom_input);
        }
        errorCraftsman.setVisibility(View.GONE);
        return true;
    }
    private void addAuth(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        addUserFirestor(authResult.getUser().getUid());
                        Toast.makeText(NewRegisterCraftsmanActivity.this, "تم تسجيل", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Log.e("Register", e.getMessage());
                    }
                });
    }

    private void addUserFirestor(String uid) {
        craftsman.setIdUser(uid);
        firestore.collection("Users")
                .document(uid).set(craftsman)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Upload User", "success");
                        startActivity(new Intent(NewRegisterCraftsmanActivity.this, HomePageActivity.class));
                        finish();
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Log.e("Upload User", e.getMessage());
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (next == view) {
            if (editTest(editTextsPage01) && !stateCraftsman.getSelectedItem().toString().equals("ولايات") &&
                    !cityCraftsman.getSelectedItem().toString().equals("بلديات")) {
                craftsman.setName(name.getText().toString());
                craftsman.setFamilyName(familyName.getText().toString());
                craftsman.setAddress(address.getText().toString());
                craftsman.setState(stateCraftsman.getSelectedItem().toString());
                craftsman.setCity(cityCraftsman.getSelectedItem().toString());
                page02();
            } else if (stateCraftsman.getSelectedItem().toString().equals("ولايات")) {
                stateCraftsman.setBackgroundResource(R.drawable.custom_input_error);
            } else if (cityCraftsman.getSelectedItem().toString().equals("بلديات")) {
                cityCraftsman.setBackgroundResource(R.drawable.custom_input_error);
            }
        } else if (logIn == view) {
            startActivity(new Intent(NewRegisterCraftsmanActivity.this, LogInActivity.class));
        } else if (goBack == view) {
            page01();
        } else if (view == submit) {
            if (editTest(editTextsPage02) &&
                    !level.getSelectedItem().toString().equals("مستوى") &&
                    !exYears.getSelectedItem().toString().equals("سنوات الخبرة") &&
                    !crafts.getSelectedItem().toString().equals("الحرف")) {
                if (password.getText().toString().equals(password02.getText().toString())
                    && password.getText().toString().length() > 6) {
                    // Progress
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Fetching data...");
                    progressDialog.show();

                    craftsman.setPhoneNumber(phoneNumber.getText().toString());
                    craftsman.setEmail(email.getText().toString());
                    craftsman.setCraft(crafts.getSelectedItem().toString());
                    craftsman.setLevel(level.getSelectedItem().toString());
                    craftsman.setExYears(exYears.getSelectedItem().toString());
                    craftsman.setUserType("Craftsman");
                    craftsman.setDescription(descriptionCraftsman.getText().toString());

                    addAuth(email.getText().toString(), password.getText().toString());

                } else if (!password.getText().toString().equals(password02.getText().toString())){
                    errorCraftsman.setText("كلمة سر غير متطابقتان");
                    password02.setBackgroundResource(R.drawable.custom_input_error);
                    password02.setBackgroundResource(R.drawable.custom_input_error);
                    errorCraftsman.setVisibility(View.VISIBLE);
                }else if (password.getText().toString().length() <= 6){
                    errorCraftsman.setText("كلمة سر يجب ان تكون أكثر من 6 أحرف");
                    errorCraftsman.setVisibility(View.VISIBLE);
                }
            }else if(level.getSelectedItem().toString().equals("مستوى")){
                level.setBackgroundResource(R.drawable.custom_input_error);
            }else if(exYears.getSelectedItem().toString().equals("سنوات الخبرة")){
                exYears.setBackgroundResource(R.drawable.custom_input_error);
            }else if(crafts.getSelectedItem().toString().equals("الحرف")){
                crafts.setBackgroundResource(R.drawable.custom_input_error);
            }
        }
    }
}