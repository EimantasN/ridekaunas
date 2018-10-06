package com.example.pertrauktiestaskas.models;

import java.util.List;

public class RouteSegment
{
    public int RouteSegmentType;
    public String IconUrl;
    public int DurationMinutes;
    public int WalkDistanceMeters;
    public int DistanceMeters;
    public int StopsCount;
    public StartPoint StartPoint;
    public EndPoint EndPoint;
    public String Shape;
    public Transport Transport;
    public List<Object> OtherTransports;
}
