package com.hadjmohamed.hirfati;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class NewRegisterUserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name, familyName, email, address, password, password02, birthday, phoneNumber;
    private TextView errorUser;
    private Spinner state, city;
    private EditText[] editTextsPage01, editTextsPage02;

    private Button next, submit, logIn, goBack;
    private DatePickerDialog datePickerDialog;
    private User user;
    // FireBase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    // progressDialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = new User();
        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        page01();
    }

    private void page01(){
        setContentView(R.layout.activity_new_register_user01);

        name = findViewById(R.id.nameUser);
        familyName = findViewById(R.id.familyNameUser);
        address = findViewById(R.id.addressUser);

        errorUser = findViewById(R.id.errorUser);

        // spinner
        state = findViewById(R.id.stateUser);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.wilaya,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(adapter);

        // birthday selected
        birthday = findViewById(R.id.dateUser);
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // data Piker
                datePickerDialog = new DatePickerDialog(NewRegisterUserActivity.this,
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

        next = findViewById(R.id.nextBtnUser);
        logIn = findViewById(R.id.goBackSignInUser);

        next.setOnClickListener(this);
        logIn.setOnClickListener(this);

        editTextsPage01 = new EditText[]{name, familyName, address, birthday};
    }

    private void page02(){
        setContentView(R.layout.activity_new_register_user02);

        phoneNumber = findViewById(R.id.phoneNumberUser);
        email = findViewById(R.id.emailUser);
        password = findViewById(R.id.password01User);
        password02 = findViewById(R.id.password02User);

        errorUser = findViewById(R.id.errorUser2);

        goBack = findViewById(R.id.goBackUser);
        submit = findViewById(R.id.submitNewRegisterUser);
        submit.setOnClickListener(this);
        goBack.setOnClickListener(this);

        editTextsPage02 = new EditText[]{phoneNumber, email, password, password02};
    }

    private boolean editTest(EditText[] editTexts){
        for (EditText e:editTexts) {
            if (e.getText().toString().isEmpty()){
                e.setBackgroundResource(R.drawable.custom_input_error);
                errorUser.setText("إملء كل خانات");
                return false;
            }
            e.setBackgroundResource(R.drawable.custom_input);
        }
        errorUser.setText("");
        return true;
    }

    private void addAuth(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        addUserFirestor(authResult.getUser().getUid());
                        Toast.makeText(NewRegisterUserActivity.this, "تم تسجيل", Toast.LENGTH_SHORT).show();
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
                        startActivity(new Intent(NewRegisterUserActivity.this, HomePageActivity.class));
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
        if (next == view){
            if (editTest(editTextsPage01)){
                user.setName(name.getText().toString());
                user.setFamilyName(familyName.getText().toString());
                user.setAddress(address.getText().toString());
                user.setState(state.getSelectedItem().toString());
                page02();
            }
        } else if (logIn == view) {
            startActivity(new Intent(NewRegisterUserActivity.this, LogInActivity.class));
        } else if (goBack == view) {
            page01();
        } else if (view == submit) {
            if (editTest(editTextsPage02)){
                if (password.getText().toString().equals(password02.getText().toString())){
                    // Progress
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Fetching data...");
                    progressDialog.show();

                    user.setPhoneNumber(phoneNumber.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setUserType("User");

                    addAuth(email.getText().toString(), password.getText().toString());
                }else{
                    errorUser.setText("كلمة سر غير متطابقتان");
                    password02.setBackgroundResource(R.drawable.custom_input_error);
                }
            }
        }
    }
}