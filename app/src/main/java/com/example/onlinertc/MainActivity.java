package com.example.onlinertc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";  // Added TAG for logging
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.darkbg));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        TextView bypass = findViewById(R.id.bypass);
        EditText otp = findViewById(R.id.otp);
        AppCompatButton login = findViewById(R.id.login1);
        otp.setVisibility(View.GONE);
        login.setVisibility(View.GONE);

        Space space1 = findViewById(R.id.space1);
        EditText mobileno = findViewById(R.id.mobileno);
        AppCompatButton getotp = findViewById(R.id.getotp1);
        TextView textView = findViewById(R.id.textView2);

        // Skip login for testing purposes (remove later)
        bypass.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, TKT.class)));

        otp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (otp.getText().length() == 6) {
                    adjustSpaceWeight(space1, 1);
                    hideKeyboard();
                }
            }
        });

        mobileno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (mobileno.getText().length() == 10) {
                    adjustSpaceWeight(space1, 1);
                    hideKeyboard();
                }
            }
        });

        getotp.setOnClickListener(view -> {
            String mobileNumber = mobileno.getText().toString().trim();
            UserNumber.getInstance().setNumber(mobileNumber);
            if (isValidMobile(mobileNumber)) {
                sendOtp(mobileNumber);
                mobileno.setVisibility(View.GONE);
                getotp.setVisibility(View.GONE);
                textView.setText("Enter OTP");
                otp.setVisibility(View.VISIBLE);
                login.setVisibility(View.VISIBLE);

                Toast.makeText(MainActivity.this, "OTP sent to " + mobileNumber, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Invalid Mobile Number", Toast.LENGTH_SHORT).show();
                textView.setText("Enter Mobile Number");
            }
        });

        login.setOnClickListener(view -> {
            String enteredOtp = otp.getText().toString().trim();
            if (enteredOtp.equals("123456")) {  // Dummy OTP for testing
                startActivity(new Intent(MainActivity.this, TKT.class));
            } else {
                Toast.makeText(MainActivity.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialize Phone Authentication Callbacks
        initCallbacks();
    }

    private void sendOtp(String mobileNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber("+91" + mobileNumber) // Ensure correct format
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void initCallbacks() {
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted: " + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(MainActivity.this, "Invalid request", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Quota exceeded", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent: " + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");
                        startActivity(new Intent(MainActivity.this, TKT.class));
                        finish();
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean isValidMobile(String number) {
        return number.length() == 10 && number.matches("\\d+");
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void adjustSpaceWeight(Space space, int weight) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) space.getLayoutParams();
        params.weight = weight;
        space.setLayoutParams(params);
    }
}
