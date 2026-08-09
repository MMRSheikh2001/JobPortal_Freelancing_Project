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
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.PortfolioRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.PortfolioResponseDTO;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PortfolioAdapter
        extends RecyclerView.Adapter<PortfolioAdapter.ViewHolder> {

    public interface OnPortfolioClickListener {
        void onEdit(PortfolioResponseDTO portfolio);
    }

    private final Context context;
    private final List<PortfolioResponseDTO> portfolioList;
    private final OnPortfolioClickListener listener;
    private final PortfolioRepository repository;

    public PortfolioAdapter(Context context,
                            List<PortfolioResponseDTO> portfolioList,
                            OnPortfolioClickListener listener) {

        this.context = context;
        this.portfolioList = portfolioList;
        this.listener = listener;
        this.repository = new PortfolioRepository(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_portfolio,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        PortfolioResponseDTO portfolio =
                portfolioList.get(position);

        holder.tvTitle.setText(
                portfolio.getTitle());

        holder.tvDescription.setText(
                portfolio.getDescription());

        holder.tvTechnologies.setText(
                portfolio.getTechnologies());

        holder.tvProjectUrl.setText(
                portfolio.getProjectUrl());

        if (portfolio.getFileUrl() == null
                || portfolio.getFileUrl().isEmpty()) {

            holder.tvFile.setText("No Attachment");

        } else {

            holder.tvFile.setText("Attachment Available");

        }

        holder.btnEdit.setOnClickListener(v ->
                listener.onEdit(portfolio));

        holder.btnDelete.setOnClickListener(v ->
                deletePortfolio(position, portfolio));

    }

    @Override
    public int getItemCount() {
        return portfolioList.size();
    }

    private void deletePortfolio(
            int position,
            PortfolioResponseDTO portfolio) {

        new AlertDialog.Builder(context)
                .setTitle("Delete Portfolio")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete",
                        (dialog, which) ->

                                repository.deletePortfolio(
                                        portfolio.getId(),
                                        new Callback<String>() {

                                            @Override
                                            public void onResponse(
                                                    Call<String> call,
                                                    Response<String> response) {

                                                if (response.isSuccessful()) {

                                                    portfolioList.remove(position);

                                                    notifyItemRemoved(position);

                                                    notifyItemRangeChanged(
                                                            position,
                                                            portfolioList.size());

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

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDescription;
        TextView tvTechnologies;
        TextView tvProjectUrl;
        TextView tvFile;

        MaterialButton btnEdit;
        MaterialButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvTechnologies = itemView.findViewById(R.id.tvTechnologies);
            tvProjectUrl = itemView.findViewById(R.id.tvProjectUrl);
            tvFile = itemView.findViewById(R.id.tvFile);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

}