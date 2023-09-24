package com.hadjmohamed.hirfati.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.SearchManager;
import android.app.SearchManager.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hadjmohamed.hirfati.R;

public class AdminHomePage extends AppCompatActivity implements View.OnClickListener {

    private CardView craftsmenCard, userCard, craftsCard, craftsmen, user, crafts, report, userOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);

        craftsCard = findViewById(R.id.craftsCardAdminHomePage);
        craftsmenCard = findViewById(R.id.craftsmanCardAdminHomePage);
        userCard = findViewById(R.id.userCardAdminHomePage);

        craftsmen = findViewById(R.id.craftsmanAdminHomePage);
        crafts = findViewById(R.id.craftsAdminHomePage);
        user = findViewById(R.id.userAdminHomePage);

        userOnline = findViewById(R.id.userOnlineAdminHomePage);
        report = findViewById(R.id.reportAdminHomePage);

        crafts.setOnClickListener(this);
        craftsmen.setOnClickListener(this);
        user.setOnClickListener(this);

        craftsCard.setOnClickListener(this);
        craftsmenCard.setOnClickListener(this);
        userCard.setOnClickListener(this);

        userOnline.setOnClickListener(this);
        report.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view == craftsmen || view == craftsmenCard)
            startActivity(new Intent(AdminHomePage.this, AdminCraftsmen.class));
        else if(view == craftsCard || view == crafts)
            startActivity(new Intent(AdminHomePage.this, AdminCrafts.class));
        else if(view == userCard || view == user)
            startActivity(new Intent(AdminHomePage.this, AdminUser.class));
        else if (view == report)
            startActivity(new Intent(AdminHomePage.this, AdminReport.class));
        else if (view == userOnline)
            startActivity(new Intent(AdminHomePage.this, AdminOnlineUsers.class));
    }
}