package com.example.pertrauktiestaskas.trulify;

import com.example.pertrauktiestaskas.models.RootObject;
import com.example.pertrauktiestaskas.models.TrafiListModel;

import java.util.List;

public class BusThread implements Runnable {

    @Override
    public void run() {
        RootObject data = BusApiHandler.GetRouteData("54.901694", "23.961288", "54.893767", "23.925123");
        List<TrafiListModel> m = BusApiHandler.FormatRoutesToListModel(data);
    }
}
