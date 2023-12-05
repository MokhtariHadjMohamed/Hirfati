package com.hadjmohamed.hirfati;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hadjmohamed.hirfati.Admin.AdminHomePage;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logIn, craftsmenRegister, userRegister;
    private TextView errorCraftsman, forgetPassword;
    private EditText email, password;
    private EditText[] editTexts;
    private User user;
    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    // progressDialog
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        logIn = findViewById(R.id.logIn);
        userRegister = findViewById(R.id.userRegisterLogIn);
        craftsmenRegister = findViewById(R.id.craftsmanRegisterLogIn);

        email = findViewById(R.id.emailSignIn);
        password = findViewById(R.id.passwordSignIn);
        forgetPassword = findViewById(R.id.forgetPasswordLogIn);
        errorCraftsman = findViewById(R.id.errorLogInActivity);

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

        editTexts = new EditText[]{email, password};

        forgetPassword.setOnClickListener(this);

        userRegister.setOnClickListener(this);
        craftsmenRegister.setOnClickListener(this);
        logIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == logIn){
            if (editTest(editTexts)){
                // Progress
                progressDialog = new ProgressDialog(this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Fetching data...");
                progressDialog.show();

                singInUser(email.getText().toString(), password.getText().toString());
            }
        } else if (view == userRegister) {
            startActivity(new Intent(LogInActivity.this, NewRegisterUserActivity.class));
        } else if (view == craftsmenRegister) {
            startActivity(new Intent(LogInActivity.this, NewRegisterCraftsmanActivity.class));
        }else if (view == forgetPassword){
            Intent intent = new Intent(LogInActivity.this, ForgetPasswordActivity.class);
            intent.putExtra("email", email.getText().toString());
            startActivity(intent);
        }
    }

    private void singInUser(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                getUser(authResult.getUser().getUid());
                Toast.makeText(LogInActivity.this, "تم تسجيل الدخول", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Log.e("SingIn error: ", e.getMessage());
                    }
                });
    }

    private void getUser(String uid){
        firestore.collection("Users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (!task.isSuccessful()){
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.d("getUser:", "Failed");
                            return;
                        }
                        user = task.getResult().toObject(User.class);
                        if (Objects.equals(user.getUserType(), "Admin")){
                            startActivity(new Intent(LogInActivity.this, AdminHomePage.class));
                            finish();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                        else if (Objects.equals(user.getUserType(), "User")
                                || Objects.equals(user.getUserType(), "Craftsman")){
                            startActivity(new Intent(LogInActivity.this, HomePageActivity.class));
                            finish();
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
                });
    }

    private boolean editTest(EditText[] editTexts){
        for (EditText e:editTexts) {
            if (e.getText().toString().isEmpty()){
                e.setBackgroundResource(R.drawable.custom_input_error);
                errorCraftsman.setText("إملء كل خانات");
                errorCraftsman.setVisibility(View.VISIBLE);
                return false;
            }else if(e.getText().toString().length() <= 6){
                errorCraftsman.setText("كلمة سر يجب ان تكون أكثر من 6 أحرف");
                errorCraftsman.setVisibility(View.VISIBLE);
                return false;
            }else
                errorCraftsman.setVisibility(View.GONE);
            e.setBackgroundResource(R.drawable.custom_input);
        }
        errorCraftsman.setText("");
        return true;
    }
}