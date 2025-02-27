package com.example.onlinertc;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import kotlinx.coroutines.MainCoroutineDispatcher;

public class TKT extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tkt);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.darkbg));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TextView user=findViewById(R.id.user);
        user.setText("Hello, "+UserNumber.getInstance().getNumber());
        ImageButton swap1=findViewById(R.id.swap);
        RadioButton radioLocal, radioDomestic;
        RadioGroup toggleGroup;
        radioLocal = findViewById(R.id.local);
        radioDomestic = findViewById(R.id.domestic);
        toggleGroup = findViewById(R.id.toggle);
        ImageButton b_edit=findViewById(R.id.bedit);
        ImageButton d_edit=findViewById(R.id.dedit);
        RadioButton oneWay,twoWay;
        RadioGroup tktType;

        tktType=findViewById(R.id.tktType);
        oneWay=findViewById(R.id.onewaytkt);
        twoWay=findViewById(R.id.twowaytkt);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        DatabaseReference TicketDetails = database.getReference("LocalBookedTickets");
        String UID;
        // Fetch prices from API or backend
        String onep="35";
        String twop="70";
        int TicketPrice;
        String priceOneWay = "₹"+onep;
        String priceTwoWay = "₹"+twop;
        AppCompatButton BookTicket=findViewById(R.id.booktkt);
        TicketDetails myTicket=new TicketDetails();
        Button his=findViewById(R.id.history);
        his.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TKT.this,YourBookings.class));
            }
        });




        // Format text for One Way
        oneWay.setText(formatTicketText("One Way", priceOneWay));

        // Format text for Two Way
        twoWay.setText(formatTicketText("Two Way", priceTwoWay));

        toggleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.local) {
                radioLocal.setBackgroundResource(R.drawable.toggle_selected);
                radioLocal.setTextColor(Color.WHITE);

                radioDomestic.setBackgroundResource(R.drawable.toggle_unselected);
                radioDomestic.setTextColor(Color.parseColor("#3E676A"));
            } else {
                radioDomestic.setBackgroundResource(R.drawable.toggle_selected);
                radioDomestic.setTextColor(Color.WHITE);

                radioLocal.setBackgroundResource(R.drawable.toggle_unselected);
                radioLocal.setTextColor(Color.parseColor("#3E676A"));
            }
        });
        tktType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.onewaytkt) {
                oneWay.setBackgroundResource(R.drawable.oneway);
                oneWay.setTextColor(Color.parseColor("#3E676A"));
                myTicket.setFare(onep);

                twoWay.setBackgroundResource(R.drawable.twoway);
                twoWay.setTextColor(Color.parseColor("#6F6F6F"));
            } else {
                twoWay.setBackgroundResource(R.drawable.oneway);
                twoWay.setTextColor(Color.parseColor("#3E676A"));
                myTicket.setFare(twop);

                oneWay.setBackgroundResource(R.drawable.twoway);
                oneWay.setTextColor(Color.parseColor("#6F6F6F"));
            }
        });
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        AutoCompleteTextView autoCompleteTextView1 = findViewById(R.id.autoCompleteTextView1);
        // Sample list of boarding points
        //String[] boardingPoints = {"Hyderabad", "Secunderabad", "Miyapur", "Ameerpet", "Begumpet", "Kukatpally","KPHB"};
        // Set up ArrayAdapter
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.dropdown_items, R.layout.spinner_dropdown_item);
        autoCompleteTextView.setAdapter(adapter);
        // Handle item selection
        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedBoardingPoint = (String) parent.getItemAtPosition(position);

            if(!selectedBoardingPoint.isEmpty()){
                autoCompleteTextView.setEnabled(false);
                if(!autoCompleteTextView.isEnabled()){

                    b_edit.setVisibility(View.VISIBLE);
                    b_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            autoCompleteTextView.setEnabled(true);
                            autoCompleteTextView.requestFocus();
                            b_edit.setVisibility(View.GONE);
                        }
                    });
                }
            }else{
                autoCompleteTextView.setEnabled(true);
            }
            Toast.makeText(TKT.this, "Your Boarding Point "+selectedBoardingPoint + " is selected", Toast.LENGTH_SHORT).show();
        });


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.dropdown_items, R.layout.spinner_dropdown_item);
        autoCompleteTextView1.setAdapter(adapter1);
        autoCompleteTextView1.setOnItemClickListener((parent, view, position, id) -> {
            String selectedDestinationPoint = (String) parent.getItemAtPosition(position);



            if(!selectedDestinationPoint.isEmpty()){
                autoCompleteTextView1.setEnabled(false);
                tktType.setVisibility(View.VISIBLE);
                if(!autoCompleteTextView1.isEnabled()){
                    d_edit.setVisibility(View.VISIBLE);
                    d_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            autoCompleteTextView1.setEnabled(true);
                            autoCompleteTextView1.requestFocus();
                            d_edit.setVisibility(View.GONE);
                        }
                    });
                }

            }else{
                autoCompleteTextView1.setEnabled(true);
            }
            Toast.makeText(TKT.this, "Your Destination Point "+selectedDestinationPoint + " is selected", Toast.LENGTH_SHORT).show();
        });


        swap1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Boarding=autoCompleteTextView.getText().toString();
                String Destination=autoCompleteTextView1.getText().toString();
                autoCompleteTextView.setText(Destination);
                autoCompleteTextView1.setText(Boarding);
            }
        });


        BookTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookTicket.setEnabled(false);
                BookTicket.setText("Hold on! Ticket is loading...\uD83D\uDE80");


                String[] dropdownArray = getResources().getStringArray(R.array.dropdown_items);
                List<String> bpoints = Arrays.asList(dropdownArray);

                String[] dropdownArray1 = getResources().getStringArray(R.array.Destinations);
                List<String> dpoints = Arrays.asList(dropdownArray1);

                String User=UserNumber.getInstance().getNumber();
                if(!User.equals("")){
                    if(autoCompleteTextView.getText().toString().equals("") || !bpoints.contains(autoCompleteTextView.getText().toString())){
                        Toast.makeText(TKT.this,"Please Select Boarding Point",Toast.LENGTH_SHORT).show();

                    } else if ( autoCompleteTextView1.getText().toString().equals("") || !dpoints.contains(autoCompleteTextView1.getText().toString())) {
                        Toast.makeText(TKT.this,"Please Select Destination Point",Toast.LENGTH_SHORT).show();
                    } else{
                        Date date=new Date();

                        String UserID=UserNumber.getInstance().getNumber();
                        String Boarding=autoCompleteTextView.getText().toString();
                        String Destination=autoCompleteTextView1.getText().toString();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd / MM / yyyy", Locale.getDefault());
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

                        String Date=dateFormat.format(date) + "\n" + timeFormat.format(date) + " (24 Hours)";
                        myTicket.setUserID(UserID);
                        myTicket.setBoardingPoint(Boarding);
                        myTicket.setDestinationPoint(Destination);
                        String Fare=myTicket.getFare();
                        boolean status =true;


                        HashMap<String, Object> ticketData = new HashMap<>();
                        ticketData.put("UserID", UserID);
                        ticketData.put("BoardingPoint", Boarding);
                        ticketData.put("DestinationPoint", Destination);
                        ticketData.put("Fare", Fare);
                        String uniqueId = UUID.randomUUID().toString();
                        ticketData.put("UID",uniqueId);
                        ticketData.put("Status",status);
                        ticketData.put("DateTime",Date);
                        ticketData.put("ValidationTime","");
                        ticketData.put("ValidatedBy","");





                        String key=TicketDetails.push().getKey();

                        if(Fare!="0"){
                            TicketDetails.child(key).setValue(ticketData);
                            Toast.makeText(TKT.this,uniqueId+" is UID",Toast.LENGTH_SHORT).show();
                            new android.os.Handler().postDelayed(() -> {
                            Intent intent = new Intent(TKT.this, BookedTicket.class);
                            intent.putExtra("UserID", UserID);
                            intent.putExtra("BoardingPoint", Boarding);
                            intent.putExtra("DestinationPoint", Destination);
                            intent.putExtra("Fare", Fare);
                            intent.putExtra("Status", status);
                            intent.putExtra("UID",uniqueId);
                            intent.putExtra("Date",Date);

                                BookTicket.setEnabled(true);
                                BookTicket.setText("Booking Your Ticket...");
                                startActivity(intent);
                                finish();
                                autoCompleteTextView.getText().clear();
                                autoCompleteTextView1.getText().clear();
                                autoCompleteTextView1.setEnabled(true);
                                autoCompleteTextView.setEnabled(true);
                                tktType.setVisibility(View.GONE);
                                b_edit.setVisibility(View.GONE);
                                d_edit.setVisibility(View.GONE);
                            }, 3000);



                        }else{
                            Toast.makeText(TKT.this,"Please select ticket type.",Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else{
                    startActivity(new Intent(TKT.this,MainActivity.class));
                    finish();
                }


                //FirebaseFirestore db = FirebaseFirestore.getInstance();


            }
        });

    }
    private SpannableString formatTicketText(String label, String price) {
        String fullText = label + "\n" + price;
        SpannableString spannable = new SpannableString(fullText);

        // Set "One Way" or "Two Way" smaller
        spannable.setSpan(new AbsoluteSizeSpan(18, true), 0, label.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new StyleSpan(Typeface.NORMAL), 0, label.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set Price bigger
        spannable.setSpan(new AbsoluteSizeSpan(50, true), label.length() + 1, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new StyleSpan(Typeface.BOLD), label.length() + 1, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }


}