package my.trainfooddelivery.app.ChefFoodPanel;

public class FoodDetails {
    public String Dishes,Quantity,Price,Description,Chefid,RandomUID,ImageURL;
    // Alt+insert

    public FoodDetails(String dishes, String quantity, String price, String description,String randomUID, String chefid,String imageURL) {
        Dishes = dishes;
        Quantity = quantity;
        Price = price;
        Description = description;
        ImageURL = imageURL;
        RandomUID = randomUID;
        Chefid = chefid;
    }
}
