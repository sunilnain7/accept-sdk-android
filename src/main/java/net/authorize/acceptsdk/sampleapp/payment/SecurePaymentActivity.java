package net.authorize.acceptsdk.sampleapp.payment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import net.authorize.acceptsdk.R;
import net.authorize.acceptsdk.security.PaymentSecurityUtil;

/**
 * Sample Payment Activity demonstrating secure payment form implementation.
 * 
 * This Activity shows best practices for handling sensitive payment data:
 * - Uses PaymentSecurityUtil to enable FLAG_SECURE
 * - Implements the fixed fragment_accept.xml layout
 * - Properly masks sensitive input fields
 * - Prevents screenshots and screen recording
 */
public class SecurePaymentActivity extends Activity {
    
    private EditText cardNumberView;
    private EditText expiryDateView;
    private EditText securityCodeView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // SECURITY: Apply FLAG_SECURE BEFORE setContentView()
        // This prevents screenshots and screen recording
        PaymentSecurityUtil.protectActivity(this);
        
        setContentView(R.layout.fragment_accept);
        
        initializeViews();
        setupListeners();
    }
    
    /**
     * Initialize view references
     */
    private void initializeViews() {
        cardNumberView = findViewById(R.id.card_number_view);
        expiryDateView = findViewById(R.id.expiry_date_view);
        securityCodeView = findViewById(R.id.security_code_view);
        
        // All fields now have android:importantForAutofill="no"
        // and sensitive fields use android:inputType="numberPassword"
    }
    
    /**
     * Setup input validation listeners
     */
    private void setupListeners() {
        // Add text watchers for validation
        // The CVV field (securityCodeView) now automatically masks input
        // while using numberPassword input type
    }
    
    /**
     * Safely handle the payment submission
     */
    private void submitPayment() {
        String cardNumber = cardNumberView.getText().toString();
        String expiryDate = expiryDateView.getText().toString();
        String cvv = securityCodeView.getText().toString();
        
        // SECURITY: Never log card data or CVV
        // SECURITY: Clear sensitive data from memory after use
        // SECURITY: Process payment securely
        
        // Clear sensitive views
        clearSensitiveData();
    }
    
    /**
     * Clear sensitive data from UI
     */
    private void clearSensitiveData() {
        if (cardNumberView != null) {
            cardNumberView.setText("");
        }
        if (expiryDateView != null) {
            expiryDateView.setText("");
        }
        if (securityCodeView != null) {
            securityCodeView.setText("");
        }
    }
    
    @Override
    protected void onPause() {
        // Clear sensitive data when leaving the activity
        clearSensitiveData();
        super.onPause();
    }
    
    @Override
    protected void onDestroy() {
        // Optional: Remove FLAG_SECURE when completely done with payment
        PaymentSecurityUtil.unprotectActivity(this);
        clearSensitiveData();
        super.onDestroy();
    }
}
