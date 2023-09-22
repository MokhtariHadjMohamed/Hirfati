package com.hadjmohamed.hirfati;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NewRegisterUserActivity extends AppCompatActivity implements View.OnClickListener {

    private Button next, submit, logIn, goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register_user01);

        next = findViewById(R.id.nextBtnUser);
        logIn = findViewById(R.id.goBackSignInUser);

        next.setOnClickListener(this);
        logIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (next == view){
            setContentView(R.layout.activity_new_register_user02);
            goBack = findViewById(R.id.goBackUser);
            submit = findViewById(R.id.submitNewRegisterUser);
            submit.setOnClickListener(this);
            goBack.setOnClickListener(this);
        } else if (logIn == view) {
            startActivity(new Intent(NewRegisterUserActivity.this, LogInActivity.class));
        } else if (goBack == view) {
            setContentView(R.layout.activity_new_register_user01);
            next = findViewById(R.id.nextBtnUser);
            logIn = findViewById(R.id.goBackSignInUser);
            next.setOnClickListener(this);
            logIn.setOnClickListener(this);
        } else if (view == submit) {
            startActivity(new Intent(NewRegisterUserActivity.this, HomePageActivity.class));
        }
    }
}