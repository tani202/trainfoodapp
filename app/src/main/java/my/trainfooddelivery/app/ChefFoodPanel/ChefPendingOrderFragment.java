package my.trainfooddelivery.app.ChefFoodPanel;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import my.trainfooddelivery.app.ChefPendingAdapter;
import my.trainfooddelivery.app.CustomerHomeAdapter;
import my.trainfooddelivery.app.Order;
import my.trainfooddelivery.app.R;
import my.trainfooddelivery.app.UpdateDishModel;
import my.trainfooddelivery.app.placedorder;

public class ChefPendingOrderFragment extends Fragment {
    private List<placedorder> orderList;
    private ChefPendingAdapter adapter;
    RecyclerView recyclerView;
    private Button backButton; // Reference to the "Back" button
    private String rname;
    private DatabaseReference database, data; // Reference to the "Placed Orders" node in Firebase

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout


        // Get references to the views
        View v = inflater.inflate(R.layout.fragment_chef_pendingorders, null);
        getActivity().setTitle("Pending orders");
        recyclerView = v.findViewById(R.id.card_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderList = new ArrayList<>();
        backButton = v.findViewById(R.id.back_button);


        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        data = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users").child(userid);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Chef cheff = snapshot.getValue(Chef.class);
                assert cheff != null;
                rname = cheff.getRestaurant();
                Log.d("onDataChange: ", "Failed to read value." + rname);
                database = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("PLACED ORDERS");

                Query orderQuery = database.child(rname);

                orderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot idSnapshot : snapshot.getChildren()) {
                            placedorder order = new placedorder();
                            order.setCustomerName(idSnapshot.child("CustomerName").getValue(String.class));
                            order.setMobileNo(idSnapshot.child("MobileNo").getValue(String.class));

                            for (DataSnapshot keyname : idSnapshot.getChildren()) {
                                if (keyname.getKey().equals("CustomerName") || keyname.getKey().equals("MobileNo")) {
                                    continue; // Skip the "CustomerName" and "MobileNo" child nodes
                                }

                                order.setdishes(keyname.child("dishes").getValue(String.class));
                                order.setquantity(keyname.child("quantity").getValue(String.class));
                                order.setprice(keyname.child("price").getValue(String.class));
                                order.seteta(keyname.child("eta").getValue(String.class));
                                order.settraino(keyname.child("trainno").getValue(String.class));
                                orderList.add(order);
                            }
                        }

                        adapter = new ChefPendingAdapter(getActivity(), orderList);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled event
                    }
                });
            }



                @Override
                public void onCancelled (@NonNull DatabaseError error){
                    Log.e("onDataChange: ", "Failed to read value.", error.toException());
                }
            });


        // Get a reference to the "Placed Orders" node in Firebase


        return v;

    }
}