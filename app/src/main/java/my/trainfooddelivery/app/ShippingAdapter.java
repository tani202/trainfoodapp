package my.trainfooddelivery.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ShippingAdapter extends RecyclerView.Adapter<ShippingAdapter.ViewHolder> {
    private Context mcontext;
    private DatabaseReference data;
    private List<placedorder> updateDishModellist;
   private String orderid;

    public ShippingAdapter(Context context, List<placedorder> updateDishModelList,String order) {
        this.mcontext=context;
        this.updateDishModellist=updateDishModelList;
       this.orderid=order;
    }

    @NonNull
    @Override
    public ShippingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.shipping,parent,false);
        return new ShippingAdapter.ViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull ShippingAdapter.ViewHolder holder, int position) {

        final placedorder updateDishModel = updateDishModellist.get(position);

        holder.fprice.setText("Grand total"+updateDishModel.getTotalPrice());
        holder.food.setText(""+updateDishModel.getdishes());

        holder.etaText.setText("Eta:" + updateDishModel.geteta());
        holder.train.setText("Train no:" + updateDishModel.gettrainno());
        holder.mob.setText("Mobile: " + updateDishModel.getMobileNo());
        holder.Name.setText("Name: " + updateDishModel.getCustomerName());
        holder.coach.setText("Coach: " + updateDishModel.getCoach());
        holder.rname.setText("Restaurant: " + updateDishModel.getRestaurant());
        holder.seatno.setText("SeatNo: " + updateDishModel.getSeatNumber());
        holder.payment.setText("Payment mode:"+updateDishModel.getPayment());

        holder.delivered.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.delivered.setBackgroundTintList(ContextCompat.getColorStateList(mcontext, R.color.Red));
                }
                data= FirebaseDatabase.getInstance("https://train-food-delivery-39665-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("PENDING DELIVERY");
                data.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot remove:snapshot.getChildren())
                        {

                             String key =remove.getKey();
                                    if(key.equals(orderid))
                                  {
                                      remove.getRef().removeValue();
                                }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return updateDishModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView etaText, train, Name, mob,rname;
        TextView food,fprice,seatno,coach,payment;
        Button delivered;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fprice=itemView.findViewById(R.id.money);
            food=itemView.findViewById(R.id.foods);
            rname=itemView.findViewById(R.id.hotel);
            etaText = itemView.findViewById(R.id.ETa);
            mob = itemView.findViewById(R.id.cusmobile);
            Name = itemView.findViewById(R.id.cusname);
            train = itemView.findViewById(R.id.trainos);
            seatno=itemView.findViewById(R.id.seaT);
            coach=itemView.findViewById(R.id.coacH);
            delivered=itemView.findViewById(R.id.delivered);

            payment=itemView.findViewById(R.id.payment);

        }
    }
}
