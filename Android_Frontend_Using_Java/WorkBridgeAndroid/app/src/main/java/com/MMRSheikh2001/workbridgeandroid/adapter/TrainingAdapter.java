package com.MMRSheikh2001.workbridgeandroid.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.MMRSheikh2001.workbridgeandroid.R;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.TrainingRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.TrainingResponseDTO;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrainingAdapter
        extends RecyclerView.Adapter<TrainingAdapter.ViewHolder> {

    public interface OnTrainingClickListener {
        void onEdit(TrainingResponseDTO training);
    }

    private final Context context;
    private final List<TrainingResponseDTO> trainingList;
    private final OnTrainingClickListener listener;
    private final TrainingRepository repository;

    public TrainingAdapter(Context context,
                           List<TrainingResponseDTO> trainingList,
                           OnTrainingClickListener listener) {

        this.context = context;
        this.trainingList = trainingList;
        this.listener = listener;
        this.repository = new TrainingRepository(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_training, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 int position) {

        TrainingResponseDTO training = trainingList.get(position);

        holder.tvTrainingName.setText(training.getName());
        holder.tvInstitution.setText(training.getInstitution());

        holder.tvTrainingType.setText(
                training.getTrainingType() != null
                        ? training.getTrainingType().name()
                        : "");

        String duration = "";

        if (training.getStartDate() != null) {
            duration += training.getStartDate();
        }

        duration += " - ";

        if (training.getCompleted() != null &&
                training.getCompleted()) {

            if (training.getEndDate() != null) {
                duration += training.getEndDate();
            }

            holder.tvCompleted.setText("Completed");

        } else {

            duration += "Present";
            holder.tvCompleted.setText("Ongoing");
        }

        holder.tvDuration.setText(duration);

        if (training.getCertificateId() == null ||
                training.getCertificateId().isEmpty()) {

            holder.tvCertificateId.setVisibility(View.GONE);

        } else {

            holder.tvCertificateId.setVisibility(View.VISIBLE);
            holder.tvCertificateId.setText(
                    "Certificate ID : " +
                            training.getCertificateId());
        }

        holder.btnEdit.setOnClickListener(v ->
                listener.onEdit(training));

        holder.btnDelete.setOnClickListener(v ->
                deleteTraining(position, training));

    }

    @Override
    public int getItemCount() {
        return trainingList.size();
    }

    private void deleteTraining(int position,
                                TrainingResponseDTO training) {

        new AlertDialog.Builder(context)
                .setTitle("Delete Training")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete", (dialog, which) ->

                        repository.deleteTraining(
                                training.getId(),
                                new Callback<String>() {

                                    @Override
                                    public void onResponse(
                                            Call<String> call,
                                            Response<String> response) {

                                        if (response.isSuccessful()) {

                                            trainingList.remove(position);

                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(
                                                    position,
                                                    trainingList.size());

                                            Toast.makeText(
                                                    context,
                                                    "Training deleted",
                                                    Toast.LENGTH_SHORT).show();

                                        } else {

                                            Toast.makeText(
                                                    context,
                                                    "Delete failed",
                                                    Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onFailure(
                                            Call<String> call,
                                            Throwable t) {

                                        Toast.makeText(
                                                context,
                                                t.getMessage(),
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }))

                .setNegativeButton("Cancel", null)
                .show();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTrainingName;
        TextView tvInstitution;
        TextView tvTrainingType;
        TextView tvDuration;
        TextView tvCompleted;
        TextView tvCertificateId;

        MaterialButton btnEdit;
        MaterialButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTrainingName = itemView.findViewById(R.id.tvTrainingName);
            tvInstitution = itemView.findViewById(R.id.tvInstitution);
            tvTrainingType = itemView.findViewById(R.id.tvTrainingType);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvCompleted = itemView.findViewById(R.id.tvCompleted);
            tvCertificateId = itemView.findViewById(R.id.tvCertificateId);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}