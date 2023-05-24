package my.trainfooddelivery.app.deliveryFoodPanel;

public class delivery {

    private String State,Area;

    public delivery(){
    }
    // Press Alt+insert


    public delivery( String state, String area) {

        State = state;
        Area = area;

    }



    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }


}