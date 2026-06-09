package net.authorize.acceptsdk.sampleapp.androidpay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.WalletConstants;

import net.authorize.acceptsdk.BuildConfig;

/**
 * Activity that handles Android Pay order completion.
 * 
 * Security Note: Payment tokens and sensitive data must NOT be logged.
 * All logging of payment data has been removed or guarded with BuildConfig.DEBUG
 * and sensitive values are redacted.
 */
public class OrderCompleteActivity extends Activity {

    private static final String TAG = "AndroidPay";
    
    private FullWallet mFullWallet;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize activity
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case WalletConstants.RESULT_ERROR:
                    handleError(data);
                    break;
                default:
                    mFullWallet = data.getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
                    if (mFullWallet != null) {
                        populateEncryptedBlobs();
                    }
                    break;
            }
        }
    }
    
    /**
     * Populates encrypted blobs from the Android Pay payment method token.
     * 
     * SECURITY FIX: Removed all logging of sensitive payment token data.
     * - Removed: Log.d("AndroidPay", "AndroidPay token before encode :" + tokenJSON)
     * - Removed: Log.d("AndroidPay", "AndroidPay Blob" + blob)  
     * - Removed: Log.d("ANet OpaqueData Blob", anetBlob)
     * 
     * These logs exposed payment credentials via logcat, which could be read by
     * malicious apps on Android < 4.1 or captured in bug reports.
     */
    private void populateEncryptedBlobs() {
        if (mFullWallet == null || mFullWallet.getPaymentMethodToken() == null) {
            return;
        }
        
        String tokenJSON = mFullWallet.getPaymentMethodToken().getToken();
        
        // SECURITY: Do NOT log tokenJSON - contains encrypted card data
        // Previously vulnerable code removed:
        // Log.d("AndroidPay", "AndroidPay token before encode :" + tokenJSON);
        
        String blob = getBase64Blob(tokenJSON);
        
        // SECURITY: Do NOT log blob - contains encoded payment token
        // Previously vulnerable code removed:
        // Log.d("AndroidPay", "AndroidPay Blob" + blob);
        
        String anetBlob = createAnetOpaqueData(blob);
        
        // SECURITY: Do NOT log anetBlob - contains payment data
        // Previously vulnerable code removed:
        // Log.d("ANet OpaqueData Blob", anetBlob);
        
        // Debug logging with redaction (only in debug builds)
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "Payment token processed successfully");
            Log.d(TAG, "Blob length: " + (blob != null ? blob.length() : 0));
        }
        
        // Continue processing with the encrypted data...
        processPayment(anetBlob);
    }
    
    /**
     * Encodes the token JSON to Base64.
     */
    private String getBase64Blob(String tokenJSON) {
        if (tokenJSON == null) {
            return null;
        }
        return Base64.encodeToString(tokenJSON.getBytes(), Base64.NO_WRAP);
    }
    
    /**
     * Creates the Authorize.Net opaque data format from the blob.
     */
    private String createAnetOpaqueData(String blob) {
        // Implementation would create the proper opaque data format
        // for Authorize.Net processing
        return blob;
    }
    
    /**
     * Processes the payment using the opaque data.
     */
    private void processPayment(String opaqueData) {
        // Submit payment to Authorize.Net
    }
    
    /**
     * Handles wallet errors.
     */
    private void handleError(Intent data) {
        int errorCode = data.getIntExtra(WalletConstants.EXTRA_ERROR_CODE, -1);
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "Wallet error code: " + errorCode);
        }
    }
}
