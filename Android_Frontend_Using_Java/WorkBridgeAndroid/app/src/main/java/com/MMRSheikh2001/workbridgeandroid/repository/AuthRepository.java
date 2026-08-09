package com.MMRSheikh2001.workbridgeandroid.repository;

import android.content.Context;

import com.MMRSheikh2001.workbridgeandroid.api.ApiClient;
import com.MMRSheikh2001.workbridgeandroid.api.ApiService;
import com.MMRSheikh2001.workbridgeandroid.request.ForgotPasswordRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.request.LoginRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.request.UserRequestDTO;
import com.MMRSheikh2001.workbridgeandroid.response.LoginResponseDTO;
import com.MMRSheikh2001.workbridgeandroid.response.UserDashboardDTO;
import com.MMRSheikh2001.workbridgeandroid.response.UserResponseDTO;

import retrofit2.Call;
import retrofit2.Callback;

public class AuthRepository {


    private final ApiService apiService;

    public AuthRepository(Context context) {
        apiService = ApiClient.getClient(context);
    }

    public void login(LoginRequestDTO request,
                      Callback<LoginResponseDTO> callback) {

        Call<LoginResponseDTO> call = apiService.login(request);

        call.enqueue(callback);
    }

    public void register(UserRequestDTO requestDTO, Callback<UserResponseDTO> callback) {
        Call<UserResponseDTO> call = apiService.register(requestDTO);
        call.enqueue(callback);
    }

    public void forgotPassword(
            ForgotPasswordRequestDTO requestDTO,
            Callback<String> callback) {

        apiService.forgotPassword(requestDTO).enqueue(callback);
    }

    public void getUserDashboard(
            Long userId,
            Callback<UserDashboardDTO> callback
    ) {

        Call<UserDashboardDTO> call =
                apiService.getUserDashboard(userId);

        call.enqueue(callback);

    }


}
