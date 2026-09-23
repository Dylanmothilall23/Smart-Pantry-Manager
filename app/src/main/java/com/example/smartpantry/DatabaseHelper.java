package com.example.smartpantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    //SQLite Database -> manages the function it provides the pantry with
    private static final String DATABASE_NAME = "smartpantry.db";
    private static final int DATABASE_VERSION = 2;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creates pantry table to track available user ingridents
        db.execSQL("CREATE TABLE pantry (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ingredient_name TEXT NOT NULL, " +
                "quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL, " +
                "expiry_date TEXT)");

        db.execSQL("CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recipe_name TEXT NOT NULL, " +
                "method TEXT NOT NULL)");

        db.execSQL("CREATE TABLE recipe_ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recipe_id INTEGER NOT NULL, " +
                "ingredient_name TEXT NOT NULL, " +
                "required_quantity REAL NOT NULL, " +
                "unit TEXT NOT NULL, " +
                "FOREIGN KEY(recipe_id) REFERENCES recipes(id))");
        //Seed default
        insertRecipes(db);
    }
    //Seeds the database with default recipes
    private void insertRecipes(SQLiteDatabase db) {
        addRecipe(db, "Chicken Pasta",
                "Boil pasta. Cook chicken in a pan. Add tomato and onion. Mix with the cooked pasta and serve.",
                new String[]{"Chicken", "Pasta", "Tomato", "Onion"},
                new double[]{200, 200, 1, 1},
                new String[]{"g", "g", "units", "units"});

        addRecipe(db, "Chicken Rice",
                "Cook the rice. Cook the chicken with onion and garlic. Mix together and serve.",
                new String[]{"Chicken", "Rice", "Onion", "Garlic"},
                new double[]{200, 200, 1, 1},
                new String[]{"g", "g", "units", "units"});

        addRecipe(db, "Beef Stir Fry",
                "Slice the beef and vegetables. Fry the beef, then add the vegetables and soy sauce. Cook until ready.",
                new String[]{"Beef", "Carrot", "Onion", "Soy Sauce"},
                new double[]{200, 1, 1, 20},
                new String[]{"g", "units", "units", "ml"});

        addRecipe(db, "Vegetable Rice",
                "Cook the rice. Fry the vegetables with onion. Add the cooked rice and mix.",
                new String[]{"Rice", "Carrot", "Peas", "Onion"},
                new double[]{200, 1, 100, 1},
                new String[]{"g", "units", "g", "units"});

        addRecipe(db, "Tomato Pasta",
                "Cook the pasta. Cook tomato and onion in a pan. Add the pasta and mix well.",
                new String[]{"Pasta", "Tomato", "Onion"},
                new double[]{200, 2, 1},
                new String[]{"g", "units", "units"});

        addRecipe(db, "Egg Fried Rice",
                "Cook the rice. Scramble the eggs. Add rice, peas and onion and stir fry.",
                new String[]{"Rice", "Egg", "Peas", "Onion"},
                new double[]{200, 2, 100, 1},
                new String[]{"g", "units", "g", "units"});

        addRecipe(db, "Chicken Sandwich",
                "Cook the chicken. Place chicken, lettuce and tomato between two slices of bread.",
                new String[]{"Chicken", "Bread", "Lettuce", "Tomato"},
                new double[]{150, 2, 50, 1},
                new String[]{"g", "slices", "g", "units"});

        addRecipe(db, "Cheese Omelette",
                "Beat the eggs. Cook in a pan and add cheese. Fold and serve.",
                new String[]{"Egg", "Cheese", "Milk"},
                new double[]{3, 50, 30},
                new String[]{"units", "g", "ml"});

        addRecipe(db, "Chicken Curry",
                "Cook onion and garlic. Add chicken and curry powder. Add tomato and cook until the chicken is ready.",
                new String[]{"Chicken", "Onion", "Garlic", "Tomato", "Curry Powder"},
                new double[]{250, 1, 1, 2, 10},
                new String[]{"g", "units", "units", "units", "g"});

        addRecipe(db, "Beef Pasta",
                "Cook the pasta. Cook the beef with onion and tomato. Mix with pasta and serve.",
                new String[]{"Beef", "Pasta", "Onion", "Tomato"},
                new double[]{200, 200, 1, 1},
                new String[]{"g", "g", "units", "units"});

        addRecipe(db, "Tuna Sandwich",
                "Mix tuna with mayonnaise. Place tuna, lettuce and tomato between bread slices.",
                new String[]{"Tuna", "Bread", "Mayonnaise", "Lettuce"},
                new double[]{100, 2, 20, 50},
                new String[]{"g", "slices", "g", "g"});

        addRecipe(db, "Vegetable Omelette",
                "Beat the eggs. Add chopped tomato, onion and cheese. Cook in a pan until done.",
                new String[]{"Egg", "Tomato", "Onion", "Cheese"},
                new double[]{3, 1, 1, 50},
                new String[]{"units", "units", "units", "g"});

        addRecipe(db, "Chicken Salad",
                "Cook the chicken. Mix chicken with lettuce, tomato, cucumber and mayonnaise.",
                new String[]{"Chicken", "Lettuce", "Tomato", "Cucumber", "Mayonnaise"},
                new double[]{150, 50, 1, 1, 20},
                new String[]{"g", "g", "units", "units", "g"});

        addRecipe(db, "Beef Burger",
                "Cook the beef patty. Place it on bread with lettuce, tomato and cheese.",
                new String[]{"Beef", "Bread", "Lettuce", "Tomato", "Cheese"},
                new double[]{200, 1, 30, 1, 30},
                new String[]{"g", "units", "g", "units", "g"});

        addRecipe(db, "Tomato Omelette",
                "Beat the eggs. Add chopped tomato and onion. Cook in a pan until firm.",
                new String[]{"Egg", "Tomato", "Onion"},
                new double[]{3, 1, 1},
                new String[]{"units", "units", "units"});

        addRecipe(db, "Chicken Wrap",
                "Cook the chicken. Place chicken, lettuce, tomato and cheese inside the wrap and roll.",
                new String[]{"Chicken", "Wrap", "Lettuce", "Tomato", "Cheese"},
                new double[]{150, 1, 30, 1, 30},
                new String[]{"g", "units", "g", "units", "g"});

        addRecipe(db, "Garlic Butter Pasta",
                "Cook the pasta. Melt butter and cook garlic. Add pasta and mix well.",
                new String[]{"Pasta", "Garlic", "Butter"},
                new double[]{200, 2, 30},
                new String[]{"g", "units", "g"});

        addRecipe(db, "Rice and Beans",
                "Cook the rice. Heat the beans with onion and tomato. Serve together.",
                new String[]{"Rice", "Beans", "Onion", "Tomato"},
                new double[]{200, 150, 1, 1},
                new String[]{"g", "g", "units", "units"});

        addRecipe(db, "Chicken and Potatoes",
                "Cut the potatoes and chicken. Season and cook together until both are fully cooked.",
                new String[]{"Chicken", "Potato", "Onion"},
                new double[]{250, 2, 1},
                new String[]{"g", "units", "units"});

        addRecipe(db, "Cheese Pasta",
                "Cook the pasta. Heat milk and cheese together. Add pasta and mix until creamy.",
                new String[]{"Pasta", "Cheese", "Milk", "Butter"},
                new double[]{200, 80, 100, 20},
                new String[]{"g", "g", "ml", "g"});
    }
    //insert a receipe and maps it ingridents
    private void addRecipe(SQLiteDatabase db, String name, String method,
                           String[] ingredients, double[] quantities, String[] units) {
        ContentValues recipeValues = new ContentValues();
        recipeValues.put("recipe_name", name);
        recipeValues.put("method", method);

        long recipeId = db.insert("recipes", null, recipeValues);
        //Linked to recipeID
        for (int i = 0; i < ingredients.length; i++) {
            ContentValues ingredientValues = new ContentValues();
            ingredientValues.put("recipe_id", recipeId);
            ingredientValues.put("ingredient_name", ingredients[i]);
            ingredientValues.put("required_quantity", quantities[i]);
            ingredientValues.put("unit", units[i]);

            db.insert("recipe_ingredients", null, ingredientValues);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drops old tables on database version
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS pantry");
        onCreate(db);
    }
}