package com.example.pertrauktiestaskas.API;

import java.time.Period;
import java.util.Date;
import java.util.List;

public class Models {

    public class UserFast
    {
        public int Id;
        public String Gmail;
        public UserInfo Info;
    }

    public class UserInfo
    {
        public int Id;
        public String Name;
        public String LastName;
        public String ImgUrl;
        public String Gmail;
        public Date LastUsed;

        public Date Created;
        public List<History> History;

        public List<Cards> Cards;

        public List<Route> FavoriteRoutes;
    }

    public enum Transport_type
    {
        T,
        A
    }

    public class Route
    {
        public int Id;
        public String DisplayName;
        public String Latitude;
        public String Longitude;
        public String Address;
        public Date Created;
    }

    public class History
    {
        public int Id;

        public Transport_type Type;

        public String Number;

        public Date Time;
    }

    public class Cards
    {
        public int Id;
        public Date LastUsed;
        public Date LastSync;
        public int Uid;
        public int Tag;
        public Period ValidUntil;
        public double Value;
        public Period  AcitveUntil;
        public boolean Activated;
        public boolean Blocked;
        public String Block_status;
    }
}
