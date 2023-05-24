package my.trainfooddelivery.app;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;




import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import my.trainfooddelivery.app.ChefFoodPanel.Chef;

public class ChefPendingAdapter extends RecyclerView.Adapter<ChefPendingAdapter.ViewHolder> implements IBaseGpsListener{

    private Context mcontext;
    private List<placedorder> updateDishModellist;
    private static final int PERMISSION_LOCATION = 1;
    private double latitude;
    private double longitude;
    DatabaseReference locationRef,dataa;
    String Area,Code;




    public ChefPendingAdapter(Activity context, List<placedorder>updateDishModelslist){

        this.updateDishModellist = updateDishModelslist;
        this.mcontext = context;


    }


    @NonNull
    public ChefPendingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card_view, parent, false);
        return new ChefPendingAdapter.ViewHolder(view);
    }




    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ChefPendingAdapter.ViewHolder holder, int position) {

        final placedorder updateDishModel = updateDishModellist.get(position);


        holder.Dishname.setText(""+updateDishModel.getdishes());
        holder.Price.setText("Price: "+updateDishModel.getprice()+"Rs");
        holder.quantity.setText("Qno: "+updateDishModel.getquantity());
        holder.etaTextView.setText("eta:"+updateDishModel.geteta());
        holder.trainno.setText("trainno:"+updateDishModel.gettrainno());
        holder.mobile.setText(""+updateDishModel.getMobileNo());
       holder.name.setText("Customer's name:"+updateDishModel.getCustomerName());

       holder.preparing.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.ACCESS_FINE_LOCATION)
                       != PERMISSION_GRANTED) {
                   ActivityCompat.requestPermissions((Activity) mcontext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
               } else {
                   showLocation();
               }



           }


           public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
               if (requestCode == PERMISSION_LOCATION) {
                   if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                       showLocation();
                   } else {
                       Toast.makeText(mcontext, "Permission not granted!", Toast.LENGTH_SHORT).show();
                   }
               }
           }

           private void showLocation() {
               LocationManager locationManager = (LocationManager) mcontext.getSystemService(Context.LOCATION_SERVICE);
               if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                   //tv_location.setText("Loading location...");
                   if (ActivityCompat.checkSelfPermission((Activity) mcontext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                           ActivityCompat.checkSelfPermission((Activity) mcontext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                       ActivityCompat.requestPermissions((Activity) mcontext, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION);
                       return;
                   }
                   Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                   if (location != null) {
                       latitude = location.getLatitude();
                       longitude = location.getLongitude();



                       FirebaseDatabase database = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app");
                       String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                       DatabaseReference deliveryRef = database.getReference("Delivery Person location");
                       dataa = FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users").child(userid);
                       dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                           @Override
                           public void onDataChange(@NonNull DataSnapshot snapshot) {

                               Chef cheff = snapshot.getValue(Chef.class);
                               if (cheff !=null) {
                                   Code = cheff.getState();
                                   Area = cheff.getArea();
                                   Query query = deliveryRef.child(Code).child(Area).orderByChild("timestamp");

                                   query.addListenerForSingleValueEvent(new ValueEventListener() {
                                       @Override
                                       public void onDataChange (@NonNull DataSnapshot dataSnapshot){
                                           if (dataSnapshot.exists()) {
                                               for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                                   double longti = userSnapshot.child("longtitude").getValue(Double.class);
                                                   double lat = userSnapshot.child("latitude").getValue(Double.class);
                                                   Log.d("long","lonf"+longti);
                                                   Log.d("long","lonf"+lat);



                                               }
                                           } else {
                                               Log.d("error","error");

                                           }
                                       }

                                       @Override
                                       public void onCancelled (@NonNull DatabaseError databaseError){
                                           // Handle any errors that occur during the retrieval process
                                       }
                                   });

                               }


                           }

                           @Override
                           public void onCancelled(@NonNull DatabaseError error) {

                           }




                       });










                       // Display latitude and longitude in a Toast message
                       String message = "Latitude: " + latitude + ", Longitude: " + longitude;
                       Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();
                   } else {
                       Toast.makeText(mcontext, "Location not available", Toast.LENGTH_SHORT).show();
                   }
               }
                  locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ChefPendingAdapter.this);
           }

       });




    }
    private String getLocationText(Location location) {
        return "Lat: " + location.getLatitude() + "\nLon: " + location.getLongitude();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public int getItemCount() {
        return updateDishModellist.size();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        String message = "Latitude: " + latitude + ", Longitude: " + longitude;
        Toast.makeText(mcontext, message, Toast.LENGTH_SHORT).show();

        // Do something with the latitude and longitude
        String locationText = getLocationText(location);
        Log.d("Location", locationText);


    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onGpsStatusChanged(int event) {

    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView etaTextView,trainno,name,mobile;
        TextView Dishname,Price,quantity;
        Button preparing ,shipping;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Dishname = itemView.findViewById(R.id.txt_dish_name);
            Price = itemView.findViewById(R.id.text_price);
            quantity=itemView.findViewById(R.id.text_quantity);
            etaTextView=itemView.findViewById(R.id.eta);
            mobile=itemView.findViewById(R.id.customer_mobile);
            name=itemView.findViewById(R.id.customer_name);
            trainno=itemView.findViewById(R.id.trainno);
            preparing=itemView.findViewById(R.id.btn_view_details);
            shipping=itemView.findViewById(R.id.btn_send_shipping);






        }
    }



}

