package com.hadjmohamed.hirfati.Admin;

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
import com.hadjmohamed.hirfati.Craftsman;
import com.hadjmohamed.hirfati.HomePageActivity;
import com.hadjmohamed.hirfati.NewRegisterCraftsmanActivity;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdminCraftsmenAccountAdd extends AppCompatActivity implements View.OnClickListener {

    private EditText nameAdmin, familyNameAdmin, birthdayAdmin, addressAdmin, emailAdmin,
            phoneNumberAdmin, passwordAdmin, password02Admin, descriptionAdmin;
    private EditText[] editTexts;
    private Spinner statesAdmin, cityAdmin, craftsAdmin, levelAdmin, yearsAdmin;
    private Button submitAdmin;
    private TextView errorAdmin;
    // birthday
    private DatePickerDialog datePickerDialog;

    private Craftsman craftsman;
    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;

    // progressDialog
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_craftsmen_account_add);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        craftsman = new Craftsman();

        // get all element
        errorAdmin = findViewById(R.id.errorAdminCraftsmenAccountAdd);
        nameAdmin = findViewById(R.id.nameAdminCraftsmenAccountAdd);
        familyNameAdmin = findViewById(R.id.familyNameAdminCraftsmenAccountAdd);
        addressAdmin = findViewById(R.id.addressAdminCraftsmenAccountAdd);
        emailAdmin = findViewById(R.id.emailAdminCraftsmenAccountAdd);
        phoneNumberAdmin = findViewById(R.id.phoneNumberAdminCraftsmenAccountAdd);
        passwordAdmin = findViewById(R.id.passwordAdminCraftsmenAccountAdd);
        password02Admin = findViewById(R.id.password02AdminCraftsmenAccountAdd);
        descriptionAdmin = findViewById(R.id.descriptionAdminCraftsmenAccountAdd);
        statesAdmin = findViewById(R.id.statesAdminCraftsmenAccountAdd);
        cityAdmin = findViewById(R.id.cityAdminCraftsmenAccountAdd);
        craftsAdmin = findViewById(R.id.craftsAdminCraftsmenAccountAdd);
        levelAdmin = findViewById(R.id.levelAdminCraftsmenAccountAdd);
        yearsAdmin = findViewById(R.id.yearsAdminCraftsmenAccountAdd);
        submitAdmin = (Button) findViewById(R.id.submitAdminCraftsmenAccountAdd);

        // birthday selected
        birthdayAdmin = (EditText) findViewById(R.id.birthdayAdminCraftsmenAccountAdd);
        birthdayAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // data Piker
                datePickerDialog = new DatePickerDialog(AdminCraftsmenAccountAdd.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                birthdayAdmin.setText(i2 + "/" + (i1 + 1) + "/" + i);
                                craftsman.setBirthday(i2 + "/" + (i1 + 1) + "/" + i);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        editTexts = new EditText[]{nameAdmin, familyNameAdmin, birthdayAdmin, addressAdmin,
                emailAdmin, phoneNumberAdmin, passwordAdmin, password02Admin};

        // spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.wilaya,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statesAdmin.setAdapter(adapter);

        // crafts spinner
        List<String> craftsList = new ArrayList();
        craftsList.add("ميكانيكي");
        craftsList.add("كهربائي");
        craftsList.add("مصور");
        ArrayAdapter<String> adapterCrafts = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, craftsList);
        adapterCrafts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        craftsAdmin.setAdapter(adapterCrafts);
        // level spinner
        ArrayAdapter<CharSequence> adapterLevel = ArrayAdapter.createFromResource(this,R.array.level,
                android.R.layout.simple_spinner_item);
        adapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelAdmin.setAdapter(adapterLevel);
        // exYears spinner
        ArrayAdapter<CharSequence> adapterExYears = ArrayAdapter.createFromResource(this,R.array.exYears,
                android.R.layout.simple_spinner_item);
        adapterExYears.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearsAdmin.setAdapter(adapterExYears);

        submitAdmin.setOnClickListener(this);
    }

    private boolean editTest(EditText[] editTexts){
        for (EditText e:editTexts) {
            if (e.getText().toString().isEmpty()){
                e.setBackgroundResource(R.drawable.custom_input_error);
                errorAdmin.setText("إملء كل خانات");
                return false;
            }
            e.setBackgroundResource(R.drawable.custom_input);
        }
        errorAdmin.setText("");
        return true;
    }
    @Override
    public void onClick(View view) {
        if (view == submitAdmin){
            if (editTest(editTexts)){
                if (passwordAdmin.getText().toString().equals(password02Admin.getText().toString())) {
                    // Progress
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Fetching data...");
                    progressDialog.show();

                    craftsman.setName(nameAdmin.getText().toString());
                    craftsman.setFamilyName(familyNameAdmin.getText().toString());
                    craftsman.setAddress(addressAdmin.getText().toString());
//                    user.setCity(cityAdmin.getSelectedItem().toString());
                    craftsman.setState(statesAdmin.getSelectedItem().toString());
                    craftsman.setEmail(emailAdmin.getText().toString());
                    craftsman.setCraft(craftsAdmin.getSelectedItem().toString());
                    craftsman.setLevel(levelAdmin.getSelectedItem().toString());
                    craftsman.setExYears(yearsAdmin.getSelectedItem().toString());
                    craftsman.setDescription(descriptionAdmin.getText().toString());
                    craftsman.setUserType("Craftsman");

                    addAuth(emailAdmin.getText().toString(), passwordAdmin.getText().toString());
                }else{
                    errorAdmin.setText("كلمة سر غير متطابقتان");
                    passwordAdmin.setBackgroundResource(R.drawable.custom_input_error);
                    password02Admin.setBackgroundResource(R.drawable.custom_input_error);
                }
            }
        }
    }

    private void addAuth(String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        addUserFirestor(authResult.getUser().getUid());
                        Toast.makeText(AdminCraftsmenAccountAdd.this, "تم تسجيل", Toast.LENGTH_SHORT).show();
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
        craftsman.setIdUser(uid);
        firestore.collection("Users")
                .document(uid).set(craftsman)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Upload User", "success");
                        startActivity(new Intent(AdminCraftsmenAccountAdd.this, AdminCraftsmen.class));
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