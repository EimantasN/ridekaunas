package com.example.pertrauktiestaskas.models;

import java.util.List;

public class Route
{
    public String PreferenceLabel;
    public int DurationMinutes;
    public int WalkMinutes;
    public String DepartureTime;
    public String ArrivalTime;
    public double Price;
    public String Currency;
    public List<RouteSegment> RouteSegments;
}
