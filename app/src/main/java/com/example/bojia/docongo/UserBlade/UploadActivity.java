package com.example.bojia.docongo.UserBlade;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bojia.docongo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {

    EditText textname, textprice, textmg, textdescription, textexpiry;
    Button choose_img, upload;
    ImageView imageView;
    final int CODE_GALLERY_REQUEST = 999;
    Bitmap bitmap;

    private static String URL_PROCESS = "https://kdtravelandtours.com/grabmed/upload_medicine";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        textname = findViewById(R.id.txtname);
        textprice = findViewById(R.id.txtprice);
        textmg = findViewById(R.id.txtmg);
        textdescription = findViewById(R.id.txtdesc);
        textexpiry = findViewById(R.id.txtexpiry);
        choose_img = findViewById(R.id.choosebtn);
        upload = findViewById(R.id.uploadbtn);
        imageView = findViewById(R.id.prescriptionimg);

        choose_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityCompat.requestPermissions(
                            UploadActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            CODE_GALLERY_REQUEST);
                }
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data1 = textname.getText().toString();
                String data2= textprice.getText().toString();
                String data3 = textmg.getText().toString();
                String data4 = textdescription.getText().toString();
                String data5 = textexpiry.getText().toString();
                update_data(data1,data2,data3,data4,data5);
            }
        });
    }

    public void update_data(final String d1, final String d2, final String d3, final String d4, final String d5){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PROCESS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(UploadActivity.this, "Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(UploadActivity.this, "Error Uploading! Please try again!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e){
                            e.printStackTrace();
                            Toast.makeText(UploadActivity.this, "Something went wrong! Please contact the developers", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UploadActivity.this, "No Internet Connection: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", d1);
                params.put("price", d2);
                params.put("mg", d3);
                params.put("description", d4);
                params.put("expiry", d5);
                String imageData = imageToString(bitmap);
                params.put("image",imageData);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CODE_GALLERY_REQUEST){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image"),CODE_GALLERY_REQUEST);
            } else {
                Toast.makeText(this, "You dont have permission to access gallery", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null){
            Uri filepath = data.getData();
            if(filepath != null){
                try {
                    InputStream inputStream = getContentResolver().openInputStream(filepath);
                    bitmap  = BitmapFactory.decodeStream(inputStream);
                    imageView.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
