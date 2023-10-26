package com.hadjmohamed.hirfati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hadjmohamed.hirfati.Admin.AdminHomePage;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logIn, craftsmenRegister, userRegister;
    private TextView errorCraftsman;
    private EditText email, password;
    private EditText[] editTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        logIn = findViewById(R.id.logIn);
        userRegister = findViewById(R.id.userRegisterLogIn);
        craftsmenRegister = findViewById(R.id.craftsmanRegisterLogIn);

        email = findViewById(R.id.emailSignIn);
        password = findViewById(R.id.passwordSignIn);
        errorCraftsman = findViewById(R.id.errorLogInActivity);

        editTexts = new EditText[]{email, password};

        userRegister.setOnClickListener(this);
        craftsmenRegister.setOnClickListener(this);
        logIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == logIn){
            if (editTest(editTexts))
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
}