package com.MMRSheikh2001.workbridgeandroid;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.MMRSheikh2001.workbridgeandroid.repository.AuthRepository;
import com.MMRSheikh2001.workbridgeandroid.request.ForgotPasswordRequestDTO;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {


    private TextInputEditText etEmail;
    private MaterialButton btnSend;

    private AuthRepository authRepository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forgot_password);

        init();

        btnSend.setOnClickListener(v -> forgotPassword());

    }


    private void init() {

        etEmail = findViewById(R.id.etEmail);
        btnSend = findViewById(R.id.btnSend);

        authRepository = new AuthRepository(this);
    }

    private void forgotPassword() {

        String email = etEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email");
            etEmail.requestFocus();
            return;
        }

        ForgotPasswordRequestDTO request = new ForgotPasswordRequestDTO();
        request.setEmail(email);

        btnSend.setEnabled(false);

        authRepository.forgotPassword(
                request,
                new Callback<String>() {

                    @Override
                    public void onResponse(Call<String> call,
                                           Response<String> response) {

                        btnSend.setEnabled(true);

                        if (!response.isSuccessful()) {

                            Toast.makeText(
                                    ForgotPasswordActivity.this,
                                    "Failed to send reset email",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        String message = response.body();

                        Toast.makeText(
                                ForgotPasswordActivity.this,
                                message == null
                                        ? "Password reset email sent."
                                        : message,
                                Toast.LENGTH_LONG
                        ).show();

                        finish();
                    }

                    @Override
                    public void onFailure(Call<String> call,
                                          Throwable t) {

                        btnSend.setEnabled(true);

                        Toast.makeText(
                                ForgotPasswordActivity.this,
                                "Unable to connect to server",
                                Toast.LENGTH_SHORT
                        ).show();

                        t.printStackTrace();
                    }
                });
    }


}