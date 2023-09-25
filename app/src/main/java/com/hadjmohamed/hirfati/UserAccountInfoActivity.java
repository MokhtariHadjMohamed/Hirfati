package com.hadjmohamed.hirfati;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class UserAccountInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewToolsBar;
    private ImageView backArrowToolsBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account_info);

        // ToolsBar
        Toolbar toolBar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolBar);
        textViewToolsBar = findViewById(R.id.toolbarTitle);
        backArrowToolsBar = findViewById(R.id.backArrow);

        textViewToolsBar.setText("معلومات الحساب");
        backArrowToolsBar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == backArrowToolsBar){
            startActivity(new Intent(UserAccountInfoActivity.this, UserAccountActivity.class));
            finish();
        }
    }
}