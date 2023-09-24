package com.hadjmohamed.hirfati.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.hadjmohamed.hirfati.R;

public class AdminCraftsInfo extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewToolsBar;
    private ImageView backArrowToolsBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_crafts_info);

        Toolbar toolBar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolBar);
        textViewToolsBar = findViewById(R.id.toolbarTitle);
        backArrowToolsBar = findViewById(R.id.backArrow);

        textViewToolsBar.setText("الحرفة");
        backArrowToolsBar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == backArrowToolsBar)
            startActivity(new Intent(AdminCraftsInfo.this, AdminCrafts.class));
    }
}