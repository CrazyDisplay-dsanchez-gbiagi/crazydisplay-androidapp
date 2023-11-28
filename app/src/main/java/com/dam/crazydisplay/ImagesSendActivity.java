package com.dam.crazydisplay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class ImagesSendActivity extends AppCompatActivity {

    AppData appData;
    ClientMessageControler clientMessageControler;
    public ImagesSendActivity(){
        appData = AppData.getInstance(); // This is a singleton to save data between views
        clientMessageControler = appData.getClientMessageControler();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_send);

        Button buttonHome = findViewById(R.id.buttonHome);
        ImageView img1 = findViewById(R.id.imageView);
        ImageView img2 = findViewById(R.id.imageView2);
        ImageView img3 = findViewById(R.id.imageView3);
        ImageView img4 = findViewById(R.id.imageView4);
        ImageView img5 = findViewById(R.id.imageView5);
        ImageView img6 = findViewById(R.id.imageView6);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImagesSendActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject objResponse = null;
                try {
                    objResponse = new JSONObject("{}");
                    objResponse.put("type", "message");
                    objResponse.put("format", "img");
                    objResponse.put("ext", "jpg");
                    objResponse.put("value", imgToBase64(img1));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                clientMessageControler.sendMessage(objResponse.toString());
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject objResponse = null;
                try {
                    objResponse = new JSONObject("{}");
                    objResponse.put("type", "message");
                    objResponse.put("format", "img");
                    objResponse.put("ext", "jpg");
                    objResponse.put("value", imgToBase64(img2));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                clientMessageControler.sendMessage(objResponse.toString());
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject objResponse = null;
                try {
                    objResponse = new JSONObject("{}");
                    objResponse.put("type", "message");
                    objResponse.put("format", "img");
                    objResponse.put("ext", "jpg");
                    objResponse.put("value", imgToBase64(img3));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                clientMessageControler.sendMessage(objResponse.toString());
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject objResponse = null;
                try {
                    objResponse = new JSONObject("{}");
                    objResponse.put("type", "message");
                    objResponse.put("format", "img");
                    objResponse.put("ext", "jpg");
                    objResponse.put("value", imgToBase64(img4));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                clientMessageControler.sendMessage(objResponse.toString());
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject objResponse = null;
                try {
                    objResponse = new JSONObject("{}");
                    objResponse.put("type", "message");
                    objResponse.put("format", "img");
                    objResponse.put("ext", "jpg");
                    objResponse.put("value", imgToBase64(img5));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                clientMessageControler.sendMessage(objResponse.toString());
            }
        });
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject objResponse = null;
                try {
                    objResponse = new JSONObject("{}");
                    objResponse.put("type", "message");
                    objResponse.put("format", "img");
                    objResponse.put("ext", "jpg");
                    objResponse.put("value", imgToBase64(img6));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                clientMessageControler.sendMessage(objResponse.toString());
            }
        });
    }
    public static String imgToBase64(ImageView imageView) {
        // Get the Drawable from the ImageView
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

        // Convert the Drawable to a Bitmap
        Bitmap bitmap = drawable.getBitmap();

        // Convert the Bitmap to a Base64 string
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }
}