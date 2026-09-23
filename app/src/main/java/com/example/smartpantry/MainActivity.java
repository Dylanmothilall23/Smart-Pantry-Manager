package com.example.smartpantry;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
//Main dashboard for the smart pantry displays info using a RecyclerView and provides acccess to other parts of the app
public class MainActivity extends AppCompatActivity {
    //UI navigation
    private Button btnAddIngredient;
    private Button btnPantry;
    private Button btnRecipes;
    private Button btnSettings;
    //UI list
    private RecyclerView recyclerPantry;
    private TextView txtEmpty;
    //DB
    private DatabaseHelper databaseHelper;
    //Data Storage in arrays
    private final ArrayList<Integer> ids = new ArrayList<>();
    private final ArrayList<String> names = new ArrayList<>();
    private final ArrayList<String> quantities = new ArrayList<>();
    private final ArrayList<String> units = new ArrayList<>();
    private final ArrayList<String> expiryDates = new ArrayList<>();
    //Recycler View
    private PantryAdapter pantryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //UI
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnPantry = findViewById(R.id.btnPantry);
        btnRecipes = findViewById(R.id.btnRecipes);
        btnSettings = findViewById(R.id.btnSettings);

        recyclerPantry = findViewById(R.id.recyclerPantry);
        txtEmpty = findViewById(R.id.txtEmpty);
        //Database and RecycleerView
        databaseHelper = new DatabaseHelper(this);
        recyclerPantry.setLayoutManager(new LinearLayoutManager(this));
        //Navigation onlick listners
        btnAddIngredient.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddIngredientActivity.class);
            startActivity(intent);
        });

        btnPantry.setOnClickListener(v -> loadPantry());

        btnRecipes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SuggestedRecipesActivity.class);
            startActivity(intent);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        loadPantry();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Refresh pantry list
        loadPantry();
    }
    //Queries the database for all pantry items and displays if they exist
    private void loadPantry() {
        ids.clear();
        names.clear();
        quantities.clear();
        units.clear();
        expiryDates.clear();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, ingredient_name, quantity, unit, expiry_date " +
                        "FROM pantry ORDER BY id DESC",
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String quantity = cursor.getString(2);
            String unit = cursor.getString(3);
            String expiry = cursor.getString(4);

            ids.add(id);
            names.add(name);
            quantities.add(quantity);
            units.add(unit);
            expiryDates.add(expiry == null ? "" : expiry);
        }

        cursor.close();
        db.close();

        if (names.isEmpty()) {
            recyclerPantry.setVisibility(RecyclerView.GONE);
            txtEmpty.setVisibility(TextView.VISIBLE);
        } else {
            recyclerPantry.setVisibility(RecyclerView.VISIBLE);
            txtEmpty.setVisibility(TextView.GONE);

            pantryAdapter = new PantryAdapter(
                    this,
                    ids,
                    names,
                    quantities,
                    units,
                    expiryDates
            );

            recyclerPantry.setAdapter(pantryAdapter);
        }
    }
}