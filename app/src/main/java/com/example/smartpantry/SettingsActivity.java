package com.example.smartpantry;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
// Managing user prefernece and app settings
public class SettingsActivity extends AppCompatActivity {
    // UI
    private Switch switchExpiryAlerts;
    //Storage
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //UI view
        switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);

        preferences = getSharedPreferences("SmartPantrySettings", MODE_PRIVATE);

        boolean expiryAlerts = preferences.getBoolean("expiry_alerts", true);
        switchExpiryAlerts.setChecked(expiryAlerts);

        switchExpiryAlerts.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferences.edit()
                    .putBoolean("expiry_alerts", isChecked)
                    .apply();
            // Notify user if change
            if (isChecked) {
                Toast.makeText(this, "Expiry alerts enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Expiry alerts disabled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}