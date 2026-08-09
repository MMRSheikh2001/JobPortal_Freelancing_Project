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
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.ExtracurricularRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ExtracurricularResponseDTO;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExtracurricularAdapter extends RecyclerView.Adapter<ExtracurricularAdapter.ViewHolder> {

    public interface OnExtracurricularClickListener {
        void onEdit(ExtracurricularResponseDTO extracurricular);
    }

    private final Context context;
    private final List<ExtracurricularResponseDTO> extracurricularList;
    private final OnExtracurricularClickListener listener;
    private final ExtracurricularRepository repository;

    public ExtracurricularAdapter(
            Context context,
            List<ExtracurricularResponseDTO> extracurricularList,
            OnExtracurricularClickListener listener) {

        this.context = context;
        this.extracurricularList = extracurricularList;
        this.listener = listener;
        this.repository = new ExtracurricularRepository(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_extracurricular, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        ExtracurricularResponseDTO item = extracurricularList.get(position);

        holder.tvTitle.setText(item.getTitle());

        holder.tvOrganization.setText(
                "Organization: " +
                        (item.getOrganization() == null ? "" : item.getOrganization()));

        holder.tvRole.setText(
                "Role: " +
                        (item.getRole() == null ? "" : item.getRole()));

        holder.tvDescription.setText(
                item.getDescription() == null ? "" : item.getDescription());

        holder.btnEdit.setOnClickListener(v ->
                listener.onEdit(item));

        holder.btnDelete.setOnClickListener(v ->
                deleteExtracurricular(position, item));
    }

    @Override
    public int getItemCount() {
        return extracurricularList.size();
    }

    private void deleteExtracurricular(
            int position,
            ExtracurricularResponseDTO item) {

        new AlertDialog.Builder(context)
                .setTitle("Delete Extracurricular")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete", (dialog, which) ->

                        repository.deleteExtracurricular(
                                item.getId(),
                                new Callback<String>() {

                                    @Override
                                    public void onResponse(
                                            Call<String> call,
                                            Response<String> response) {

                                        if (response.isSuccessful()) {

                                            extracurricularList.remove(position);

                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(
                                                    position,
                                                    extracurricularList.size());

                                        }

                                    }

                                    @Override
                                    public void onFailure(
                                            Call<String> call,
                                            Throwable t) {

                                    }
                                }))

                .setNegativeButton("Cancel", null)
                .show();

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvOrganization;
        TextView tvRole;
        TextView tvDescription;

        MaterialButton btnEdit;
        MaterialButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOrganization = itemView.findViewById(R.id.tvOrganization);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvDescription = itemView.findViewById(R.id.tvDescription);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}