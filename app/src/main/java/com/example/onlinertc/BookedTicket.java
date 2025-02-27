package com.example.onlinertc;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Bitmap;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookedTicket extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 100;
    private static final String IMAGE_URL = "https://your-image-url.com/image.jpg"; // Replace with actual URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_ticket);

        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.darkbg));

        TextView tnkyou = findViewById(R.id.tnkyou);
        tnkyou.setText("Thank You, " + UserNumber.getInstance().getNumber());

        // Get references to UI elements
        TextView userIDText = findViewById(R.id.userIDText);
        TextView boardingText = findViewById(R.id.boardingText);
        TextView destinationText = findViewById(R.id.destinationText);
        TextView fareText = findViewById(R.id.fareText);
        ImageView qrCodeImage = findViewById(R.id.qrCodeImage);

        AppCompatButton backtohome=findViewById(R.id.backtohome);

        // Get data from intent
        String userID = getIntent().getStringExtra("UserID");
        String boarding = getIntent().getStringExtra("BoardingPoint");
        String destination = getIntent().getStringExtra("DestinationPoint");
        boolean status = Boolean.parseBoolean(getIntent().getStringExtra("status"));
        String fare = getIntent().getStringExtra("Fare");
        String UID = getIntent().getStringExtra("UID");
        // Set values to UI elements
        userIDText.setText(userID);
        boardingText.setText(boarding);
        destinationText.setText(destination);
        fareText.setText("Rs. " + fare);
        // Generate QR code
        String QR_data = UID;
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(QR_data, BarcodeFormat.QR_CODE, 1200, 1200);
            qrCodeImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Download button click listener
        AppCompatButton screenshotButton = findViewById(R.id.tktdownload);
        screenshotButton.setOnClickListener(v -> captureScreenshot());
        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backtohome.setText("Going Back To Home.. \uD83D\uDC4B");

                new android.os.Handler().postDelayed(() -> {
                    Intent intent = new Intent(BookedTicket.this, TKT.class);
                    startActivity(intent);
                },2000);
            }
        });
    }
    private void captureScreenshot() {
        try {
            // Get the root view of the activity
            View rootView = getWindow().getDecorView().getRootView();

            // Create a bitmap
            Bitmap bitmap = Bitmap.createBitmap(rootView.getWidth(), rootView.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            rootView.draw(canvas);

            // Save the bitmap
            saveBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to capture screenshot", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveBitmap(Bitmap bitmap) {
        try {
            // Create file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "Screenshot_" + timeStamp + ".png");

            // Save to file
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            Toast.makeText(this, "Screenshot saved: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


