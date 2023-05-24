package my.trainfooddelivery.app.deliveryFoodPanel;

public class LocationData {

    private  double latitude;
    private  double longtitude;
    private long  timestamp;




    public LocationData( double lat, double  longtitude,long timestamp)
        {
            this.latitude=lat;
            this.longtitude=longtitude;
            this.timestamp=timestamp;
        }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}



