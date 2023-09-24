package com.hadjmohamed.hirfati.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.hadjmohamed.hirfati.AdapterRecCategoryHor;
import com.hadjmohamed.hirfati.AdapterRecCategoryVer;
import com.hadjmohamed.hirfati.Category;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;

import java.util.ArrayList;
import java.util.List;

public class AdminCrafts extends AppCompatActivity implements RecViewInterface {

    RecyclerView recyclerViewCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_crafts);

        // RecyclerView Category
        recyclerViewCategory = findViewById(R.id.craftsAdmin);
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(new Category("سباك", Uri.parse("android.resource:" + R.drawable.logo)));
        categoryList.add(new Category("نجار", Uri.parse("android.resource:" + R.drawable.logo)));
        categoryList.add(new Category("كهربائي", Uri.parse("android.resource:" + R.drawable.logo)));
        categoryList.add(new Category("طيار", Uri.parse("android.resource:" + R.drawable.logo)));

        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerViewCategory.setAdapter(new AdapterRecCategoryVer(getApplicationContext(),
                categoryList, this));

    }

    @Override
    public void onItemClick(String view, int position) {
        startActivity(new Intent(AdminCrafts.this, AdminCraftsInfo.class));
    }
}