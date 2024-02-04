package com.dombeu.map; // Make sure this matches your actual package name

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.dombeu.map.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextInput;
    private ImageView imageView;
    private List<Bitmap> imageList; // List to store multiple images
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextInput = findViewById(R.id.editTextInput);
        imageView = findViewById(R.id.imageView);
        Button buttonSend = findViewById(R.id.buttonSend);
        Button buttonAttachImage = findViewById(R.id.buttonAttachImage);

        // Initialize the imageList
        imageList = new ArrayList<>();

        buttonSend.setOnClickListener(view -> {
            String inputText = editTextInput.getText().toString();
            String postData = "{\"data\":\"" + inputText + "\"}";
            String requestUrl = "https://your.server.com/endpoint"; // Replace with your server URL

            // Include your existing NetworkUtils.sendPostRequest code here
            NetworkUtils.sendPostRequest(requestUrl, postData, new NetworkUtils.NetworkCallback() {
                @Override
                public void onSuccess(String response) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Success: " + response, Toast.LENGTH_LONG).show());
                }

                @Override
                public void onError(String error) {
                    // Handle error on the main thread
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_LONG).show());
                }
            });
        });

        // Add functionality to attach images
        buttonAttachImage.setOnClickListener(view -> {
            // Create an intent to capture an image
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            // Retrieve the captured image as a Bitmap
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // Add the captured image to the list
            imageList.add(imageBitmap);
            // Display the captured image in the ImageView
            imageView.setImageBitmap(imageBitmap);
        }
    }
}