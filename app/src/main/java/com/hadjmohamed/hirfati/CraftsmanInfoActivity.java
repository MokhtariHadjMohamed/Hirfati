package com.hadjmohamed.hirfati;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class CraftsmanInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewToolsBar;
    private ImageView backArrowToolsBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craftsman_info);

        // ToolsBar
        Toolbar toolBar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolBar);
        textViewToolsBar = findViewById(R.id.toolbarTitle);
        backArrowToolsBar = findViewById(R.id.backArrow);

        textViewToolsBar.setText("الحرفي");
        backArrowToolsBar.setOnClickListener(this);

        // comment RecyclerView
        RecyclerView recyclerView = findViewById(R.id.commentCraftsmanInfo);
        List<Comment> commentList = new ArrayList<>();
//        commentList.add(new Comment(null, "جاك سيمسون", "بلا بلا بلا بلا"));
//        commentList.add(new Comment(null, "جاك سيمسون", "بلا بلا بلا بلا"));
//        commentList.add(new Comment(null, "جاك سيمسون", "بلا بلا بلا بلا"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(new AdapterRecComment(getApplicationContext(), commentList));


    }

    @Override
    public void onClick(View view) {
        if (view == backArrowToolsBar){
            startActivity(new Intent(CraftsmanInfoActivity.this, HomePageActivity.class));
            finish();
        }
    }
}