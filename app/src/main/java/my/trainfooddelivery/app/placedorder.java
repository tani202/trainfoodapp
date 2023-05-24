package my.trainfooddelivery.app;


    public class placedorder {
        private String dishes;
        private String price;
        private String quantity;
        private String eta;
        private String trainno;
        private String CustomerName;
        private String MobileNo;


        public placedorder() {
            // Default constructor required for calls to DataSnapshot.getValue(Order.class)
        }

        public placedorder(String dishName, String price, String quantity, String eta,String name,String mobile, String trainno) {
            this.dishes = dishName;
            this.price = price;
            this.quantity = quantity;
            this.eta = eta;
            this.trainno = trainno;
            this.CustomerName=name;
            this.MobileNo=mobile;
        }

        public void setprice(String price )
        {
            this.price=price;
        }

        public String getprice()
        {
            return price;
        }
        public void seteta(String eta) {
            this.eta = eta;
        }

        public void setquantity(String quantity) {
            this.quantity = quantity;
        }

        public void settraino(String trainno) {
            this.trainno = trainno;
        }

        public String getdishes() {
            return dishes;
        }

        public void setdishes(String dishes) {
            this.dishes = dishes;
        }

        public String geteta() {
            return eta;
        }

        public String getquantity() {
            return quantity;
        }

        public String gettrainno() {
            return trainno;
        }

        public String getCustomerName(){
            return CustomerName;
        }
         public String getMobileNo()
         {
             return MobileNo;
         }

        public void setMobileNo(String mobileNo) {
            MobileNo = mobileNo;
        }

        public void setCustomerName(String customerName) {
            CustomerName = customerName;
        }
    }



