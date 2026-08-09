package com.MMRSheikh2001.workbridgeandroid;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.MMRSheikh2001.workbridgeandroid.enums.UserRole;
import com.MMRSheikh2001.workbridgeandroid.repository.AuthRepository;
import com.MMRSheikh2001.workbridgeandroid.request.UserRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.response.UserResponseDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


    private TextInputEditText etFullName;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;

    private MaterialButton btnRegister;

    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        init();

        btnRegister.setOnClickListener(v -> register());

    }

    private void init() {

        etFullName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnRegister = findViewById(R.id.btnRegister);

        authRepository = new AuthRepository(this);
    }


    private void register() {

        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (fullName.isEmpty()) {
            etFullName.setError("Required");
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Required");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Required");
            return;
        }

        if (!password.equals(confirmPassword)) {

            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        btnRegister.setEnabled(false);

        UserRequestDTO dto = new UserRequestDTO();

        dto.setFullName(fullName);
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setRole(UserRole.USER);

        authRepository.register(dto, new Callback<UserResponseDTO>() {

            @Override
            public void onResponse(Call<UserResponseDTO> call,
                                   Response<UserResponseDTO> response) {

                btnRegister.setEnabled(true);

                if (!response.isSuccessful() || response.body() == null) {

                    Toast.makeText(
                            RegisterActivity.this,
                            "Registration failed",
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                }

                Toast.makeText(
                        RegisterActivity.this,
                        "Registration successful",
                        Toast.LENGTH_LONG
                ).show();

                finish();
            }

            @Override
            public void onFailure(Call<UserResponseDTO> call,
                                  Throwable t) {

                btnRegister.setEnabled(true);

                Toast.makeText(RegisterActivity.this, "", Toast.LENGTH_SHORT).show();
                Toast.makeText(
                        RegisterActivity.this,
                        "Unable to connect to server",
                        Toast.LENGTH_SHORT
                ).show();

                t.printStackTrace();
            }
        });

    }


}