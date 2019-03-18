package com.example.bojia.docongo.UserBlade;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bojia.docongo.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Orderfulldetails extends AppCompatActivity implements View.OnClickListener{

    EditText uname, uemail, uaddress, ucity, uphone;
    ImageView image_view, prescriptionimg;
    TextView img_description,Price,miligram, expiry,add_notes;
    TextView order_id, primary_id, lat, lng;
    Button accept, cancel, locate;

    private static String URL_PROCESS = "https://kdtravelandtours.com/grabmed/accept_order";
    private static String URL_PROCESS_CANCEL = "https://kdtravelandtours.com/grabmed/cancel_order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderfulldetails);

        uname = findViewById(R.id.uname);
        uemail = findViewById(R.id.uemail);
        uphone = findViewById(R.id.uphone);
        uaddress = findViewById(R.id.uaddress);
        ucity = findViewById(R.id.ucity);
        image_view = findViewById(R.id.image_view);
        img_description = findViewById(R.id.img_description);
        Price = findViewById(R.id.Price);
        miligram = findViewById(R.id.miligram);
        order_id = findViewById(R.id.order_id);
        primary_id = findViewById(R.id.primary_id);
        add_notes = findViewById(R.id.add_notes);
        prescriptionimg = findViewById(R.id.prescriptionimg);
        expiry = findViewById(R.id.expiry);
        accept = findViewById(R.id.accept);
        cancel = findViewById(R.id.cancel);
        locate = findViewById(R.id.locate);
        lat = findViewById(R.id.latitude);
        lng = findViewById(R.id.longitude);

        Intent intent = getIntent();

        String data1 = intent.getStringExtra("order_id");
        String data2 = intent.getStringExtra("user_name");
        String data3 = intent.getStringExtra("user_email");
        String data4 = intent.getStringExtra("user_phone");
        String data5 = intent.getStringExtra("user_address");
        String data6 = intent.getStringExtra("user_city");
        String data7 = intent.getStringExtra("user_latitude");
        String data8 = intent.getStringExtra("user_longtitude");
        String data9 = intent.getStringExtra("uploads_img_description");
        String data10 = intent.getStringExtra("uploads_imgurl");
        String data11 = intent.getStringExtra("uploads_price");
        String data12 = intent.getStringExtra("uploads_miligram");
        String data13 = intent.getStringExtra("uploads_expiry");
        String data14 = intent.getStringExtra("prescription");
        String data15 = intent.getStringExtra("notes");
        String data16 = intent.getStringExtra("quantity");

        order_id.setText(data1);
        uname.setText(data2);
        uemail.setText(data3);
        uphone.setText(data4);
        uaddress.setText(data5);
        ucity.setText(data6);
        lat.setText(data7);
        lng.setText(data8);
        img_description.setText(data9);
        Picasso.with(this)
                .load(data10)
                .placeholder(R.drawable.loader)
                .into(image_view);
        double total = Double.parseDouble(data11) * Double.parseDouble(data16);
        String stringTotal = "Quantity: " +data16 + " - Total: P " + Double.toString(total);
        Price.setText(stringTotal);
        miligram.setText(data12);
        String expirydatesu = "Expiry date: "+data13;
        expiry.setText(expirydatesu);
        Picasso.with(this)
                .load(data14)
                .placeholder(R.drawable.loader)
                .into(prescriptionimg);
        add_notes.setText(data15);

        accept.setOnClickListener(this);
        cancel.setOnClickListener(this);
        locate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == accept){
            acceporder();
        }
        if(v == cancel){
            cancelprocess();
        }
        if(v == locate){
            tracker_user();
        }
    }
    public void acceporder(){
        String getOrderID = order_id.getText().toString();
        accept_process(getOrderID);
    }

    public void cancelprocess(){
        String cancelOrderID = order_id.getText().toString();
        cancelorder(cancelOrderID);
    }

    public void accept_process(final String oID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PROCESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(Orderfulldetails.this, "Successfully accepted!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Orderfulldetails.this, RecyclerActivity.class));
                            }
                            else{
                                Toast.makeText(Orderfulldetails.this, "Error accepting order! Please try again!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(Orderfulldetails.this, "Something went wrong! Please contact the developers", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Orderfulldetails.this, "No Internet Connection: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", oID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void cancelorder(final String odID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PROCESS_CANCEL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(Orderfulldetails.this, "Order Cancelled!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Orderfulldetails.this, RecyclerActivity.class));
                            }
                            else{
                                Toast.makeText(Orderfulldetails.this, "Error accepting order! Please try again!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(Orderfulldetails.this, "Something went wrong! Please contact the developers", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Orderfulldetails.this, "No Internet Connection: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("order_id", odID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void tracker_user(){
        String mLat = lat.getText().toString();
        String mLng = lng.getText().toString();

        Intent intent = new Intent(Orderfulldetails.this, MapsActivity.class);
        intent.putExtra("latitude", mLat);
        intent.putExtra("longtitude", mLng);
        startActivity(intent);
    }
}
