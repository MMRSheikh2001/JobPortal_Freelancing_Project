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
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.EducationRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.EducationResponseDTO;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.ViewHolder> {

    public interface OnEducationClickListener {
        void onEdit(EducationResponseDTO education);
        void onDelete();

    }

    private final Context context;
    private final List<EducationResponseDTO> educationList;
    private final OnEducationClickListener listener;
    private final EducationRepository repository;

    public EducationAdapter(Context context,
                            List<EducationResponseDTO> educationList,
                            OnEducationClickListener listener) {

        this.context = context;
        this.educationList = educationList;
        this.listener = listener;
        this.repository = new EducationRepository(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_education, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 int position) {

        EducationResponseDTO education = educationList.get(position);

        holder.tvEducationLevel.setText(
                education.getEducationLevel().name());

        holder.tvInstitution.setText(
                education.getInstitution());

        holder.tvBoard.setText(
                education.getBoard());

        holder.tvField.setText(
                education.getFieldOfStudy());

        // Result
        String result;

        if (education.getResultType() != null) {

            switch (education.getResultType()) {

                case CGPA:
                case GPA:

                    result = education.getResultType().name()
                            + " "
                            + education.getResult()
                            + " / "
                            + education.getOutOf();

                    break;

                default:

                    result = education.getResultType().name()
                            + " "
                            + education.getGradeOrDivision();

                    break;
            }

        } else {

            result = "";
        }

        holder.tvResult.setText(result);

        // Duration
        String duration = "";

        if (education.getStartDate() != null) {
            duration += education.getStartDate();
        }

        duration += " - ";

        if (Boolean.TRUE.equals(education.getCurrentlyStudying())) {
            duration += "Present";
            holder.tvCurrent.setVisibility(View.VISIBLE);
        } else {

            holder.tvCurrent.setVisibility(View.GONE);

            if (education.getEndDate() != null) {
                duration += education.getEndDate();
            }
        }

        holder.tvDuration.setText(duration);

        holder.btnEdit.setOnClickListener(v ->
                listener.onEdit(education));

        holder.btnDelete.setOnClickListener(v ->
                deleteEducation(position, education));
    }

    @Override
    public int getItemCount() {
        return educationList.size();
    }

    private void deleteEducation(int position,
                                 EducationResponseDTO education) {

        new AlertDialog.Builder(context)
                .setTitle("Delete Education")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete", (dialog, which) ->

                        repository.deleteEducation(
                                education.getId(),
                                new Callback<String>() {

                                    @Override
                                    public void onResponse(Call<String> call,
                                                           Response<String> response) {

                                        if (response.isSuccessful()) {

                                            listener.onDelete();

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

        TextView tvEducationLevel;
        TextView tvInstitution;
        TextView tvBoard;
        TextView tvField;
        TextView tvResult;
        TextView tvDuration;
        TextView tvCurrent;

        MaterialButton btnEdit;
        MaterialButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEducationLevel = itemView.findViewById(R.id.tvEducationLevel);
            tvInstitution = itemView.findViewById(R.id.tvInstitution);
            tvBoard = itemView.findViewById(R.id.tvBoard);
            tvField = itemView.findViewById(R.id.tvField);
            tvResult = itemView.findViewById(R.id.tvResult);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvCurrent = itemView.findViewById(R.id.tvCurrent);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }


}