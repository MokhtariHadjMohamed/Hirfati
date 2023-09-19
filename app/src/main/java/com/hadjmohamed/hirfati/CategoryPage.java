package com.hadjmohamed.hirfati;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CategoryPage extends AppCompatActivity {

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
                startActivity(new Intent(CategoryPage.this, HomePage.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                return true;
            } else if (id == R.id.searchNavigation) {
                startActivity(new Intent(CategoryPage.this, SearchPage.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                return true;
            } else if (id == R.id.accountNavigation) {
                startActivity(new Intent(CategoryPage.this, UserAccount.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                return true;
            } else if (id == R.id.categoryNavigation) {
                return true;
            } else {
                return false;
            }
        });
    }
}