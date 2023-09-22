package com.hadjmohamed.hirfati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

        // navigation bar Bottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar_home);
        bottomNavigationView.setSelectedItemId(R.id.searchNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homeNavigation) {
                startActivity(new Intent(SearchPageActivity.this, HomePageActivity.class));
                return true;
            } else if (id == R.id.searchNavigation) {
                return true;
            } else if (id == R.id.accountNavigation) {
                startActivity(new Intent(SearchPageActivity.this, UserAccountActivity.class));
                return true;
            } else if (id == R.id.categoryNavigation) {
                startActivity(new Intent(SearchPageActivity.this, CategoryPageActivity.class));
                return true;
            } else {
                return false;
            }
        });

    }
}