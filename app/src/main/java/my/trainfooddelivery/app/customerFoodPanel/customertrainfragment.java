package my.trainfooddelivery.app.customerFoodPanel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import my.trainfooddelivery.app.R;
import okhttp3.Call;
//import okhttp3.Callback;
//import okhttp3.OkHttpClient;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.android.material.textfield.TextInputLayout;

public class customertrainfragment extends Fragment {
    Button search;
    EditText trainno ;
    TextView res;
    private Spinner resultSpinner;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View v = inflater.inflate(R.layout.activity_customertrainfragment, null);
        getActivity().setTitle("Place Order");


        resultSpinner =v.findViewById(R.id.result_spinner);

        trainno = v.findViewById(R.id.Trainno);
        search=v.findViewById(R.id.search);
        // res=findViewById(R.id.result);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String t =trainno.getText().toString();
                String startDay="0";
                try {
                    fetchData(t,startDay);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

      return v;

    }

    private void fetchData(String trainno,String startDay) throws IOException {
        String url = "https://irctc1.p.rapidapi.com/api/v1/liveTrainStatus?trainNo="+trainno+"&startDay="+startDay;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("X-RapidAPI-Key", "c9721dd284mshcf53d38c658666cp1c6e1ejsnf68a5cb74c97")
                .addHeader("X-RapidAPI-Host", "irctc1.p.rapidapi.com")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Some error!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                if (response.isSuccessful()) {
                    String resp = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject json = new JSONObject(resp);
                                JSONArray upcoming_stations = json.getJSONObject("data").getJSONArray("upcoming_stations");
                                List<String> stationNames = new ArrayList<>();
                                for (int i = 0; i < upcoming_stations.length(); i++) {
                                    JSONObject station = upcoming_stations.getJSONObject(i);
                                    String stationName = station.getString("station_name");
                                    stationNames.add(stationName);
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, stationNames);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                // Set the adapter to the Spinner
                                resultSpinner.setAdapter(adapter);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }



}