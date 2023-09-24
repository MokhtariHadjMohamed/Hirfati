package com.hadjmohamed.hirfati.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;
import com.hadjmohamed.hirfati.User;

import java.util.ArrayList;
import java.util.List;

public class AdminReport extends AppCompatActivity implements RecViewInterface {

    private RecyclerView recyclerViewReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report);

        // recycle view
        recyclerViewReport = findViewById(R.id.reportRecAdmin);
        List<Report> reportList = new ArrayList<>();
        reportList.add(new Report("حاج", "مختاري", "بلا بلا بلا بلا"));
        reportList.add(new Report("حاج", "مختاري", "بلا بلا بلا بلا"));
        reportList.add(new Report("حاج", "مختاري", "بلا بلا بلا بلا"));
        reportList.add(new Report("حاج", "مختاري", "بلا بلا بلا بلا"));


        recyclerViewReport.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerViewReport.setAdapter(new AdapterRecReport(getApplicationContext(),
                reportList, this));

    }

    @Override
    public void onItemClick(String view, int position) {

    }
}