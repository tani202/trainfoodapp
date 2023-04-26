package my.trainfooddelivery.app;

public class UpdateDishModel {

    String Dishes,RandomUID,Description,Quantity,Price,ImageURL,Restaurant;

    // Press Alt+insert
      public UpdateDishModel()
      {

      }
    public UpdateDishModel(String  dish,String desc,String randomuid,String quan,String price,String image,String restaurant){
        Dishes=dish;
        RandomUID=randomuid;
        Description=desc;
        Quantity=quan;
        Price=price;
        ImageURL=image;
        Restaurant=restaurant;
    }

    public String getDishes() {
        return Dishes;
    }

    public void setDishes(String dishes) {
        Dishes = dishes;
    }

    public String getRandomUID() {
        return RandomUID;
    }

    public void setRandomUID(String randomUID) {
        RandomUID = randomUID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public void setRestaurant(String restaurant){  Restaurant=restaurant;;}

    public String getRestaurant(){ return Restaurant ;}


}
