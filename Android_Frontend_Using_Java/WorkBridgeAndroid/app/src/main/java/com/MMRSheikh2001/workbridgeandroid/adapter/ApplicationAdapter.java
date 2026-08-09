package com.MMRSheikh2001.workbridgeandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.MMRSheikh2001.workbridgeandroid.ApplicationDetailsActivity;
import com.MMRSheikh2001.workbridgeandroid.MyApplications;
import com.MMRSheikh2001.workbridgeandroid.R;
import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.response.JobApplicationResponseDTO;
import com.bumptech.glide.Glide;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ViewHolder> {

    private final Context context;
    private final List<JobApplicationResponseDTO> applicationList;



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_application, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        JobApplicationResponseDTO application =
                applicationList.get(position);

        holder.tvJobTitle.setText(application.getJobTitle());

        holder.tvCompany.setText(application.getCompanyName());

        holder.tvStatus.setText(
                "Status: " + application.getStatus());

        holder.tvAppliedDate.setText(
                "Applied: " + application.getAppliedAt());

        if (application.getAiFinalScore() != null) {

            holder.tvAIScore.setVisibility(View.VISIBLE);

            holder.tvAIScore.setText(
                    "AI Score: " + application.getAiFinalScore());

        } else {

            holder.tvAIScore.setVisibility(View.GONE);
        }

        if (application.getCompanyLogo() != null
                && !application.getCompanyLogo().isEmpty()) {

            Glide.with(context)
                    .load(ApiClient.getCompanyLogoUrl(application.getCompanyLogo()))
                    .placeholder(R.drawable.ic_company)
                    .error(R.drawable.ic_company)
                    .into(holder.imgCompany);

        } else {

            holder.imgCompany.setImageResource(R.drawable.ic_company);
        }



            holder.itemView.setOnClickListener(v -> {

                Intent intent = new Intent(this.context, ApplicationDetailsActivity.class);
                intent.putExtra("APPLICATION_ID", application.getId());
                context.startActivity(intent);

            });

    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgCompany;

        TextView tvJobTitle;
        TextView tvCompany;
        TextView tvStatus;
        TextView tvAppliedDate;
        TextView tvAIScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCompany = itemView.findViewById(R.id.imgCompany);

            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvAppliedDate = itemView.findViewById(R.id.tvAppliedDate);
            tvAIScore = itemView.findViewById(R.id.tvAIScore);
        }
    }
}