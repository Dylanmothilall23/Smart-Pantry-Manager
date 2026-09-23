package com.example.smartpantry;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
//Matches avaaible imgrients in the pantry with the database receipe
public class SuggestedRecipesActivity extends AppCompatActivity {
    //UI componets
    private RecyclerView recyclerRecipes;
    private TextView txtNoRecipes;
    //DB
    private DatabaseHelper databaseHelper;
    //Filter data
    private final ArrayList<Integer> ids = new ArrayList<>();
    private final ArrayList<String> names = new ArrayList<>();
    private RecipeAdapter recipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        //UI view
        recyclerRecipes = findViewById(R.id.recyclerRecipes);
        txtNoRecipes = findViewById(R.id.txtNoRecipes);

        databaseHelper = new DatabaseHelper(this);
        recyclerRecipes.setLayoutManager(new LinearLayoutManager(this));
        //Evalulate and load receipes
        loadSuggestedRecipes();
    }
    //Quries all receipes from the database filters for receipe where all required
    private void loadSuggestedRecipes() {
        //clears previous
        ids.clear();
        names.clear();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor recipeCursor = db.rawQuery(
                "SELECT id, recipe_name FROM recipes",
                null
        );

        while (recipeCursor.moveToNext()) {
            int recipeId = recipeCursor.getInt(0);
            String recipeName = recipeCursor.getString(1);

            if (canMakeRecipe(db, recipeId)) {
                ids.add(recipeId);
                names.add(recipeName);
            }
        }

        recipeCursor.close();
        db.close();
        //Update Ui depending on matching recipes
        if (names.isEmpty()) {
            recyclerRecipes.setVisibility(RecyclerView.GONE);
            txtNoRecipes.setVisibility(TextView.VISIBLE);
        } else {
            recyclerRecipes.setVisibility(RecyclerView.VISIBLE);
            txtNoRecipes.setVisibility(TextView.GONE);

            recipeAdapter = new RecipeAdapter(this, ids, names);
            recyclerRecipes.setAdapter(recipeAdapter);
        }
    }
    //Checks if all ingridents required by a receipe existsin the pantry database
    private boolean canMakeRecipe(SQLiteDatabase db, int recipeId) {
        Cursor ingredientCursor = db.rawQuery(
                "SELECT ingredient_name, required_quantity, unit " +
                        "FROM recipe_ingredients WHERE recipe_id=?",
                new String[]{String.valueOf(recipeId)}
        );

        while (ingredientCursor.moveToNext()) {
            String requiredName = ingredientCursor.getString(0);
            double requiredQuantity = ingredientCursor.getDouble(1);
            String requiredUnit = ingredientCursor.getString(2);

            Cursor pantryCursor = db.rawQuery(
                    "SELECT ingredient_name, quantity, unit FROM pantry",
                    null
            );

            boolean ingredientFound = false;

            while (pantryCursor.moveToNext()) {
                String pantryName = pantryCursor.getString(0);
                double pantryQuantity = pantryCursor.getDouble(1);
                String pantryUnit = pantryCursor.getString(2);

                if (sameIngredient(pantryName, requiredName)) {
                    if (pantryUnit.equalsIgnoreCase(requiredUnit)
                            && pantryQuantity >= requiredQuantity) {
                        ingredientFound = true;
                        break;
                    }
                }
            }

            pantryCursor.close();
            //if any receipe cannot be found than alert
            if (!ingredientFound) {
                ingredientCursor.close();
                return false;
            }
        }

        ingredientCursor.close();
        return true;
    }
    //Compares two ingrident names after normalinze
    private boolean sameIngredient(String pantryName, String requiredName) {
        pantryName = normaliseIngredient(pantryName);
        requiredName = normaliseIngredient(requiredName);
        return pantryName.equals(requiredName);
    }
    //Puts strings into lowercase removes spaces and mapping common pural
    private String normaliseIngredient(String name) {
        name = name.trim().toLowerCase();

        if (name.equals("tomatoes")) return "tomato";
        if (name.equals("potatoes")) return "potato";
        if (name.equals("onions")) return "onion";
        if (name.equals("carrots")) return "carrot";
        if (name.equals("peas")) return "pea";
        if (name.equals("eggs")) return "egg";
        if (name.equals("beans")) return "bean";
        if (name.equals("wraps")) return "wrap";
        if (name.equals("breads")) return "bread";

        return name;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Refresh suggestions
        if (databaseHelper != null) {
            loadSuggestedRecipes();
        }
    }
}