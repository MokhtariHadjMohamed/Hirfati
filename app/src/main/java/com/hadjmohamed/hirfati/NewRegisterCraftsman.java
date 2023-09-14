package com.hadjmohamed.hirfati;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NewRegisterCraftsman extends AppCompatActivity implements View.OnClickListener {

    private Button next, logIn, goBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register_craftsman01);
        next =findViewById(R.id.nextBtnCraftsman);
        logIn = findViewById(R.id.goBackSignInCraftsman);

        next.setOnClickListener(this);
        logIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (next == view){
            setContentView(R.layout.activity_new_register_craftsman02);
            goBack = findViewById(R.id.goBackCraftsman);
            goBack.setOnClickListener(this);
        } else if (logIn == view) {

        } else if (goBack == view) {
            setContentView(R.layout.activity_new_register_craftsman01);
            next = findViewById(R.id.nextBtnCraftsman);
            logIn = findViewById(R.id.goBackSignInCraftsman);

            next.setOnClickListener(this);
            logIn.setOnClickListener(this);
        }
    }
}