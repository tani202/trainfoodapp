package my.trainfooddelivery.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class Chefloginphone extends AppCompatActivity {
   EditText num;
   Button sendotp,signinemail;
   TextView signup;
   CountryCodePicker cpp;
   FirebaseAuth FAuth;
   String number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chefloginphone);
           num=(EditText)findViewById(R.id.number);
           sendotp=(Button)findViewById(R.id.otp);
           cpp=(CountryCodePicker)findViewById(R.id.CountryCode);
           signinemail=(Button)findViewById(R.id.btnEmail);
           signup=(TextView)findViewById(R.id.acsignup);

           FAuth=FirebaseAuth.getInstance();
           sendotp.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   number=num.getText().toString().trim();
                   String Phonenum =cpp.getSelectedCountryCodeWithPlus()+number;
                   Intent b =new Intent(Chefloginphone.this,Chefsendotp.class);
                   b.putExtra("Phonenum",Phonenum);
                   startActivity(b);
                   finish();

               }
           });
         signup.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(Chefloginphone.this,ChefRegistration.class));
                 finish();
             }
         });
         signinemail.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 startActivity(new Intent(Chefloginphone.this,Cheflogin.class));
                 finish();
             }
         });



    }
}
