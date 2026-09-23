package com.example.smartpantry;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
//Activity for creating a new ingredient in the pantry
public class AddIngredientActivity extends AppCompatActivity {
    //UI
    private EditText edtIngredientName, edtQuantity, edtUnit, edtExpiryDate;
    private Button btnSaveIngredient;
    //Variables
    private DatabaseHelper databaseHelper;
    private int ingredientId = -1;
    private boolean editMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingredient);
        //UI Components
        edtIngredientName = findViewById(R.id.edtIngredientName);
        edtQuantity = findViewById(R.id.edtQuantity);
        edtUnit = findViewById(R.id.edtUnit);
        edtExpiryDate = findViewById(R.id.edtExpiryDate);
        btnSaveIngredient = findViewById(R.id.btnSaveIngredient);
        databaseHelper = new DatabaseHelper(this);
        //Check if ingriendients for edit mode
        ingredientId = getIntent().getIntExtra("ingredient_id", -1);
        if (ingredientId != -1) {
            editMode = true;
            edtIngredientName.setText(getIntent().getStringExtra("ingredient_name"));
            edtQuantity.setText(getIntent().getStringExtra("quantity"));
            edtUnit.setText(getIntent().getStringExtra("unit"));
            edtExpiryDate.setText(getIntent().getStringExtra("expiry_date"));
            btnSaveIngredient.setText("Update Ingredient");
        }

        btnSaveIngredient.setOnClickListener(v -> saveIngredient());
    }
    //Validates user inputs in SQLite
    private void saveIngredient() {
        String name = edtIngredientName.getText().toString().trim();
        String quantityText = edtQuantity.getText().toString().trim();
        String unit = edtUnit.getText().toString().trim();
        String expiryDate = edtExpiryDate.getText().toString().trim();
        //Checks
        if (name.isEmpty()) {
            showError(edtIngredientName, "Enter an ingredient name");
            return;
        }
        if (quantityText.isEmpty()) {
            showError(edtQuantity, "Enter a quantity");
            return;
        }
        if (unit.isEmpty()) {
            showError(edtUnit, "Enter a unit");
            return;
        }
        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (Exception e) {
            showError(edtQuantity, "Enter a valid quantity");
            return;
        }

        if (quantity <= 0) {
            showError(edtQuantity, "Quantity must be greater than 0");
            return;
        }
        //Prepares values for database
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ingredient_name", name);
        values.put("quantity", quantity);
        values.put("unit", unit);
        values.put("expiry_date", expiryDate);

        if (editMode) {
            int result = db.update("pantry", values, "id=?", new String[]{String.valueOf(ingredientId)});
            db.close();

            if (result > 0) {
                Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update ingredient", Toast.LENGTH_SHORT).show();
            }
        } else {
            long result = db.insert("pantry", null, values);
            db.close();

            if (result != -1) {
                Toast.makeText(this, "Ingredient saved", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to save ingredient", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //Highlights an error
    private void showError(EditText field, String message) {
        field.setError(message);
        field.requestFocus();
    }
}