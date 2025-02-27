package com.example.onlinertc;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class ticket_card extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.ticket_card);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView QR = findViewById(R.id.QR);
        TicketDetails tck = new TicketDetails();
        String ValidCheck = String.valueOf(tck.isStatus());
        String UID = tck.getUID();
        Switch ShowQR = findViewById(R.id.showQR);
        ImageView QRCODE = findViewById(R.id.QRCODE);
        if (ValidCheck.equals("true")) {
            QR.setImageResource(R.drawable.newtkt);
        } else {
            QR.setImageResource(R.drawable.validated);
        }
    }
}
