package com.example.a61;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class UpgradeActivity extends AppCompatActivity {

    private static final String TAG = "PayPalActivity";
    private static final int PAYPAL_REQUEST_CODE = 123;

    private Button paymentButton1;
    private Button paymentButton2;
    private Button paymentButton3;
    private PayPalConfiguration paypalConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrade_activity);

        // Initialize PayPal configuration
        paypalConfig = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // Use sandbox for testing
                .clientId("Abezek8rUEH6BTeC9J5TMv0yJWynWF6KAmD8d9IGXsuzI9IUDDn-sSYwtYO89stdf3U7nw2u9iUbMVGU");

        paymentButton1 = findViewById(R.id.upgradeButton1);
        paymentButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment(5);
            }
        });

        paymentButton2 = findViewById(R.id.upgradeButton2);
        paymentButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment(10);
            }
        });

        paymentButton3 = findViewById(R.id.upgradeButton3);
        paymentButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePayment(20);
            }
        });
    }

    private void makePayment(int cost) {
        PayPalPayment payment = new PayPalPayment(
                new BigDecimal(cost), "USD", "Test Payment",
                PayPalPayment.PAYMENT_INTENT_SALE
        );

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, paypalConfig);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        Log.d(TAG, "onActivityResult: " + paymentDetails);
                        // Handle payment success
                        Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Log.e(TAG, "onActivityResult: " + e.getMessage());
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                // Payment was canceled by user
                Log.d(TAG, "onActivityResult: Payment canceled");
                Toast.makeText(this, "Payment canceled", Toast.LENGTH_SHORT).show();
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.e(TAG, "onActivityResult: Invalid payment");
                Toast.makeText(this, "Invalid payment", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }
}
