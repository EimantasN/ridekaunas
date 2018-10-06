package com.example.pertrauktiestaskas.trulify;

import com.example.pertrauktiestaskas.models.RootObject;
import com.example.pertrauktiestaskas.models.RouteSegment;
import com.example.pertrauktiestaskas.models.TrafiListModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BusApiHandler {
    private static GsonBuilder builder;
    private static Gson jsonConverter;

    public static RootObject GetRouteData(String start_lat, String start_lng, String end_lat, String end_lng) {
        final String apiKey = "cb99332fe299a1e5a5b8d434c03b24f9";
        final String url = String.format("http://api-ext.trafi.com/routes?start_lat=%s&start_lng=%s&end_lat=%s&end_lng=%s&is_arrival=false&api_key=%s",
                start_lat, start_lng, end_lat, end_lng, apiKey);

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Cache-Control", "no-cache")
                    .build();

            Response resp = client.newCall(request).execute();
            if (null != resp.body()) {
                String json = resp.body().string();
                builder = new GsonBuilder();
                jsonConverter = builder.create();
                return jsonConverter.fromJson(json, RootObject.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
return null;
    }

    public static List<TrafiListModel> FormatRoutesToListModel(RootObject routes) {
        List<TrafiListModel> data = new ArrayList<>();
        if(routes.Routes != null){
            for(RouteSegment s : routes.Routes.get(0).RouteSegments) {
                TrafiListModel tmp = new TrafiListModel();
                    tmp.Image = s.IconUrl;
                    tmp.StartTime = s.StartPoint.Time;
                    tmp.EndTime = s.EndPoint.Time;
                    tmp.EndStreet = s.EndPoint.Name;
                    tmp.NextStopDistance = s.DistanceMeters + " m";
                    tmp.NextStopTime = s.DurationMinutes + " min";
                    tmp.ImageBottomDistance = Integer.toString(s.WalkDistanceMeters);
                    data.add(tmp);
            }
            return data;
            }
            return null;
        }
}

