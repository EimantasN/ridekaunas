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
                    .url("http://api.pertrauktiestaskas.lt/api/User/Login")
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

    //TODO reikia grazinti status kodus, nes dabar register nepavyko nes jau toks yra
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

    public static boolean updatePersonalInfo()
    {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\r\n  \"id\": 0,\r\n  \"name\": \"A\",\r\n  \"lastName\": \"B\",\r\n  \"imgUrl\": \"gOOGLEurL\",\r\n  \"gmail\": \"noreika.eimantas2@gmail.com\",\r\n  \"lastUsed\": \"2018-10-08T00:00:00\",\r\n  \"created\": \"0001-01-01T00:00:00\",\r\n  \"history\": null,\r\n  \"cards\": null,\r\n  \"favoriteRoutes\": null\r\n}");
        Request request = new Request.Builder()
                .url("http://api.pertrauktiestaskas.lt/api/User/AddPersonalInfo")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();
            return Boolean.parseBoolean(response.body().source().readString(Charset.forName("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
