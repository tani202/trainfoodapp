package my.trainfooddelivery.app.customerFoodPanel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import my.trainfooddelivery.app.ChefFoodPanel.Chef;
import my.trainfooddelivery.app.CustomerCartAdapter;
import my.trainfooddelivery.app.CustomerHomeAdapter;
import my.trainfooddelivery.app.R;
import my.trainfooddelivery.app.UpdateDishModel;

public class CustomerCartFragment extends Fragment {

    RecyclerView recyclerView;
    private List<UpdateDishModel> updateCartList;
    private CustomerCartAdapter adapter;
    private Button placeorder;
    private String name,last,mobile;
    DatabaseReference dataa;
    Boolean isOrderAvailable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customercart, container, false);
        getActivity().setTitle("Cart");
        recyclerView = v.findViewById(R.id.recycle_cart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateCartList = new ArrayList<>();
        placeorder=v.findViewById(R.id.place_order_button);


        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ORDERS");
//        long sixHoursAgoTimestamp = (System.currentTimeMillis() / 1000) - (6* 60 * 60);
//        long unixTime = System.currentTimeMillis() / 1000L;
//        String sixHoursAgoString = Long.toString(sixHoursAgoTimestamp);

// Convert unixTime to string

// Query dishes ordered within the past 6 hours
//        Query latestDishesQuery = database.child(userId)
//                .orderByKey();


//        long currentTime = System.currentTimeMillis();
//        long fourHoursAgo = currentTime - (10 * 60 * 1000); // 4 hours ago in milliseconds
//        Log.d("time","10 min"+fourHoursAgo);
        Query latestDishesQuery = database.child(userId)
                .orderByChild("timestamp");



        latestDishesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dishSnapshot : snapshot.getChildren()) {

                    UpdateDishModel cart = new UpdateDishModel();

                    cart.setDishes(dishSnapshot.child("dishName").getValue(String.class));
                    cart.setQuantity(dishSnapshot.child("quantity").getValue(String.class));
                    cart.setPrice(dishSnapshot.child("price").getValue(String.class));
                    cart.setRandomUID(dishSnapshot.child("imageURL").getValue(String.class));
                    cart.setRestaurant(dishSnapshot.child("restaurant").getValue(String.class));
                    cart.setEta(dishSnapshot.child("mEta").getValue(String.class));
                    cart.setTrainno(dishSnapshot.child("trainno").getValue(String.class));
                    updateCartList.add(cart);




                }
//                    Collections.reverse(updateCartList);
                adapter = new CustomerCartAdapter(getContext(), updateCartList,userId);
                recyclerView.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors here
            }
        });



        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference availableOrdersRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("AVAILABLE ORDERS");
                DatabaseReference placedOrdersRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("PLACED ORDERS");

                // Get the current user's ID
                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                availableOrdersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean isOrderAvailable = false;
                        for (DataSnapshot restaurantSnapshot : snapshot.getChildren()) {
                            String restaurantName = restaurantSnapshot.getKey();
                            for (DataSnapshot customerSnapshot : restaurantSnapshot.getChildren()) {
                                String customerID = customerSnapshot.getKey();
                                if (customerID.equals(userID)) {
                                    isOrderAvailable = true;
                                    DatabaseReference customerOrderRef = placedOrdersRef.child(restaurantName).child(customerID);

                                    // Get customer details from the "users" node
                                    DatabaseReference userDataRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users").child(userID);
                                    userDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Customer cust = snapshot.getValue(Customer.class);
                                            if (cust != null) {
                                                name = cust.getFirstName() + " " + cust.getLastName();
                                                mobile = cust.getMobileNo();
                                                Log.d("name", "name" + name);
                                                Log.d("mobile", "mobile" + mobile);

                                                customerOrderRef.child("CustomerName").setValue(name);
                                                customerOrderRef.child("MobileNo").setValue(mobile);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            // Handle any errors here
                                        }
                                    });

                                    // Move dish data from "AVAILABLE ORDERS" to "PLACED ORDERS"
                                    for (DataSnapshot dishSnapshot : customerSnapshot.getChildren()) {
                                        String dishName = dishSnapshot.getKey();
                                        UpdateDishModel dish = dishSnapshot.getValue(UpdateDishModel.class);
                                        if (dish != null) {
                                            DatabaseReference dishRef = customerOrderRef.child(dishName);
                                            dishRef.setValue(dish);
                                        }
                                    }

                                    // Remove the customer's order from "AVAILABLE ORDERS"
                                    customerSnapshot.getRef().removeValue();
                                    Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();

                                    break; // Exit the loop once the customer's order is found
                                }
                            }
                        }

                        if (!isOrderAvailable) {
                            Toast.makeText(getContext(), "Please check availability of the items!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle any errors here
                    }
                });
            }
        });







        return v;
    }
}

