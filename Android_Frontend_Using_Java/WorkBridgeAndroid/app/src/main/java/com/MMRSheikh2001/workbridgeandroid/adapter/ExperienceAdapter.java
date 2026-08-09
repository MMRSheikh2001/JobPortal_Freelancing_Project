package com.MMRSheikh2001.workbridgeandroid.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.MMRSheikh2001.workbridgeandroid.R;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.ExperienceRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ExperienceResponseDTO;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExperienceAdapter extends RecyclerView.Adapter<ExperienceAdapter.ViewHolder> {

    public interface OnExperienceClickListener {
        void onEdit(ExperienceResponseDTO experience);
    }

    private final Context context;
    private final List<ExperienceResponseDTO> experienceList;
    private final OnExperienceClickListener listener;
    private final ExperienceRepository repository;

    public ExperienceAdapter(Context context,
                             List<ExperienceResponseDTO> experienceList,
                             OnExperienceClickListener listener) {

        this.context = context;
        this.experienceList = experienceList;
        this.listener = listener;
        this.repository = new ExperienceRepository(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_experience, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 int position) {

        ExperienceResponseDTO experience = experienceList.get(position);

        holder.tvCompany.setText(experience.getCompanyName());
        holder.tvPosition.setText(experience.getPosition());

        if (experience.getEmploymentType() != null) {
            holder.tvEmploymentType.setText(
                    experience.getEmploymentType().name());
        } else {
            holder.tvEmploymentType.setText("");
        }

        String duration = "";

        if (experience.getStartDate() != null) {
            duration += experience.getStartDate();
        }

        duration += " - ";

        if (Boolean.TRUE.equals(experience.getCurrentlyWorking())) {

            duration += "Present";
            holder.tvCurrent.setVisibility(View.VISIBLE);

        } else {

            holder.tvCurrent.setVisibility(View.GONE);

            if (experience.getEndDate() != null) {
                duration += experience.getEndDate();
            }

        }

        holder.tvDuration.setText(duration);

        holder.tvResponsibilities.setText(
                experience.getResponsibilities());

        holder.tvAchievements.setText(
                experience.getAchievements());

        holder.btnEdit.setOnClickListener(v ->
                listener.onEdit(experience));

        holder.btnDelete.setOnClickListener(v ->
                deleteExperience(position, experience));
    }

    @Override
    public int getItemCount() {
        return experienceList.size();
    }

    private void deleteExperience(int position,
                                  ExperienceResponseDTO experience) {

        new AlertDialog.Builder(context)
                .setTitle("Delete Experience")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete", (dialog, which) ->

                        repository.deleteExperience(
                                experience.getId(),
                                new Callback<String>() {

                                    @Override
                                    public void onResponse(Call<String> call,
                                                           Response<String> response) {

                                        if (response.isSuccessful()) {

                                            experienceList.remove(position);

                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(
                                                    position,
                                                    experienceList.size());

                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<String> call,
                                                          Throwable t) {

                                    }
                                })

                )
                .setNegativeButton("Cancel", null)
                .show();

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvCompany;
        TextView tvPosition;
        TextView tvEmploymentType;
        TextView tvDuration;
        TextView tvCurrent;
        TextView tvResponsibilities;
        TextView tvAchievements;

        MaterialButton btnEdit;
        MaterialButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCompany = itemView.findViewById(R.id.tvCompany);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            tvEmploymentType = itemView.findViewById(R.id.tvEmploymentType);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvCurrent = itemView.findViewById(R.id.tvCurrent);
            tvResponsibilities = itemView.findViewById(R.id.tvResponsibilities);
            tvAchievements = itemView.findViewById(R.id.tvAchievements);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

}