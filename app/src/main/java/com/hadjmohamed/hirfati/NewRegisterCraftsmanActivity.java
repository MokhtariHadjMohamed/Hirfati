package com.hadjmohamed.hirfati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewRegisterCraftsmanActivity extends AppCompatActivity implements View.OnClickListener {

    private Button next, submit, logIn, goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register_craftsman01);
        next =findViewById(R.id.nextBtnCraftsman01);
        logIn = findViewById(R.id.goBackSignInCraftsman);

        next.setOnClickListener(this);
        logIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (next == view){
            setContentView(R.layout.activity_new_register_craftsman02);
            goBack = findViewById(R.id.goBackCraftsman);
            submit = findViewById(R.id.submitNewRegisterCraftsman);
            submit.setOnClickListener(this);
            goBack.setOnClickListener(this);
        } else if (logIn == view) {
            startActivity(new Intent(NewRegisterCraftsmanActivity.this, LogInActivity.class));
        } else if (goBack == view) {
            setContentView(R.layout.activity_new_register_craftsman01);
            next = findViewById(R.id.nextBtnCraftsman01);
            logIn = findViewById(R.id.goBackSignInCraftsman);

            next.setOnClickListener(this);
            logIn.setOnClickListener(this);
        } else if (view == submit) {
            startActivity(new Intent(NewRegisterCraftsmanActivity.this, HomePageActivity.class));
        }
    }
}