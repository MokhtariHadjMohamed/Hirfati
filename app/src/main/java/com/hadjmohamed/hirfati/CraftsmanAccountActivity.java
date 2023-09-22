package com.hadjmohamed.hirfati;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CraftsmanAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView accountInfoCraftsmanAccount, settingCraftsmanAccount, logOutCraftsAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craftsman_account);

        accountInfoCraftsmanAccount = findViewById(R.id.accountInfoCraftsmanAccount);
        settingCraftsmanAccount = findViewById(R.id.settingUserAccount);
        logOutCraftsAccount = findViewById(R.id.logOutUserAccount);

        accountInfoCraftsmanAccount.setOnClickListener(this);
        settingCraftsmanAccount.setOnClickListener(this);
        logOutCraftsAccount.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == accountInfoCraftsmanAccount){
            startActivity(new Intent(CraftsmanAccountActivity.this, CraftsmanAccountInfoActivity.class));
        }else if (view == settingCraftsmanAccount){
            startActivity(new Intent(CraftsmanAccountActivity.this, SettingsPageActivity.class));
        }else if (view == logOutCraftsAccount)
            startActivity(new Intent(CraftsmanAccountActivity.this, LogInActivity.class));
    }
}