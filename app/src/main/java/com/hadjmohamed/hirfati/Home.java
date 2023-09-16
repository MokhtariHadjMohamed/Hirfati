package com.hadjmohamed.hirfati;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.categoryHome);
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("سباك", Uri.parse("android.resource:"+R.drawable.logo)));
        categoryList.add(new Category("نجار", Uri.parse("android.resource:"+R.drawable.logo)));
        categoryList.add(new Category("كهربائي", Uri.parse("android.resource:"+R.drawable.logo)));
        categoryList.add(new Category("طيار", Uri.parse("android.resource:"+R.drawable.logo)));

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new AdapterRecCategory(getApplicationContext(), categoryList));

    }
}