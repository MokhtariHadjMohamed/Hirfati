package com.hadjmohamed.hirfati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hadjmohamed.hirfati.Admin.AdminHomePage;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logIn, craftsmenRegister, userRegister;
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        logIn = findViewById(R.id.logIn);
        userRegister = findViewById(R.id.userRegisterLogIn);
        craftsmenRegister = findViewById(R.id.craftsmanRegisterLogIn);

        email = findViewById(R.id.emailSignIn);
        password = findViewById(R.id.passwordSignIn);

        userRegister.setOnClickListener(this);
        craftsmenRegister.setOnClickListener(this);
        logIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == logIn){
            if (email.getText().toString().equals("admin"))
                startActivity(new Intent(LogInActivity.this, AdminHomePage.class));
            else
                startActivity(new Intent(LogInActivity.this, HomePageActivity.class));
        } else if (view == userRegister) {
            startActivity(new Intent(LogInActivity.this, NewRegisterUserActivity.class));
        } else if (view == craftsmenRegister) {
            startActivity(new Intent(LogInActivity.this, NewRegisterCraftsmanActivity.class));
        }
    }
}