package com.example.a61;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

public class UpgradeActivity extends AppCompatActivity {
    private PaymentsClient paymentsClient;
    private Button upgradeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrade_activity);

        paymentsClient = Wallet.getPaymentsClient(
                this,
                new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                        .build()
        );


        upgradeButton = findViewById(R.id.upgradeButton);
        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Google Pay payment process
                requestPayment();
            }
        });
    }

    private void requestPayment() {
        PaymentDataRequest request = buildPaymentDataRequest();
        Task<PaymentData> futurePaymentTask = paymentsClient.loadPaymentData(request);
        futurePaymentTask.addOnCompleteListener(this, new OnCompleteListener<PaymentData>() {
            @Override
            public void onComplete(@NonNull Task<PaymentData> task) {
                if (task.isSuccessful()) {
                    PaymentData paymentData = task.getResult();
                    // Handle successful payment response (for testing, you can log the paymentData)
                    Log.d("PaymentResponse", "onComplete: " + paymentData.toJson());
                } else {
                    // Handle payment failure or cancellation
                    Exception exception = task.getException();
                    if (exception != null) {
                        // Handle the exception (for testing, you can log the exception message)
                        Log.e("PaymentError", "onComplete: " + exception.getMessage());
                    } else {
                        // Payment was canceled
                        Log.d("PaymentResponse", "onComplete: Payment canceled");
                    }
                }
            }
        });
    }


    private void performUpgradeAction() {
        // Implement the logic for upgrading here
        // This could involve updating user preferences, database records, etc.
    }

    private PaymentDataRequest buildPaymentDataRequest() {
        PaymentDataRequest request = PaymentDataRequest.fromJson("{\n" +
                "  \"apiVersion\": 2,\n" +
                "  \"apiVersionMinor\": 0,\n" +
                "  \"allowedPaymentMethods\": [\n" +
                "    {\n" +
                "      \"type\": \"CARD\",\n" +
                "      \"parameters\": {\n" +
                "        \"allowedAuthMethods\": [\"PAN_ONLY\", \"CRYPTOGRAM_3DS\"],\n" +
                "        \"allowedCardNetworks\": [\"AMEX\", \"DISCOVER\", \"JCB\", \"MASTERCARD\", \"VISA\"]\n" +
                "      },\n" +
                "      \"tokenizationSpecification\": {\n" +
                "        \"type\": \"PAYMENT_GATEWAY\",\n" +
                "        \"parameters\": {\n" +
                "          \"gateway\": \"example\",\n" +
                "          \"gatewayMerchantId\": \"exampleGatewayMerchantId\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"transactionInfo\": {\n" +
                "    \"totalPrice\": \"10.00\",\n" +
                "    \"totalPriceStatus\": \"FINAL\",\n" +
                "    \"currencyCode\": \"USD\"\n" +
                "  },\n" +
                "  \"merchantInfo\": {\n" +
                "    \"merchantName\": \"Example Merchant\"\n" +
                "  }\n" +
                "}");

        return request;
    }

}
