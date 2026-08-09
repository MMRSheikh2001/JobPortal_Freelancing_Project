package com.MMRSheikh2001.workbridgeandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.MMRSheikh2001.workbridgeandroid.R;
import com.MMRSheikh2001.workbridgeandroid.response.JobResponseDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.List;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder> {

    private final Context context;
    private final List<JobResponseDTO> jobList;

    public interface OnJobClickListener {
        void onJobClick(JobResponseDTO job);
    }
    private final OnJobClickListener listener;

    public JobAdapter(Context context,
                      List<JobResponseDTO> jobList,
                      OnJobClickListener listener) {

        this.context = context;
        this.jobList = jobList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_job, parent, false);

        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder,
                                 int position) {

        JobResponseDTO job = jobList.get(position);

        holder.tvJobTitle.setText(job.getTitle());

        holder.tvCompanyName.setText(job.getCompanyName());

        holder.tvLocation.setText(
                job.getLocationPoliceStationName() + ", "
                        + job.getLocationDistrictName());

        holder.chipEmployment.setText(
                job.getEmploymentType().name());

        holder.chipWorkplace.setText(
                job.getWorkPlaceType().name());

        if (Boolean.TRUE.equals(job.getIsNegotiable())) {

            holder.tvSalary.setText("Salary : Negotiable");

        } else {

            holder.tvSalary.setText(
                    "Salary : "
                            + job.getSalaryMin()
                            + " - "
                            + job.getSalaryMax());

        }

        holder.tvDeadline.setText(
                "Deadline : "
                        + job.getApplicationDeadline());

        holder.itemView.setOnClickListener(v ->
                listener.onJobClick(job));

        holder.btnViewDetails.setOnClickListener(v ->
                listener.onJobClick(job));

    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    static class JobViewHolder extends RecyclerView.ViewHolder {

        TextView tvJobTitle;
        TextView tvCompanyName;
        TextView tvLocation;
        TextView tvSalary;
        TextView tvDeadline;

        Chip chipEmployment;
        Chip chipWorkplace;

        MaterialButton btnViewDetails;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);

            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvCompanyName = itemView.findViewById(R.id.tvCompanyName);
            tvLocation = itemView.findViewById(R.id.tvLocation);

            tvSalary = itemView.findViewById(R.id.tvSalary);
            tvDeadline = itemView.findViewById(R.id.tvDeadline);

            chipEmployment = itemView.findViewById(R.id.chipEmployment);
            chipWorkplace = itemView.findViewById(R.id.chipWorkplace);

            btnViewDetails = itemView.findViewById(R.id.btnViewDetails);
        }

    }



}