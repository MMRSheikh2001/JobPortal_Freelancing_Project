package com.MMRSheikh2001.workbridgeandroid.api;

import android.content.Context;
import android.content.Intent;

import com.MMRSheikh2001.workbridgeandroid.LoginActivity;
import com.MMRSheikh2001.workbridgeandroid.session.SessionManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class UnauthorizedInterceptor implements Interceptor {

    private final Context context;
    private static boolean isRedirecting = false;

    public UnauthorizedInterceptor(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        Response response = chain.proceed(request);

        if (response.code() == 401 && !isRedirecting) {

            isRedirecting = true;

            // Clear saved login
            SessionManager sessionManager = new SessionManager(context);
            sessionManager.logout();

            // Go to LoginActivity
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP
            );

            context.startActivity(intent);
        }

        return response;
    }
}