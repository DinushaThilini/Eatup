package com.example.eatup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.HashMap;

public class Delivery_Registration extends AppCompatActivity {
    String[] Gampaha={"gampaha","weliweriya","kadawatha","kirillawala"};
    String[] Colombo={"kiribathgoda","colombo1","kelaniya","homagama"};

    TextInputLayout Fname,Lname,Email,Pass,cpass,mobileno,houseno,area,pincode;
    Spinner Statespin,cityspin;
    Button signup,Emaill,phone;
    CountryCodePicker Cpp;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String fname,lname,emailid,password,confpassword,mobile,house,Area,Pincode,statee,cityy;
    String role="DeliveryPerson";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery__registration);

        Fname=(TextInputLayout)findViewById(R.id.fname);
        Lname=(TextInputLayout)findViewById(R.id.lname);
        Email=(TextInputLayout)findViewById(R.id.Emailid);
        Pass=(TextInputLayout)findViewById(R.id.Password);
        cpass=(TextInputLayout)findViewById(R.id.confirmpassword);
        mobileno=(TextInputLayout)findViewById(R.id.mobileno);
        houseno=(TextInputLayout)findViewById(R.id.Houseno);
        area=(TextInputLayout)findViewById(R.id.Areaa);
        pincode=(TextInputLayout)findViewById(R.id.Pincodee);
        Statespin=(Spinner) findViewById(R.id.State);
        cityspin=(Spinner) findViewById(R.id.City);


        signup = (Button)findViewById(R.id.Signupp);
        Emaill = (Button)findViewById(R.id.emaillid);
        phone = (Button)findViewById(R.id.Phonenumber);

        Cpp = (CountryCodePicker)findViewById(R.id.cos);

        Statespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object value = parent.getItemAtPosition(position);
                statee = value.toString().trim();
                if (statee.equals("Gampaha")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String cities : Gampaha) {
                        list.add(cities);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Delivery_Registration.this, android.R.layout.simple_spinner_item, list);
                    cityspin.setAdapter(arrayAdapter);
                }
                if (statee.equals("Colombo")) {
                    ArrayList<String> list = new ArrayList<>();
                    for (String cities : Colombo) {
                        list.add(cities);
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Delivery_Registration.this, android.R.layout.simple_spinner_item, list);
                    cityspin.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        cityspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object value = parent.getItemAtPosition(position);
                cityy = value.toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

        databaseReference = firebaseDatabase.getInstance().getReference("DeliveryPerson");
        FAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fname = Fname.getEditText().getText().toString().trim();
                lname = Lname.getEditText().getText().toString().trim();
                emailid = Email.getEditText().getText().toString().trim();
                mobile = mobileno.getEditText().getText().toString().trim();
                password = Pass.getEditText().getText().toString().trim();
                confpassword = cpass.getEditText().getText().toString().trim();
                Area = area.getEditText().getText().toString().trim();
                house = houseno.getEditText().getText().toString().trim();
                Pincode = pincode.getEditText().getText().toString().trim();

                if (isValid()){
                    final ProgressDialog mDialog = new ProgressDialog(Delivery_Registration.this);
                    mDialog. setCancelable(false);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setMessage("Registation is progress please wait.....");
                    mDialog.show();

                    FAuth.createUserWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful() ){
                                String useridd= FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference=FirebaseDatabase.getInstance().getReference("user" ).child(useridd);
                                final HashMap<String,String> hashMap=new HashMap<>();
                                hashMap.put("Role",role);
                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        HashMap<String,String> hashMap1=new HashMap<>();
                                        hashMap1.put("First Name",fname);
                                        hashMap1.put("Last Name",lname);
                                        hashMap1.put("EmailId",emailid);
                                        hashMap1.put("City",cityy);
                                        hashMap1.put("Area",Area);
                                        hashMap1.put("Password",password);
                                        hashMap1.put("PinCode",Pincode);
                                        hashMap1.put("State",statee);
                                        hashMap1.put("Confirm Password",confpassword);
                                        hashMap1.put("House",house);

                                        firebaseDatabase.getInstance().getReference("Chef")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                mDialog.dismiss();

                                                FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if(task.isSuccessful()){
                                                            AlertDialog.Builder builder=new AlertDialog.Builder(Delivery_Registration.this);
                                                            builder.setMessage("You Have Registered! Make Sure To Verify Your Email");
                                                            builder.setCancelable(false);
                                                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                    String phonenumber= Cpp.getSelectedCountryCodeWithPlus()+mobile;
                                                                    Intent b=new Intent(Delivery_Registration.this,Delivery_Verifyphone.class);
                                                                    b.putExtra("phonenumber",phonenumber);
                                                                    startActivity(b);


                                                                }
                                                            });
                                                            AlertDialog Alert = builder.create();
                                                            Alert.show();


                                                        }else {

                                                            mDialog.dismiss();
                                                            ReusableCodeForAll.ShowAlert(Delivery_Registration.this, "Error", task.getException().getMessage());

                                                        }

                                                    }
                                                });

                                            }
                                        });

                                        Emaill.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                startActivity(new Intent(Delivery_Registration.this,Delivery_Login.class));
                                                finish();
                                            }
                                        });
                                        phone.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                startActivity(new Intent(Delivery_Registration.this,Delivery_Loginphone.class));
                                                finish();
                                            }
                                        });


                                    }
                                });

                            }

                        }
                    });


                }
            }
        });

        Emaill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Delivery_Registration.this,Delivery_Login.class));
                finish();
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Delivery_Registration.this,Delivery_Loginphone.class));
                finish();
            }
        });
    }
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public boolean isValid(){
        Email.setErrorEnabled(false);
        Email.setError("");
        Fname.setErrorEnabled(false);
        Fname.setError("");
        Lname.setErrorEnabled(false);
        Lname.setError("");
        Pass.setErrorEnabled(false);
        Pass.setError("");
        mobileno.setErrorEnabled(false);
        mobileno.setError("");
        cpass.setErrorEnabled(false);
        cpass.setError("");
        area.setErrorEnabled(false);
        area.setError("");
        houseno.setErrorEnabled(false);
        houseno.setError("");
        pincode.setErrorEnabled(false);
        pincode.setError("");

        boolean isvalid=false,isValidname=false,isValidlname=false,isValidemail=false,isValidpassword=false,isValidconfpassword=false,isValidarea=false,isValidlocaladd=false,isValidpincode=false,isValidmobilenum=false;
        if(TextUtils.isEmpty(fname)){
            Fname.setErrorEnabled(true);
            Fname.setError("Enter First Name");
        }else{
            isValidname = true;
        }
        if(TextUtils.isEmpty(lname)){
            Lname.setErrorEnabled(true);
            Lname.setError("Enter Last Name");
        }else{
            isValidlname = true;
        }
        if(TextUtils.isEmpty(emailid)){
            Email.setErrorEnabled(true);
            Email.setError("Email Is Required");
        }else{
            if(emailid.matches(emailpattern)){
                isValidemail = true;
            }else{
                Email.setErrorEnabled(true);
                Email.setError("Enter a Valid Email Id");
            }
        }
        if(TextUtils.isEmpty(password)){
            Pass.setErrorEnabled(true);
            Pass.setError("Enter Password");
        }else{
            if(password.length()<8){
                Pass.setErrorEnabled(true);
                Pass.setError("Password is Weak");
            }else{
                isValidpassword = true;
            }
        }
        if(TextUtils.isEmpty(confpassword)){
            cpass.setErrorEnabled(true);
            cpass.setError("Enter Password Again");
        }else{
            if(!password.equals(confpassword)){
                cpass.setErrorEnabled(true);
                cpass.setError("Password Dosen't Match");
            }else{
                isValidconfpassword = true;
            }
        }
        if(TextUtils.isEmpty(mobile)){
            mobileno.setErrorEnabled(true);
            mobileno.setError("Mobile Number Is Required");
        }else{
            if(mobile.length()<10){
                mobileno.setErrorEnabled(true);
                mobileno.setError("Invalid Mobile Number");
            }else{
                isValidmobilenum = true;
            }
        }
        if(TextUtils.isEmpty(Area)){
            area.setErrorEnabled(true);
            area.setError("Area Is Required");
        }else{
            isValidarea = true;
        }
        if(TextUtils.isEmpty(Pincode)){
            pincode.setErrorEnabled(true);
            pincode.setError("Please Enter Pincode");
        }else{
            isValidpincode = true;
        }
        if(TextUtils.isEmpty(house)){
            houseno.setErrorEnabled(true);
            houseno.setError("Fields Can't Be Empty");
        }else{
            isValidlocaladd = true;
        }

        isvalid = (isValidarea && isValidconfpassword && isValidpassword && isValidpincode && isValidemail && isValidmobilenum && isValidname && isValidlocaladd && isValidlname) ? true : false;
        return isvalid;





    }
}