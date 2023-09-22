package com.hadjmohamed.hirfati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CraftsmanInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craftsman_info);

        // navigation bar Bottom
        navigationBottom();

    }

    private void navigationBottom(){
        // navigation bar Bottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar_home);
        bottomNavigationView.setSelectedItemId(R.id.homeNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homeNavigation) {
                startActivity(new Intent(CraftsmanInfoActivity.this, HomePageActivity.class));
                return true;
            } else if (id == R.id.searchNavigation) {
                startActivity(new Intent(CraftsmanInfoActivity.this, SearchPageActivity.class));
                return true;
            } else if (id == R.id.accountNavigation) {
                startActivity(new Intent(CraftsmanInfoActivity.this, UserAccountActivity.class));
                return true;
            } else if (id == R.id.categoryNavigation) {
                startActivity(new Intent(CraftsmanInfoActivity.this, CategoryPageActivity.class));
                return true;
            } else {
                return false;
            }
        });
    }
}