package com.hadjmohamed.hirfati.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hadjmohamed.hirfati.R;
import com.hadjmohamed.hirfati.RecViewInterface;

import java.util.List;

public class AdapterRecReport extends RecyclerView.Adapter<HolderRecReport>{

    private final RecViewInterface recViewInterface;
    Context context;
    List<Report> reportList;

    public AdapterRecReport(Context context, List<Report> reportList, RecViewInterface recViewInterface) {
        this.recViewInterface = recViewInterface;
        this.context = context;
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public HolderRecReport onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HolderRecReport(LayoutInflater.from(context).
                inflate(R.layout.report_item, parent, false), recViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderRecReport holder, int position) {
        holder.imageView.setImageResource(R.drawable.baseline_report_gmailerrorred_24);
        holder.idCraftsman.setText(reportList.get(position).getIdCraftsman());
        holder.idUser.setText(reportList.get(position).getIdUser());
        if (reportList.get(position).isReadSituation())
            holder.desc.setText("تم القراءة");
        else
            holder.desc.setText("لم يتم القراءة");

    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }
}
