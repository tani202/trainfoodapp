package my.trainfooddelivery.app.customerFoodPanel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private String name, last, mobile;
    DatabaseReference dataa;
    Boolean isOrderAvailable,deliveryperson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customercart, container, false);
        getActivity().setTitle("Cart");
        recyclerView = v.findViewById(R.id.recycle_cart);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateCartList = new ArrayList<>();
        placeorder = v.findViewById(R.id.place_order_button);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ORDERS");

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

                adapter = new CustomerCartAdapter(getContext(), updateCartList, userId);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enter Seat and Coach Details");


                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_seat_coach, null);
                builder.setView(dialogView);

                EditText seatNumberEditText = dialogView.findViewById(R.id.edit_seat_number);
                EditText coachEditText = dialogView.findViewById(R.id.edit_coach);

                builder.setPositiveButton("Place Order", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String seatNumber = seatNumberEditText.getText().toString().trim();
                        String coach = coachEditText.getText().toString().trim();

                        if (seatNumber.isEmpty() || coach.isEmpty()) {
                            Toast.makeText(getContext(), "Please enter seat number and coach details", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        placeOrder(seatNumber, coach);
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();
            }
        });

        return v;
    }

    private void placeOrder(String seatNumber, String coach) {
        DatabaseReference availableOrdersRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("AVAILABLE ORDERS");
        DatabaseReference placedOrdersRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("PLACED ORDERS");

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
                            deliveryperson=false;
                            DatabaseReference customerOrderRef = placedOrdersRef.child(restaurantName).child(customerID);

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
                                        customerOrderRef.child("SeatNumber").setValue(seatNumber);
                                        customerOrderRef.child("Coach").setValue(coach);
                                        customerOrderRef.child("Deliveryperson").setValue(deliveryperson);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // Handle any errors here
                                }
                            });
                            List<UpdateDishModel> selectedItems = new ArrayList<>();
                            int totalPrice = 0;
                            // Move dish data from "AVAILABLE ORDERS" to "PLACED ORDERS"
                            for (DataSnapshot dishSnapshot : customerSnapshot.getChildren()) {
                                String dishName = dishSnapshot.getKey();
                                UpdateDishModel dish = dishSnapshot.getValue(UpdateDishModel.class);
                                if (dish != null) {
                                    int price = Integer.parseInt(dish.getPrice());
                                    DatabaseReference dishRef = customerOrderRef.child(dishName);
                                    dishRef.setValue(dish);
                                    selectedItems.add(dish);
                                    totalPrice=totalPrice+price;
                                }
                            }
                            String totalPriceString = String.valueOf(totalPrice);
                            // Remove the customer's order from "AVAILABLE ORDERS"
                            customerSnapshot.getRef().removeValue();
                            showOrderDetailsPopup(selectedItems, totalPriceString,restaurantName,customerID);


                            break;// Exit the loop once the customer's order is found
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

    private void showOrderDetailsPopup(List<UpdateDishModel> selectedItems, String totalPriceString,String res,String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Order Details");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        DatabaseReference placedOrdersRef = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("PLACED ORDERS");
        DatabaseReference customerOrderRef = placedOrdersRef.child(res).child(id);


        // Section 2: Selected Items
        TextView selectedItemsTextView = new TextView(getContext());
        StringBuilder selectedItemsText = new StringBuilder();
        for (UpdateDishModel item : selectedItems) {
            selectedItemsText.append(item.getDishes()).append(" - ")
                    .append("Price: ").append(item.getPrice()).append(", ")
                    .append("Quantity: ").append(item.getQuantity()).append("\n");
        }
        selectedItemsTextView.setText(selectedItemsText.toString());
        layout.addView(selectedItemsTextView);

        // Section 3: Total Price
        TextView totalPriceTextView = new TextView(getContext());
        totalPriceTextView.setText("Total Price: " + totalPriceString);
        layout.addView(totalPriceTextView);

        // Section 4: Payment Method
        RadioGroup paymentMethodRadioGroup = new RadioGroup(getContext());
        paymentMethodRadioGroup.setOrientation(LinearLayout.VERTICAL);

        RadioButton cashRadioButton = new RadioButton(getContext());
        cashRadioButton.setText("Cash on Delivery");
        paymentMethodRadioGroup.addView(cashRadioButton);

        RadioButton cardRadioButton = new RadioButton(getContext());
        cardRadioButton.setText("Credit/Debit on Delivery");
        paymentMethodRadioGroup.addView(cardRadioButton);


        layout.addView(paymentMethodRadioGroup);

        builder.setView(layout);

        builder.setPositiveButton("Confirm Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                    int selectedPaymentId = paymentMethodRadioGroup.getCheckedRadioButtonId();
                    String paymentMethod;

                    if (selectedPaymentId == cashRadioButton.getId()) {
                        paymentMethod = "Cash on Delivery";

                    }
                    else if(selectedPaymentId == cardRadioButton.getId()){
                        paymentMethod="Credit/Debit on Delivery";


                }
                    else {
                        Toast.makeText(getContext(), "Please select a payment method", Toast.LENGTH_SHORT).show();
                        return;
                    }

                customerOrderRef.child("Payment").setValue(paymentMethod);


                Toast.makeText(getContext(), "Order placed successfully!", Toast.LENGTH_SHORT).show();

                }

        });

        builder.setNegativeButton("Cancel Order", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                customerOrderRef.removeValue();
                Toast.makeText(getContext(), "Order canceled successfully!", Toast.LENGTH_SHORT).show();
            }
        });



        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }


}
