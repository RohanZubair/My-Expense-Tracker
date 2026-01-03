package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private Context context;
    private ArrayList<String> expenseList;

    public ExpenseAdapter(Context context, ArrayList<String> expenseList) {
        this.context = context;
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        String data = expenseList.get(position);

        // Assuming data format is "ItemName - $Price"
        String[] parts = data.split(" - ");
        holder.tvTitle.setText(parts[0]);
        if (parts.length > 1) {
            holder.tvPrice.setText(parts[1]);
        }

        // Section 6.3: Popup Menu for item-specific actions
        holder.itemView.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(context, v);
            popup.getMenu().add("Edit");
            popup.getMenu().add("Delete");

            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Delete")) {
                    expenseList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, expenseList.size());
                    Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Edit Clicked", Toast.LENGTH_SHORT).show();
                }
                return true;
            });
            popup.show();
        });
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvPrice;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}