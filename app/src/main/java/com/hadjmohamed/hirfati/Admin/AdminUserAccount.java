package com.hadjmohamed.hirfati.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hadjmohamed.hirfati.R;

public class AdminUserAccount extends AppCompatActivity implements View.OnClickListener {

    private Button userInfo, reportUser, deleteUser;
    private TextView textViewToolsBar;
    private ImageView backArrowToolsBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_account);

        userInfo = findViewById(R.id.userInfoAdminUserAccount);
        reportUser = findViewById(R.id.reportUserAdminUserAccount);
        deleteUser = findViewById(R.id.deleteUserAdminUserAccount);

        userInfo.setOnClickListener(this);
        reportUser.setOnClickListener(this);
        deleteUser.setOnClickListener(this);

        // ToolsBar
        Toolbar toolBar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolBar);
        textViewToolsBar = findViewById(R.id.toolbarTitle);
        backArrowToolsBar = findViewById(R.id.backArrow);

        textViewToolsBar.setText("المستعمل");
        backArrowToolsBar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == userInfo)
            startActivity(new Intent(AdminUserAccount.this, AdminUserAccountInfo.class));
        else if (view == reportUser)
            Toast.makeText(this, "Report", Toast.LENGTH_SHORT).show();
        else if (view == deleteUser)
            Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
        else if(view == backArrowToolsBar)
            startActivity(new Intent(AdminUserAccount.this, AdminUser.class));
    }
}