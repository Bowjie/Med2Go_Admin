package com.example.bojia.docongo.UserBlade;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bojia.docongo.R;
import com.example.bojia.docongo.UserAuth.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;
    String value;

    SessionManager sessionManager;

    public MyAdapter(List<ListItem> listItems, Context context){
        this.listItems = listItems;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_list_item2, parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder viewHolder, int i) {
        final ListItem listItem = listItems.get(i);

        viewHolder.pk.setText(listItem.getPrimary_id());
        double compute = Double.parseDouble(listItem.getQuantity()) * Double.parseDouble(listItem.getPrice());
        String convert = Double.toString(compute);
        String total = "Quantity: "+listItem.getQuantity()+" - Total: "+convert;
        viewHolder.price.setText(total);
        viewHolder.imgdesc.setText(listItem.getImg_desc());
        viewHolder.miligram.setText(listItem.getMiligram());
        String exp_date = "Expiry date: "+listItem.getExpiry();
        viewHolder.expiry.setText(exp_date);

        Picasso.with(context)
                .load(listItem.getImg_url())
                .placeholder(R.drawable.loader)
                .into(viewHolder.img);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Opening "+ listItem.getImg_desc(), Toast.LENGTH_SHORT).show();
                value = listItem.getPrimary_id();
                gamotdetails();
            }
        });
    }
    private void gamotdetails(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "https://kdtravelandtours.com/grabmed/orderfulldetails?primary_id="+value,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray array = jsonObject.getJSONArray("gamotdetails");

                            for(int i = 0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                String order_id = object.getString("data");
                                String user_name = object.getString("data1");
                                String user_email = object.getString("data2");
                                String user_phone = object.getString("data3");
                                String user_address = object.getString("data4");
                                String user_city = object.getString("data5");
                                String user_latitude = object.getString("data6");
                                String user_longtitude = object.getString("data7");
                                String uploads_img_description = object.getString("data8");
                                String uploads_imgurl = object.getString("data9");
                                String uploads_price = object.getString("data10");
                                String uploads_miligram = object.getString("data11");
                                String uploads_expiry = object.getString("data12");
                                String prescription = object.getString("data13");
                                String notes = object.getString("data14");
                                String quantity = object.getString("data15");

                                Intent intent = new Intent(context, Orderfulldetails.class);

                                intent.putExtra("order_id", order_id);
                                intent.putExtra("user_name", user_name);
                                intent.putExtra("user_email", user_email);
                                intent.putExtra("user_phone", user_phone);
                                intent.putExtra("user_address", user_address);
                                intent.putExtra("user_city", user_city);
                                intent.putExtra("user_latitude", user_latitude);
                                intent.putExtra("user_longtitude", user_longtitude);
                                intent.putExtra("uploads_img_description", uploads_img_description);
                                intent.putExtra("uploads_imgurl", uploads_imgurl);
                                intent.putExtra("uploads_price", uploads_price);
                                intent.putExtra("uploads_miligram", uploads_miligram);
                                intent.putExtra("uploads_expiry", uploads_expiry);
                                intent.putExtra("prescription", prescription);
                                intent.putExtra("notes", notes);
                                intent.putExtra("quantity", quantity);

                                context.startActivity(intent);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView img;
        TextView imgdesc,price, pk, miligram, expiry;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.image_view);
            imgdesc = itemView.findViewById(R.id.img_description);
            price = itemView.findViewById(R.id.Price);
            pk = itemView.findViewById(R.id.primary_id);
            miligram = itemView.findViewById(R.id.miligram);
            cardView = itemView.findViewById(R.id.cardView);
            expiry = itemView.findViewById(R.id.expiry);

            sessionManager = new SessionManager(context);
            sessionManager.checkLogin();
        }
    }
}