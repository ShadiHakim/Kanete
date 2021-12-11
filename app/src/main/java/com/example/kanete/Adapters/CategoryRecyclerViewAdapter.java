package com.example.kanete.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kanete.Models.Category;
import com.example.kanete.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    private List<Category> categories;
    private LayoutInflater inflater;
    private CategoryClickListener categoryClickListener;

    public CategoryRecyclerViewAdapter(Context context, List<Category> categories) {
        this.inflater = LayoutInflater.from(context);
        this.categories = categories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_category_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Category category = categories.get(position);
        Picasso.get().load(category.getImg()).into(holder.imageViewCategoryitem);
        holder.textViewCategoryitem.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewCategoryitem;
        TextView textViewCategoryitem;

        ViewHolder(View itemView) {
            super(itemView);
            imageViewCategoryitem = itemView.findViewById(R.id.imageViewCategoryitem);
            textViewCategoryitem = itemView.findViewById(R.id.textViewCategoryitem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (categoryClickListener != null) categoryClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Category getItem(int id) {
        return categories.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(CategoryClickListener itemClickListener) {
        this.categoryClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface CategoryClickListener {
        void onItemClick(View view, int position);
    }
}
