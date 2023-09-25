package com.hadjmohamed.hirfati.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hadjmohamed.hirfati.AdapterRecComment;
import com.hadjmohamed.hirfati.Comment;
import com.hadjmohamed.hirfati.CraftsmanAccountActivity;
import com.hadjmohamed.hirfati.CraftsmanAccountInfoActivity;
import com.hadjmohamed.hirfati.R;

import java.util.ArrayList;
import java.util.List;

public class AdminCraftsmenAccount extends AppCompatActivity implements View.OnClickListener {

    private Button craftsmanInfo, reportCraftsman, deleteCraftsmen;
    private TextView textViewToolsBar;
    private ImageView backArrowToolsBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_craftsmen_account);

        craftsmanInfo = findViewById(R.id.craftsmanInfoAdminCraftsmenAccount);
        reportCraftsman = findViewById(R.id.reportCraftsmanAdminCraftsmenAccount);
        deleteCraftsmen = findViewById(R.id.deleteCraftsmenAdminCraftsmenAccount);

        craftsmanInfo.setOnClickListener(this);
        reportCraftsman.setOnClickListener(this);
        deleteCraftsmen.setOnClickListener(this);

        // ToolsBar
        Toolbar toolBar = findViewById(R.id.toolbar_back_arrow);
        setSupportActionBar(toolBar);
        textViewToolsBar = findViewById(R.id.toolbarTitle);
        backArrowToolsBar = findViewById(R.id.backArrow);

        textViewToolsBar.setText("الحرفي");
        backArrowToolsBar.setOnClickListener(this);

        // comment RecyclerView
        RecyclerView recyclerView = findViewById(R.id.commentAdminCraftsmenAccount);
        List<Comment> commentList = new ArrayList<>();
        commentList.add(new Comment(null, "جاك سيمسون", "بلا بلا بلا بلا"));
        commentList.add(new Comment(null, "جاك سيمسون", "بلا بلا بلا بلا"));
        commentList.add(new Comment(null, "جاك سيمسون", "بلا بلا بلا بلا"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(new AdapterRecComment(getApplicationContext(), commentList));
    }

    @Override
    public void onClick(View view) {
        if (view == craftsmanInfo)
            startActivity(new Intent(AdminCraftsmenAccount.this, CraftsmanAccountInfoActivity.class));
        else if(view == reportCraftsman)
            Toast.makeText(this, "Report", Toast.LENGTH_SHORT).show();
        else if(view == deleteCraftsmen)
            Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();
        else if(view == backArrowToolsBar){
            startActivity(new Intent(AdminCraftsmenAccount.this, AdminCraftsmen.class));
            finish();
        }
    }
}