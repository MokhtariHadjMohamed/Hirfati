package com.hadjmohamed.hirfati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SearchPage extends AppCompatActivity {

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
                startActivity(new Intent(SearchPage.this, HomePage.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                return true;
            } else if (id == R.id.searchNavigation) {
                return true;
            } else if (id == R.id.accountNavigation) {
                startActivity(new Intent(SearchPage.this, UserAccount.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                return true;
            } else if (id == R.id.categoryNavigation) {
                startActivity(new Intent(SearchPage.this, CategoryPage.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                return true;
            } else {
                return false;
            }
        });

    }
}