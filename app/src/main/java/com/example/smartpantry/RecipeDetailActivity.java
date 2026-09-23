package com.example.smartpantry;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
// displaying comeplete details of a receipe
public class RecipeDetailActivity extends AppCompatActivity {
    //UI componets
    private TextView txtRecipeName;
    private TextView txtIngredients;
    private TextView txtMethod;
    //DB
    private DatabaseHelper databaseHelper;
    private int recipeId;
    //Fecting and displaying details
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        txtRecipeName = findViewById(R.id.txtRecipeName);
        txtIngredients = findViewById(R.id.txtIngredients);
        txtMethod = findViewById(R.id.txtMethod);

        databaseHelper = new DatabaseHelper(this);
        recipeId = getIntent().getIntExtra("recipe_id", -1);

        loadRecipe();
    }
    //Query recipe
    private void loadRecipe() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor recipeCursor = db.rawQuery(
                "SELECT recipe_name, method FROM recipes WHERE id=?",
                new String[]{String.valueOf(recipeId)}
        );

        if (recipeCursor.moveToFirst()) {
            String recipeName = recipeCursor.getString(0);
            String method = recipeCursor.getString(1);

            txtRecipeName.setText(recipeName);
            txtMethod.setText(method);
        }
        recipeCursor.close();

        Cursor ingredientCursor = db.rawQuery(
                "SELECT ingredient_name, required_quantity, unit " +
                        "FROM recipe_ingredients WHERE recipe_id=?",
                new String[]{String.valueOf(recipeId)}
        );
        //Format ingriendts
        StringBuilder ingredients = new StringBuilder();
        while (ingredientCursor.moveToNext()) {
            String name = ingredientCursor.getString(0);
            String quantity = ingredientCursor.getString(1);
            String unit = ingredientCursor.getString(2);

            ingredients.append(name).append(" - ").append(quantity).append(" ").append(unit).append("\n");
        }

        ingredientCursor.close();
        db.close();
        //Setting the text
        txtIngredients.setText(ingredients.toString().trim());
    }
}