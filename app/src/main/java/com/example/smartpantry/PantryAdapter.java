package com.example.smartpantry;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
// Rendering pantry items inside the RecyclerView
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {
    //Arrays
    private Context context;
    private ArrayList<Integer> ids;
    private ArrayList<String> names;
    private ArrayList<String> quantities;
    private ArrayList<String> units;
    private ArrayList<String> expiryDates;

    public PantryAdapter(Context context, ArrayList<Integer> ids, ArrayList<String> names, ArrayList<String> quantities, ArrayList<String> units, ArrayList<String> expiryDates) {
        this.context = context;
        this.ids = ids;
        this.names = names;
        this.quantities = quantities;
        this.units = units;
        this.expiryDates = expiryDates;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }
    //Binding text
    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {

        holder.txtIngredientName.setText(names.get(position));
        holder.txtQuantity.setText("Quantity: " + quantities.get(position) + " " + units.get(position));

        if (expiryDates.get(position).isEmpty()) {
            holder.txtExpiry.setText("Expiry: Not provided");
        } else {
            holder.txtExpiry.setText("Expiry: " + expiryDates.get(position));
        }
        //Edit button listner
        holder.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddIngredientActivity.class);

            intent.putExtra("ingredient_id", ids.get(position));
            intent.putExtra("ingredient_name", names.get(position));
            intent.putExtra("quantity", quantities.get(position));
            intent.putExtra("unit", units.get(position));
            intent.putExtra("expiry_date", expiryDates.get(position));

            context.startActivity(intent);
        });
        //Delete button listener
        holder.btnDelete.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            builder.setTitle("Delete Ingredient");
            builder.setMessage("Are you sure you want to delete " + names.get(position) + "?");

            builder.setPositiveButton("Delete", (dialog, which) -> {

                DatabaseHelper databaseHelper = new DatabaseHelper(context);
                SQLiteDatabase db = databaseHelper.getWritableDatabase();

                db.delete("pantry", "id=?", new String[]{String.valueOf(ids.get(position))});

                db.close();

                ids.remove(position);
                names.remove(position);
                quantities.remove(position);
                units.remove(position);
                expiryDates.remove(position);

                notifyItemRemoved(position);
                notifyItemRangeChanged(position, names.size());
            });

            builder.setNegativeButton("Cancel", null);

            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }
    //Validates class that refer
    public static class PantryViewHolder extends RecyclerView.ViewHolder {

        TextView txtIngredientName;
        TextView txtQuantity;
        TextView txtExpiry;

        Button btnEdit;
        Button btnDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);

            txtIngredientName = itemView.findViewById(R.id.txtIngredientName);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtExpiry = itemView.findViewById(R.id.txtExpiry);

            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}