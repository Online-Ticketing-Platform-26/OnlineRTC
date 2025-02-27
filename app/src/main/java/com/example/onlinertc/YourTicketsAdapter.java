package com.example.onlinertc;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;

public class YourTicketsAdapter extends RecyclerView.Adapter<YourTicketsAdapter.MyViewHolder> {
    Context context;
    ArrayList<TicketDetails> ticketDetails;
    public YourTicketsAdapter(Context context, ArrayList<TicketDetails> ticketDetails) {
        this.context = context;
        this.ticketDetails = ticketDetails;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ✅ Use the correct list item layout instead of activity layout
        View v = LayoutInflater.from(context).inflate(R.layout.ticket_card, parent, false);
        return new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int reversedPosition = ticketDetails.size() - 1 - position; // Reverse index
        TicketDetails tckdet = ticketDetails.get(reversedPosition);
        holder.UserID.setText(tckdet.getUserID());
        holder.boardingPoint.setText(tckdet.getBoardingPoint());
        holder.destinationPoint.setText(tckdet.getDestinationPoint());
        holder.Date.setText(tckdet.getDateTime());
        holder.uniqueID.setText(tckdet.getUID());
        holder.Status.setText(tckdet.isStatus() ? "Active" : "Inactive"); // ✅ Fix Boolean to String conversion
        holder.Price.setText(String.valueOf(tckdet.getFare()));
        holder.ShowQR.setChecked(false);
        holder.Vby.setText(tckdet.getValidatedBy());
        holder.VDate.setText(tckdet.getValidationTime());
        if (tckdet.isStatus()) {
            holder.QR.setImageResource(R.drawable.newtkt); // Active ticket
            holder.ShowQR.setVisibility(View.VISIBLE);

        } else {
            holder.QR.setImageResource(R.drawable.validated); // Used ticket
            holder.Vby.setText("Validated By \n"+tckdet.getValidatedBy());
            holder.VDate.setText("Validated On \n"+tckdet.getValidationTime());
        }
        holder.ShowQR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    if(tckdet.isStatus()){
                        holder.QRCODE.setVisibility(View.VISIBLE);
                    }else{
                        holder.Vby.setVisibility(View.VISIBLE);
                        holder.VDate.setVisibility(View.VISIBLE);
                    }
                }else{
                    holder.QRCODE.setVisibility(View.GONE);
                    holder.Vby.setVisibility(View.GONE);
                    holder.VDate.setVisibility(View.GONE);
                }
            }
        });
        String QR_data ;
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.encodeBitmap(tckdet.getUID(), BarcodeFormat.QR_CODE, 1200, 1200);
            holder.QRCODE.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return ticketDetails.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView UserID, boardingPoint, destinationPoint, Price, uniqueID, Date, Status,Vby,VDate;
        ImageView QR,QRCODE;
        Switch ShowQR;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            UserID = itemView.findViewById(R.id.mtUserNumber);
            boardingPoint = itemView.findViewById(R.id.mtBoarding);
            destinationPoint = itemView.findViewById(R.id.mtDestination);
            Price = itemView.findViewById(R.id.mtFare);
            Date = itemView.findViewById(R.id.mtDate);
            Status = itemView.findViewById(R.id.mtValid);
            uniqueID = itemView.findViewById(R.id.mtUniqueID);
            QR = itemView.findViewById(R.id.QR);
            QRCODE=itemView.findViewById(R.id.QRCODE);
            ShowQR=itemView.findViewById(R.id.showQR);
            Vby=itemView.findViewById(R.id.vby);
            VDate=itemView.findViewById(R.id.vdate);


            // ✅ Initialize QR ImageView

        }
    }
}
