package com.example.pertrauktiestaskas.API;

import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class api_requests {

    public static String currentUserGmail = "";
    public static int UserId = -1;

    public static boolean userActive = false;

    public static  int Login(String gmail)
    {
        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "\"noreika.eimantas@gmail.com\"");
            Request request = new Request.Builder()
                    .url("http://api.pertrauktiestaskas.lt/api/User/")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                UserId = Integer.parseInt(response.body().source().readString(Charset.forName("UTF-8")));

                if(UserId > 0)
                    userActive = true;

                Thread.sleep(2500);

                return UserId;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        catch (Exception e)
        {
            Log.d("api_requests", e.getMessage());
        }

        return -1;
    }

    public static boolean Register(String gmail)
    {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "\"noreika.eimantas@gmail.com\"");
        Request request = new Request.Builder()
                .url("http://api.pertrauktiestaskas.lt/api/User/Register")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .addHeader("Postman-Token", "997222f0-090c-4450-8ae6-acf17bd347ed")
                .build();

        try {
            Response response = client.newCall(request).execute();
            userActive = Boolean.parseBoolean(response.body().source().readString(Charset.forName("UTF-8")));
            return userActive;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
