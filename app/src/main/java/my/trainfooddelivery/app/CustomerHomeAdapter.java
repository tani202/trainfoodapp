package my.trainfooddelivery.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.cert.PolicyNode;
import java.util.List;

import my.trainfooddelivery.app.R;
import my.trainfooddelivery.app.UpdateDishModel;

public class CustomerHomeAdapter extends RecyclerView.Adapter<CustomerHomeAdapter.ViewHolder> {

    private Context mcontext;
    private List<UpdateDishModel>updateDishModellist;





    public CustomerHomeAdapter(Context context , List<UpdateDishModel>updateDishModelslist){

        this.updateDishModellist = updateDishModelslist;
        this.mcontext = context;
    }


    @NonNull
    @Override
    public CustomerHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.customer_menudish,parent,false);
        return new CustomerHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHomeAdapter.ViewHolder holder, int position) {

        final UpdateDishModel updateDishModel = updateDishModellist.get(position);
        Glide.with(mcontext).load(updateDishModel.getRandomUID()).into(holder.imageView);
        holder.Dishname.setText(updateDishModel.getDishes());
        updateDishModel.getRandomUID();
        updateDishModel.getChefId();
        holder.Price.setText("Price: "+updateDishModel.getPrice()+"Rs");

    }

    @Override
    public int getItemCount() {
        return updateDishModellist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView Dishname,Price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.menu_image);
            Dishname = itemView.findViewById(R.id.dishname);
            Price = itemView.findViewById(R.id.dishprice);
        }
    }

}

