package com.example.onlinertc;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class YourBookings extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<TicketDetails> tckdet;
    DatabaseReference databaseReference;
    YourTicketsAdapter yourTicketsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_your_bookings);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ✅ Correct RecyclerView initialization
        recyclerView = findViewById(R.id.YourTicketsList);
        databaseReference = FirebaseDatabase.getInstance().getReference("LocalBookedTickets");

        tckdet = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        yourTicketsAdapter = new YourTicketsAdapter(this, tckdet);
        recyclerView.setAdapter(yourTicketsAdapter);

        String userID=UserNumber.getInstance().getNumber();

        databaseReference.orderByChild("UserID").equalTo(userID)  // "UserID" matches exactly
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        tckdet.clear(); // Clear previous list
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            TicketDetails tktdet = dataSnapshot.getValue(TicketDetails.class);
                            tckdet.add(tktdet);
                        }

                        yourTicketsAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Failed to load tickets", error.toException());
                    }
                });

    }
}
