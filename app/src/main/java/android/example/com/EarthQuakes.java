package android.example.com;

import java.util.Date;

public class EarthQuakes{
    private String location;
    private double mag;
    private long time;
    Date dateObj;
    public EarthQuakes(String location, double mag, long time){
        this.location = location;
        this.mag = mag;
        this.time = time;
        //dateObj = new Date(time);
    }

    public String getLocation(){
        return location;
    }

    public double getMagnitude(){
        return mag;
    }

    public long getTime(){

        //SimpleDateFormat formatter = new SimpleDateFormat("MMM DD, YYYY );
        //String formattedTime = formatter.format(dateObj);
        return time;
    }
}