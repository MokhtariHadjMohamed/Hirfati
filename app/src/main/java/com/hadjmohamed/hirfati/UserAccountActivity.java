package com.hadjmohamed.hirfati;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class UserAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView accountInfoUserAccount, settingUserAccount, logOutUserAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        accountInfoUserAccount = findViewById(R.id.accountInfoUserAccount);
        settingUserAccount = findViewById(R.id.settingUserAccount);
        logOutUserAccount = findViewById(R.id.logOutUserAccount);

        accountInfoUserAccount.setOnClickListener(this);
        settingUserAccount.setOnClickListener(this);
        logOutUserAccount.setOnClickListener(this);


        // navigation bar Bottom
        BottomNavigation();

        // comment RecyclerView
        RecyclerView recyclerView = findViewById(R.id.commentUserAccount);
        List<Comment> commentList = new ArrayList<>();
//        commentList.add(new Comment(null, "جاك سيمسون", "بلا بلا بلا بلا"));
//        commentList.add(new Comment(null, "جاك سيمسون", "بلا بلا بلا بلا"));
//        commentList.add(new Comment(null, "جاك سيمسون", "بلا بلا بلا بلا"));

        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(new AdapterRecComment(getApplicationContext(), commentList));
    }

    private void BottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation_bar_home);
        bottomNavigationView.setSelectedItemId(R.id.accountNavigation);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.homeNavigation) {
                startActivity(new Intent(UserAccountActivity.this, HomePageActivity.class));
                return true;
            } else if (id == R.id.searchNavigation) {
                startActivity(new Intent(UserAccountActivity.this, SearchPageActivity.class));
                return true;
            } else if (id == R.id.accountNavigation) {
                return true;
            } else if (id == R.id.categoryNavigation) {
                startActivity(new Intent(UserAccountActivity.this, CraftsPageActivity.class));
                return true;
            } else {
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == accountInfoUserAccount){
            startActivity(new Intent(UserAccountActivity.this, UserAccountInfoActivity.class));
        }else if (view == settingUserAccount){
            startActivity(new Intent(UserAccountActivity.this, SettingsPageActivity.class));
        }else if (view == logOutUserAccount){
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(UserAccountActivity.this, LogInActivity.class));
            finish();
        }
    }
}