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
import com.MMRSheikh2001.workbridgeandroid.cvinformations.repository.UserSkillRepository;
import com.MMRSheikh2001.workbridgeandroid.cvinformations.response.UserSkillResponseDTO;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSkillAdapter
        extends RecyclerView.Adapter<UserSkillAdapter.ViewHolder> {

    public interface OnUserSkillClickListener {
        void onEdit(UserSkillResponseDTO userSkill);
    }

    private final Context context;
    private final List<UserSkillResponseDTO> skillList;
    private final OnUserSkillClickListener listener;
    private final UserSkillRepository repository;

    public UserSkillAdapter(
            Context context,
            List<UserSkillResponseDTO> skillList,
            OnUserSkillClickListener listener) {

        this.context = context;
        this.skillList = skillList;
        this.listener = listener;
        this.repository = new UserSkillRepository(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(
                        R.layout.item_user_skill,
                        parent,
                        false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        UserSkillResponseDTO userSkill = skillList.get(position);

        holder.tvSkillName.setText(userSkill.getSkillName());

        holder.tvCategoryName.setText(userSkill.getCategoryName());

        holder.tvProficiency.setText(
                userSkill.getProficiencyLevel().name());

        if (userSkill.getYearsOfExperience() == null) {

            holder.tvExperience.setText("Experience not specified");

        } else {

            int years = userSkill.getYearsOfExperience();

            if (years == 1) {
                holder.tvExperience.setText("1 Year Experience");
            } else {
                holder.tvExperience.setText(
                        years + " Years Experience");
            }

        }

        holder.btnEdit.setOnClickListener(v ->
                listener.onEdit(userSkill));

        holder.btnDelete.setOnClickListener(v ->
                deleteUserSkill(position, userSkill));

    }

    @Override
    public int getItemCount() {
        return skillList.size();
    }

    private void deleteUserSkill(
            int position,
            UserSkillResponseDTO userSkill) {

        new AlertDialog.Builder(context)
                .setTitle("Delete Skill")
                .setMessage("Are you sure you want to delete this skill?")
                .setPositiveButton("Delete",
                        (dialog, which) ->

                                repository.deleteUserSkill(
                                        userSkill.getId(),
                                        new Callback<String>() {

                                            @Override
                                            public void onResponse(
                                                    Call<String> call,
                                                    Response<String> response) {

                                                if (response.isSuccessful()) {

                                                    skillList.remove(position);

                                                    notifyItemRemoved(position);

                                                    notifyItemRangeChanged(
                                                            position,
                                                            skillList.size());

                                                    Toast.makeText(
                                                                    context,
                                                                    "Skill deleted",
                                                                    Toast.LENGTH_SHORT)
                                                            .show();

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

        TextView tvSkillName;
        TextView tvCategoryName;
        TextView tvProficiency;
        TextView tvExperience;

        MaterialButton btnEdit;
        MaterialButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSkillName =
                    itemView.findViewById(R.id.tvSkillName);

            tvCategoryName =
                    itemView.findViewById(R.id.tvCategoryName);

            tvProficiency =
                    itemView.findViewById(R.id.tvProficiency);

            tvExperience =
                    itemView.findViewById(R.id.tvExperience);

            btnEdit =
                    itemView.findViewById(R.id.btnEdit);

            btnDelete =
                    itemView.findViewById(R.id.btnDelete);
        }

    }

}