package com.hadjmohamed.hirfati.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.hadjmohamed.hirfati.AdapterRecCraftsmen;
import com.hadjmohamed.hirfati.Craftsman;
import com.hadjmohamed.hirfati.CraftsmanInfoActivity;
import com.hadjmohamed.hirfati.HomePageActivity;
import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdminCraftsmen extends AppCompatActivity implements RecViewInterface {

    RecyclerView recyclerViewCraftsmen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_craftsmen);

        // recyclerView Craftsmen
        recyclerViewCraftsmen = findViewById(R.id.craftsmenAdmin);
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
        recyclerViewCraftsmen.setAdapter(new AdapterRecCraftsmen(getApplicationContext(),
                craftsmanList, this));
    }

    @Override
    public void onItemClick(String view, int position) {
        if (Objects.equals(view, "Craftsmen"))
            startActivity(new Intent(AdminCraftsmen.this, AdminCraftsmenAccount.class));
    }
}