package com.hadjmohamed.hirfati;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewRegisterCraftsmanActivity extends AppCompatActivity implements View.OnClickListener {

    private Button next, submit, logIn, goBack, birthday;
    private Spinner wilayatCraftsman, citesCraftsman, crafts, level, exYears;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page01();
    }

    private void page01(){
        setContentView(R.layout.activity_new_register_craftsman01);
        next = findViewById(R.id.nextBtnCraftsman01);
        logIn = findViewById(R.id.goBackSignInCraftsman);
        next.setOnClickListener(this);
        logIn.setOnClickListener(this);

        // birthday selected
        birthday = findViewById(R.id.dateCraftsman);
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                // data Piker

                datePickerDialog = new DatePickerDialog(NewRegisterCraftsmanActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                birthday.setText(i2 + "/" + (i1 + 1) + "/" + i);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // spinner
        wilayatCraftsman = findViewById(R.id.wilayatCraftsman);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.wilaya,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wilayatCraftsman.setAdapter(adapter);
    }

    private void page02(){
        setContentView(R.layout.activity_new_register_craftsman02);
        goBack = findViewById(R.id.goBackCraftsman);
        submit = findViewById(R.id.submitNewRegisterCraftsman);
        submit.setOnClickListener(this);
        goBack.setOnClickListener(this);

        crafts = findViewById(R.id.craftCraftsman);
        level = findViewById(R.id.levelCraftsman);
        exYears = findViewById(R.id.yearsCraftsman);

        // crafts spinner
        List<String> craftsList = new ArrayList();
        craftsList.add("ميكانيكي");
        craftsList.add("كهربائي");
        craftsList.add("مصور");
        ArrayAdapter<String> adapterCrafts = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, craftsList);
        adapterCrafts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        crafts.setAdapter(adapterCrafts);
        // level spinner
        ArrayAdapter<CharSequence> adapterLevel = ArrayAdapter.createFromResource(this,R.array.level,
                android.R.layout.simple_spinner_item);
        adapterLevel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        level.setAdapter(adapterLevel);
        // exYears spinner
        ArrayAdapter<CharSequence> adapterExYears = ArrayAdapter.createFromResource(this,R.array.exYears,
                android.R.layout.simple_spinner_item);
        adapterExYears.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exYears.setAdapter(adapterExYears);
    }

    @Override
    public void onClick(View view) {
        if (next == view){
            page02();
        } else if (logIn == view) {
            startActivity(new Intent(NewRegisterCraftsmanActivity.this, LogInActivity.class));
        } else if (goBack == view) {
            page01();
        } else if (view == submit) {
            startActivity(new Intent(NewRegisterCraftsmanActivity.this, HomePageActivity.class));
        }
    }
}