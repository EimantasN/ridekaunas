package com.example.pertrauktiestaskas.trulify;

import com.example.pertrauktiestaskas.models.RootObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class BusApiHandler {
    private static GsonBuilder builder;
    private static Gson jsonConverter;

    public static RootObject GetRouteData(String start_lat, String start_lng, String end_lat, String end_lng) {
        final String apiKey = "cb99332fe299a1e5a5b8d434c03b24f9";
        final String url = String.format("http://api-ext.trafi.com/routes?start_lat=%s&start_lng=%s&end_lat=%s&end_lng=%s&is_arrival=false&api_key=%s",
                start_lat, start_lng, end_lat, end_lng, apiKey);
        HttpResponse<JsonNode> response = null;
        try {
            response = Unirest.get(url)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        // Hold the value of the JSON body (important)
        String json = response.getBody().toString();
        builder = new GsonBuilder();
        jsonConverter = builder.create();
        return jsonConverter.fromJson(json, RootObject.class);
    }
}
