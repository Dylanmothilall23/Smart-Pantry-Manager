package com.example.smartpantry;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
//Displaying suggested recipes in a RecyclerView on user click
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    //Context and Data arrays
    private final Context context;
    private final ArrayList<Integer> ids;
    private final ArrayList<String> names;

    public RecipeAdapter(Context context, ArrayList<Integer> ids, ArrayList<String> names) {
        this.context = context;
        this.ids = ids;
        this.names = names;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.txtRecipeName.setText(names.get(position));
        holder.txtRecipeInfo.setText("You have all required ingredients");
        //View recipe button click listener
        holder.btnViewRecipe.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("recipe_id", ids.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }
    //View components for each receipe item
    public static class RecipeViewHolder extends RecyclerView.ViewHolder {

        TextView txtRecipeName;
        TextView txtRecipeInfo;
        Button btnViewRecipe;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            txtRecipeName = itemView.findViewById(R.id.txtRecipeName);
            txtRecipeInfo = itemView.findViewById(R.id.txtRecipeInfo);
            btnViewRecipe = itemView.findViewById(R.id.btnViewRecipe);
        }
    }
}