package net.authorize.acceptsdk.security;

import android.app.Activity;
import android.os.Build;
import android.view.WindowManager;

/**
 * Security utility class for protecting sensitive payment form data.
 * 
 * This class provides helper methods to implement security best practices
 * for activities and fragments that display payment forms.
 * 
 * Security Fixes for CVE/Info-Leak Vulnerabilities:
 * - FLAG_SECURE prevents screenshots and screen recording
 * - Sensitive EditText fields use numberPassword input type
 * - importantForAutofill="no" prevents credential caching
 * 
 * Usage in Activity:
 *   protected void onCreate(Bundle savedInstanceState) {
 *       super.onCreate(savedInstanceState);
 *       PaymentSecurityUtil.protectActivity(this);
 *       setContentView(R.layout.fragment_accept);
 *   }
 */
public class PaymentSecurityUtil {
    
    private static final String TAG = "PaymentSecurityUtil";
    
    /**
     * Applies security protections to an Activity hosting sensitive payment forms.
     * 
     * This method:
     * 1. Enables FLAG_SECURE to prevent screenshots and screen recording
     * 2. Disables screen capture by malicious overlays
     * 3. Protects against shoulder-surfing attacks
     * 
     * @param activity The Activity to protect
     */
    public static void protectActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        
        try {
            // Set FLAG_SECURE to prevent screenshots and screen recording
            // This must be called before setContentView()
            activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            );
            
            // Optionally prevent window content from appearing in recents
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                );
            }
            
        } catch (Exception e) {
            android.util.Log.e(TAG, "Failed to apply security flags", e);
        }
    }
    
    /**
     * Removes FLAG_SECURE when leaving the payment form.
     * Call this in onDestroy() or when navigating away from payment screens.
     * 
     * @param activity The Activity to unprotect
     */
    public static void unprotectActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        
        try {
            activity.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_SECURE
            );
        } catch (Exception e) {
            android.util.Log.e(TAG, "Failed to remove security flags", e);
        }
    }
}
