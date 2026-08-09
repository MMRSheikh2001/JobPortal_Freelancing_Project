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
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.UserLanguageRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.UserLanguageResponseDTO;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLanguageAdapter
        extends RecyclerView.Adapter<UserLanguageAdapter.ViewHolder> {

    public interface OnUserLanguageClickListener {
        void onEdit(UserLanguageResponseDTO userLanguage);
    }

    private final Context context;
    private final List<UserLanguageResponseDTO> languageList;
    private final OnUserLanguageClickListener listener;
    private final UserLanguageRepository repository;

    public UserLanguageAdapter(
            Context context,
            List<UserLanguageResponseDTO> languageList,
            OnUserLanguageClickListener listener) {

        this.context = context;
        this.languageList = languageList;
        this.listener = listener;
        this.repository = new UserLanguageRepository(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_user_language,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        UserLanguageResponseDTO userLanguage =
                languageList.get(position);

        holder.tvLanguageName.setText(
                userLanguage.getLanguageName());

        holder.tvProficiency.setText(
                userLanguage.getProficiency().name());

        holder.btnEdit.setOnClickListener(v ->
                listener.onEdit(userLanguage));

        holder.btnDelete.setOnClickListener(v ->
                deleteUserLanguage(position, userLanguage));

    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }

    private void deleteUserLanguage(
            int position,
            UserLanguageResponseDTO userLanguage) {

        new AlertDialog.Builder(context)
                .setTitle("Delete Language")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete",
                        (dialog, which) ->

                                repository.deleteUserLanguage(
                                        userLanguage.getId(),
                                        new Callback<String>() {

                                            @Override
                                            public void onResponse(
                                                    Call<String> call,
                                                    Response<String> response) {

                                                if (response.isSuccessful()) {

                                                    languageList.remove(position);

                                                    notifyItemRemoved(position);

                                                    notifyItemRangeChanged(
                                                            position,
                                                            languageList.size());

                                                } else {

                                                    Toast.makeText(
                                                                    context,
                                                                    "Delete failed",
                                                                    Toast.LENGTH_SHORT)
                                                            .show();

                                                }

                                            }

                                            @Override
                                            public void onFailure(
                                                    Call<String> call,
                                                    Throwable t) {

                                                Toast.makeText(
                                                                context,
                                                                t.getMessage(),
                                                                Toast.LENGTH_SHORT)
                                                        .show();

                                            }

                                        }))

                .setNegativeButton("Cancel", null)
                .show();

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvLanguageName;
        TextView tvProficiency;

        MaterialButton btnEdit;
        MaterialButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLanguageName =
                    itemView.findViewById(R.id.tvLanguageName);

            tvProficiency =
                    itemView.findViewById(R.id.tvProficiency);

            btnEdit =
                    itemView.findViewById(R.id.btnEdit);

            btnDelete =
                    itemView.findViewById(R.id.btnDelete);
        }

    }

}