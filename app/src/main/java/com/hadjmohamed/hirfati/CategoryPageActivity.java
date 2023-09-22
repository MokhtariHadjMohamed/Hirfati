package com.hadjmohamed.hirfati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CategoryPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);

        // navigation bar Bottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar_home);
        bottomNavigationView.setSelectedItemId(R.id.categoryNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homeNavigation) {
                startActivity(new Intent(CategoryPageActivity.this, HomePageActivity.class));
                return true;
            } else if (id == R.id.searchNavigation) {
                startActivity(new Intent(CategoryPageActivity.this, SearchPageActivity.class));
                return true;
            } else if (id == R.id.accountNavigation) {
                startActivity(new Intent(CategoryPageActivity.this, UserAccountActivity.class));
                return true;
            } else if (id == R.id.categoryNavigation) {
                return true;
            } else {
                return false;
            }
        });
    }
}