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
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.ReferenceRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.ReferenceResponseDTO;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReferenceAdapter extends RecyclerView.Adapter<ReferenceAdapter.ViewHolder> {

    public interface OnReferenceClickListener {
        void onEdit(ReferenceResponseDTO reference);
    }

    private final Context context;
    private final List<ReferenceResponseDTO> referenceList;
    private final OnReferenceClickListener listener;
    private final ReferenceRepository repository;

    public ReferenceAdapter(Context context,
                            List<ReferenceResponseDTO> referenceList,
                            OnReferenceClickListener listener) {

        this.context = context;
        this.referenceList = referenceList;
        this.listener = listener;
        this.repository = new ReferenceRepository(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_reference, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,
                                 int position) {

        ReferenceResponseDTO reference = referenceList.get(position);

        holder.tvName.setText(reference.getName());
        holder.tvDesignation.setText(reference.getDesignation());
        holder.tvOrganization.setText(reference.getOrganization());
        holder.tvRelation.setText("Relation: " + reference.getRelation());
        holder.tvPhone.setText(reference.getPhone());
        holder.tvEmail.setText(reference.getEmail());
        holder.tvAddress.setText(reference.getAddress());

        holder.btnEdit.setOnClickListener(v ->
                listener.onEdit(reference));

        holder.btnDelete.setOnClickListener(v ->
                deleteReference(position, reference));
    }

    @Override
    public int getItemCount() {
        return referenceList.size();
    }

    private void deleteReference(int position,
                                 ReferenceResponseDTO reference) {

        new AlertDialog.Builder(context)
                .setTitle("Delete Reference")
                .setMessage("Are you sure you want to delete this reference?")
                .setPositiveButton("Delete", (dialog, which) ->

                        repository.deleteReference(
                                reference.getId(),
                                new Callback<String>() {

                                    @Override
                                    public void onResponse(Call<String> call,
                                                           Response<String> response) {

                                        if (response.isSuccessful()) {

                                            referenceList.remove(position);

                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(
                                                    position,
                                                    referenceList.size());
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

        TextView tvName;
        TextView tvDesignation;
        TextView tvOrganization;
        TextView tvRelation;
        TextView tvPhone;
        TextView tvEmail;
        TextView tvAddress;

        MaterialButton btnEdit;
        MaterialButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvDesignation = itemView.findViewById(R.id.tvDesignation);
            tvOrganization = itemView.findViewById(R.id.tvOrganization);
            tvRelation = itemView.findViewById(R.id.tvRelation);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvAddress = itemView.findViewById(R.id.tvAddress);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}