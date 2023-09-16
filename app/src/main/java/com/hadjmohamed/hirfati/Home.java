package com.hadjmohamed.hirfati;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // navigation bar Bottom
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar_home);
        bottomNavigationView.setSelectedItemId(R.id.homeNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homeNavigation) {
                return true;
            } else if (id == R.id.searchNavigation) {
                Toast.makeText(this, "Search", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.accountNavigation) {
                Toast.makeText(this, "Account", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.categoryNavigation) {
                Toast.makeText(this, "Category", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });

        // RecyclerView Category Create
        RecyclerView recyclerViewCategory = findViewById(R.id.categoryHome);
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("سباك", Uri.parse("android.resource:" + R.drawable.logo)));
        categoryList.add(new Category("نجار", Uri.parse("android.resource:" + R.drawable.logo)));
        categoryList.add(new Category("كهربائي", Uri.parse("android.resource:" + R.drawable.logo)));
        categoryList.add(new Category("طيار", Uri.parse("android.resource:" + R.drawable.logo)));

        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategory.setAdapter(new AdapterRecCategory(getApplicationContext(), categoryList));

        // recyclerView Craftsmen Crate
        RecyclerView recyclerViewCraftsmen = findViewById(R.id.craftsmenHome);
        List<Craftsman> craftsmanList = new ArrayList<>();
        craftsmanList.add(new Craftsman("حاج", "مختاري",
                Uri.parse("android.resource:" + R.drawable.logo),
                "لا بلا بلا بلا بلا بلا", "سباك"));
        craftsmanList.add(new Craftsman("حاج", "مختاري",
                Uri.parse("android.resource:" + R.drawable.logo),
                "لا بلا بلا بلا بلا بلا", "سباك"));
        craftsmanList.add(new Craftsman("حاج", "مختاري",
                Uri.parse("android.resource:" + R.drawable.logo),
                "لا بلا بلا بلا بلا بلا", "سباك"));

        recyclerViewCraftsmen.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerViewCraftsmen.setAdapter(new AdapterRecCraftsmen(getApplicationContext(), craftsmanList));
    }
}