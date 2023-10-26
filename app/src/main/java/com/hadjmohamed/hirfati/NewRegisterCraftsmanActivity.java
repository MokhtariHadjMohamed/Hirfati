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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewRegisterCraftsmanActivity extends AppCompatActivity
        implements View.OnClickListener{

    //Edit text
    private EditText name, familyName, address, birthday, phoneNumber, email,
            password, password02, descriptionCraftsman;
    private EditText[] editTextsPage01, editTextsPage02;
    private TextView errorCraftsman;
    private Button next, submit, logIn, goBack;
    private Spinner stateCraftsman, citesCraftsman, crafts, level, exYears;
    private DatePickerDialog datePickerDialog;
    private User user;

    // FireBase
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    // progressDialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new User();
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        page01();
    }

    private void page01(){
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
                                user.setBirthday(i2 + "/" + (i1 + 1) + "/" + i);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // spinner
        stateCraftsman = findViewById(R.id.stateCraftsman);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.wilaya,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateCraftsman.setAdapter(adapter);

        editTextsPage01 = new EditText[]{name, familyName, address, birthday};
    }

    private void page02(){
        setContentView(R.layout.activity_new_register_craftsman02);
        goBack = findViewById(R.id.goBackCraftsman);
        submit = findViewById(R.id.submitNewRegisterCraftsman);
        submit.setOnClickListener(this);
        goBack.setOnClickListener(this);
        errorCraftsman = findViewById(R.id.errorCraftsmanPage02);

        crafts = findViewById(R.id.craftCraftsman);
        level = findViewById(R.id.levelCraftsman);
        exYears = findViewById(R.id.yearsCraftsman);
        phoneNumber =findViewById(R.id.phoneCraftsman);
        email = findViewById(R.id.emailCraftsman);
        password = findViewById(R.id.password01Craftsman);
        password02 = findViewById(R.id.password02Craftsman);
        descriptionCraftsman = findViewById(R.id.descriptionCraftsman);

        // crafts spinner
        List<String> craftsList = new ArrayList();
        craftsList.add("ميكانيكي");
        craftsList.add("كهربائي");
        craftsList.add("مصور");
        ArrayAdapter<String> adapterCrafts = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, craftsList);
        adapterCrafts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        crafts.setAdapter(adapterCrafts);
        // level spinner
        ArrayAdapter<CharSequence> adapterLevel = ArrayAdapter.createFromResource(this,R.array.level,
                android.R.layout.simple_spinner_item);
        adapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        level.setAdapter(adapterLevel);
        level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // exYears spinner
        ArrayAdapter<CharSequence> adapterExYears = ArrayAdapter.createFromResource(this,R.array.exYears,
                android.R.layout.simple_spinner_item);
        adapterExYears.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exYears.setAdapter(adapterExYears);
        editTextsPage02 = new EditText[]{phoneNumber, email, password, password02};
    }

    private boolean editTest(EditText[] editTexts){
        for (EditText e:editTexts) {
            if (e.getText().toString().isEmpty()){
                e.setBackgroundResource(R.drawable.custom_input_error);
                errorCraftsman.setText("إملء كل خانات");
                return false;
            }
            e.setBackgroundResource(R.drawable.custom_input);
        }
        errorCraftsman.setText("");
        return true;
    }

    @Override
    public void onClick(View view) {
        if (next == view){
            if (editTest(editTextsPage01)){
                user.setName(name.getText().toString());
                user.setFamilyName(familyName.getText().toString());
                user.setAddress(address.getText().toString());
                user.setState(stateCraftsman.getSelectedItem().toString());
//                user.setCite(citesCraftsman.getSelectedItem().toString());
                page02();
            }
        } else if (logIn == view) {
            startActivity(new Intent(NewRegisterCraftsmanActivity.this, LogInActivity.class));
        } else if (goBack == view) {
            page01();
        } else if (view == submit) {
            if (editTest(editTextsPage02))
                if (password.getText().toString().equals(password02.getText().toString())){
                    // Progress
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Fetching data...");
                    progressDialog.show();

                    user.setPhoneNumber(phoneNumber.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setCraft(crafts.getSelectedItem().toString());
                    user.setLevel(level.getSelectedItem().toString());
                    user.setExYears(exYears.getSelectedItem().toString());
                    user.setUserType("Craftsman");
                    user.setDescription(descriptionCraftsman.getText().toString());

                    addAuth(email.getText().toString(), password.getText().toString());

                }else{
                    errorCraftsman.setText("كلمة سر غير متطابقتان");
                    password02.setBackgroundResource(R.drawable.custom_input_error);
                }
        }
    }

    private void addAuth(String email, String password){
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

    private void addUserFirestor(String uid){
        user.setIdUser(uid);
        firestore.collection("Users")
                .document(uid).set(user)
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
}