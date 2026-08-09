package com.MMRSheikh2001.workbridgeandroid.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.Strictness;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {


    // Empubliculator
    public static final String BASE_URL = "http://10.0.2.2:8090/";

    // Real Device
    //     public static final String BASE_URL = "http://192.168.88.245:8090/";

    //Home
 //         public static final String BASE_URL = "http://192.168.0.105:8090/";

    // Real Device via USB (After running adb reverse)
    //  public static final String BASE_URL = "http://127.0.0.1:8090/";
  //        public static final String BASE_URL = "http://localhost:8090/";
// Note: You can also use "http://localhost:8090/"
    //commmand
    //     & "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" -d reverse tcp:8090 tcp:8090


    private static Retrofit retrofit;

    public static ApiService getClient(Context context) {

        if (retrofit == null) {

            HttpLoggingInterceptor logging =
                    new HttpLoggingInterceptor();

            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(new AuthInterceptor(context))
                    .addInterceptor(new UnauthorizedInterceptor(context))
                    .addInterceptor(logging)
                    .build();

            Gson gson = new GsonBuilder()
                    .setStrictness(Strictness.LENIENT)

                     .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context1) ->
                            LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE)
                    ) .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context1) ->
                            src == null ? null : new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE))
                    )

                    .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, typeOfT, context1) ->
                            LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    )
                    .registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context1) ->
                            src == null ? null : new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                    )
                    .create();


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit.create(ApiService.class);
    }

    public static String getCompanyLogoUrl(String fileName) {
        return BASE_URL + "api/files/companyprofiles/" + fileName;
    }

    public static String getUserProfileImage(String fileName) {
        return BASE_URL + "api/files/userprofiles/" + fileName;
    }

    public static String getResumeFile(String fileName) {
        return BASE_URL + "api/files/resumes/" + fileName;
    }

    public static String getPortfolioFile(String fileName) {
        return BASE_URL + "api/files/portfolios/" + fileName;
    }

    public static String getTrainingFile(String fileName) {
        return BASE_URL + "api/files/trainings/" + fileName;
    }

}