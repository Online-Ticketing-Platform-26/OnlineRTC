package com.example.onlinertc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTPVERIFY extends AppCompatActivity {
    private EditText otpnum1, otpnum2, otpnum3, otpnum4, otpnum5, otpnum6;
    Button otpverify;
    FirebaseAuth mAuth;
    private String verificationId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverify);

        TextView mobilenum = findViewById(R.id.mobilenum);
        mobilenum.setText(String.format(
                "+91-%s" ,getIntent().getStringExtra("mobile")
        ));

        otpnum1 = findViewById(R.id.otpnum1);
        otpnum2 = findViewById(R.id.otpnum2);
        otpnum3 = findViewById(R.id.otpnum3);
        otpnum4 = findViewById(R.id.otpnum4);
        otpnum5 = findViewById(R.id.otpnum5);
        otpnum6 = findViewById(R.id.otpnum6);

        setupOTPinputs();
        final ProgressBar progressbar = findViewById(R.id.progressbar);
        final Button otpverify = findViewById(R.id.otpverify);
        verificationId= getIntent().getStringExtra("verificationId");
        otpverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otpnum1.getText().toString().trim().isEmpty()
                        ||otpnum2.getText().toString().trim().isEmpty()
                        ||otpnum3.getText().toString().trim().isEmpty()
                        ||otpnum4.getText().toString().trim().isEmpty()
                        ||otpnum5.getText().toString().trim().isEmpty()
                        ||otpnum6.getText().toString().trim().isEmpty()
                ){
                    Toast.makeText(OTPVERIFY.this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
                    return;

                }
                String code=
                        otpnum1.getText().toString()+
                                otpnum2.getText().toString()+
                                otpnum3.getText().toString()+
                                otpnum4.getText().toString()+
                                otpnum5.getText().toString()+
                                otpnum6.getText().toString();

                if(verificationId != null){
                    progressbar.setVisibility(View.VISIBLE);
                    otpverify.setVisibility(View.INVISIBLE);
                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                            verificationId,
                            code
                    );
                    FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressbar.setVisibility(View.GONE);
                                    otpverify.setVisibility(View.VISIBLE);
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(getApplicationContext(),TKT.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(OTPVERIFY.this, "This verification code is invalid", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });






    }

    private void setupOTPinputs(){
        otpnum1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    otpnum2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otpnum2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    otpnum3.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otpnum3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    otpnum4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otpnum4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    otpnum5.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        otpnum5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    otpnum6.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}