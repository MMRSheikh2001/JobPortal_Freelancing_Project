package com.MMRSheikh2001.workbridgeandroid;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.MMRSheikh2001.workbridgeandroid.repository.AuthRepository;
import com.MMRSheikh2001.workbridgeandroid.request.LoginRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;

    private MaterialButton btnLogin;

    private TextView tvForgotPassword;

    private TextView tvRegister;

    private AuthRepository authRepository;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

        btnLogin.setOnClickListener(v -> login());
        tvForgotPassword.setOnClickListener(v -> goToForgotPassword());

        tvRegister.setOnClickListener(v -> goToRegister());

    }

    private void init() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegister);

        authRepository = new AuthRepository(this);
        sessionManager = new SessionManager(this);
    }

    private void login() {
        String email = String.valueOf(etEmail.getText()).trim();
        String password = String.valueOf(etPassword.getText()).trim();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email Required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password Required");
            return;
        }
        btnLogin.setEnabled(false);

        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail(email);
        request.setPassword(password);
        authRepository.login(request, new Callback<LoginResponseDTO>() {
            @Override
            public void onResponse(Call<LoginResponseDTO> call, Response<LoginResponseDTO> response) {
                btnLogin.setEnabled(true);
                if (!response.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Invalid Email or Password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                LoginResponseDTO login = response.body();
                if (login == null) {
                    Toast.makeText(LoginActivity.this, "Login Failed",
                            Toast.LENGTH_SHORT).show();
                    return;
                }


                //save token
                sessionManager.saveToken(login.getToken());

                //Save user
                sessionManager.saveUser(login);

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();


            }

            @Override
            public void onFailure(Call<LoginResponseDTO> call, Throwable t) {

                btnLogin.setEnabled(true);

                Toast.makeText(LoginActivity.this,
                        "Unable to connect to server.",
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void goToForgotPassword() {

        Intent intent = new Intent(
                LoginActivity.this,
                ForgotPasswordActivity.class);

        startActivity(intent);
    }

    private void goToRegister() {

        Intent intent = new Intent(
                LoginActivity.this,
                RegisterActivity.class);

        startActivity(intent);
    }


}